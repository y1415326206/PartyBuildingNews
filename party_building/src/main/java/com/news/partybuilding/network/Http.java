package com.news.partybuilding.network;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.BuildConfig;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {

  private static final String TAG = "== Http.java";

  private String url;
  private String queryString;
  private Map<String, String> params;
  private static OkHttpClient client;


  /**
   * 在BaseApplication的onCreate方法中初始化okHttp的Client
   */
  public static void initOkHttpClient() {
    client = new OkHttpClient.Builder()
      .connectTimeout(5000, TimeUnit.SECONDS)
      .readTimeout(5000, TimeUnit.SECONDS)
      .writeTimeout(5000, TimeUnit.SECONDS)
      .followRedirects(false)
      .followSslRedirects(false)
      //.sslSocketFactory(Http.getSslSocketFactory(inputStream))
      //.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
      .addInterceptor(new LoggingInterceptor()) // 日志拦截器
      .addInterceptor(new UserAgentInterceptor(getUserAgent())) // 请求头拦截器
      .build();
  }

  public Http(String url) {
    this.url = Urls.HTTP_SERVER_AND_URL_PREFIX + url;
    this.params = new HashMap<String, String>() {{
      put("os_type", "android");
      if (SharePreferenceUtil.isLogin()) {
        put("public_key", SharePreferenceUtil.getString("public_key", Constants.DEFAULT_STRING));
      }
      //put("os_version", SharePreferenceUtil.getString("os_version", Constants.DEFAULT_STRING));
      put("device_type", SharePreferenceUtil.getString("device_model", Constants.DEFAULT_STRING));
      put("app_version", BuildConfig.VERSION_NAME);
      put("model", SharePreferenceUtil.getString("device_model", Constants.DEFAULT_STRING));
      put("lang", SharePreferenceUtil.getString("language", Constants.DEFAULT_LANGUAGE));
    }};
  }

  public Http(String url, Map<String, String> paramsMap) {
    this.url = Urls.HTTP_SERVER_AND_URL_PREFIX + url;
    params = paramsMap;
    if (SharePreferenceUtil.isLogin()) {
      params.put("public_key", SharePreferenceUtil.getString("public_key", Constants.DEFAULT_STRING));
    }
    params.put("os_type", "android");
    //params.put("os_version", SharePreferenceUtil.getString("os_version", Constants.DEFAULT_STRING));
    params.put("device_type", SharePreferenceUtil.getString("device_model", Constants.DEFAULT_STRING));
    params.put("app_version", BuildConfig.VERSION_NAME);
    params.put("model", SharePreferenceUtil.getString("device_model", Constants.DEFAULT_STRING));
    params.put("lang", SharePreferenceUtil.getString("language", Constants.DEFAULT_LANGUAGE));
  }

  public interface ResponseCallBack {
    void onResponse(String response);
    void OnFailure(String exception);
  }

  public void get(ResponseCallBack callback) {
    queryString();
    Request request = new Request.Builder()
      .url(this.url + '?' + queryString + "signature=" + generateSignature())
      .build();
    try {
      client.newCall(request).enqueue(new Callback() {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
          if (response.code() == 500 || response.code() == 502) {
            handleServerErrorResponse(response);
            return;
          }
          assert response.body() != null;
          callback.onResponse(response.body().string());
        }

        @Override
        public void onFailure(Call call, IOException e) {
          callback.OnFailure(Log.getStackTraceString(e));
          Log.e(TAG, Log.getStackTraceString(e));
        }
      });
    } catch (Exception e) {
      Log.e(TAG, "get" + Log.getStackTraceString(e));
    }
  }

  public void post(ResponseCallBack callback) {
    queryString();
    FormBody.Builder formBuilder = new FormBody.Builder();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      formBuilder.add(entry.getKey(), entry.getValue());
    }
    formBuilder.add("signature", generateSignature());
    Request request = new Request.Builder()
      .url(this.url)
      .post(formBuilder.build())
      .build();
    try {
      client.newCall(request).enqueue(new Callback() {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
          if (response.code() == 500 || response.code() == 502) {
            handleServerErrorResponse(response);
            return;
          }
          assert response.body() != null;
          callback.onResponse(response.body().string());
        }

        @Override
        public void onFailure(Call call, IOException e) {
          Log.e(TAG, Log.getStackTraceString(e));
        }
      });
    } catch (Exception e) {
      Log.e(TAG, "post" + Log.getStackTraceString(e));
    }
  }

  // 步骤1. 加入public_key、nonce拼接出queryString格式的字符串，用于生成数字签名
  private void queryString() {
    params.put("public_key", SharePreferenceUtil.getString("public_key", Constants.DEFAULT_STRING));
    params.put("nonce", Long.toString(System.currentTimeMillis() / 1000));
    StringBuilder stringBuilder = new StringBuilder();
    // 把params的keys排序后遍历
    for (String key : (new TreeSet<>(params.keySet()))) {
      stringBuilder.append(key).append("=").append(params.get(key)).append("&");
    }
    // 生成的queryString最后一位是&
    this.queryString = stringBuilder.toString();
    Log.i(TAG, "queryString => " + queryString);
  }

  // 步骤2. 获取signature
  private String generateSignature() {
    // Android的JDK环境无法使用DigestUtils.md5Hex方法
    return new String(Hex.encodeHex(DigestUtils.md5(queryString + "private_key=" + SharePreferenceUtil.getString("private_key", Constants.DEFAULT_STRING))));
  }

  // 不生成参数签名的get请求
  public void getWithoutSignature(ResponseCallBack callback) {
    StringBuilder queryString = new StringBuilder();
    // 把params的keys排序后遍历
    for (String key : (new TreeSet<>(params.keySet()))) {
      queryString.append(key).append("=").append(params.get(key)).append("&");
    }
    Request request = new Request.Builder()
      .url(this.url + "?" + queryString.substring(0, queryString.length() - 1))
      .build();
    try {
      client.newCall(request).enqueue(new Callback() {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
          if (response.code() == 500 || response.code() == 502) {
            handleServerErrorResponse(response);
            return;
          }
          assert response.body() != null;
          callback.onResponse(response.body().string());
        }

        @Override
        public void onFailure(Call call, IOException e) {
          Log.e(TAG, Log.getStackTraceString(e));
        }
      });
    } catch (Exception e) {
      Log.e(TAG, "getWithoutSignature" + Log.getStackTraceString(e));
    }
  }


  private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

  public void postFiles(Map<String, String> files, ResponseCallBack callback) {
    // 先不把文件参数加进去计算签名
    queryString();
    MultipartBody.Builder formBuilder = new MultipartBody.Builder();
    formBuilder.setType(MultipartBody.FORM);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      formBuilder.addFormDataPart(entry.getKey(), entry.getValue());
    }
    formBuilder.addFormDataPart("signature", generateSignature());
    // 往请求的body添加文件参数
    for (Map.Entry<String, String> entry : files.entrySet()) {
      File file = new File(entry.getValue());
      formBuilder.addFormDataPart(entry.getKey(), file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
      Log.i(TAG, "filePathKey = " + entry.getKey() + "file.getName()" + file.getName());
    }
    Request request = new Request.Builder()
      .url(this.url)
      .post(formBuilder.build())
      .build();
    try {
      client.newCall(request).enqueue(new Callback() {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
          if (response.code() == 500 || response.code() == 502) {
            handleServerErrorResponse(response);
            return;
          }
          assert response.body() != null;
          callback.onResponse(response.body().string());
        }

        @Override
        public void onFailure(Call call, IOException e) {
          Log.e(TAG, Log.getStackTraceString(e));
        }
      });
    } catch (Exception e) {
      Log.e(TAG, "postFiles" + Log.getStackTraceString(e));
    }
  }

  // 日志输出从request到response服务器的响应时间
  public static class LoggingInterceptor implements Interceptor {

    @SuppressLint("DefaultLocale")
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
      Request request = chain.request();
      long startTime = System.currentTimeMillis();

      Response response = chain.proceed(request);
      long endTime = System.currentTimeMillis();
      LogUtils.i(TAG, response.request().url().toString());
      LogUtils.i(TAG, String.format("response in %d ms", (endTime - startTime)));

      return response;
    }
  }

  private static void handleServerErrorResponse(Response response) {
    LogUtils.e(TAG, "handleServerErrorResponse: " + response.toString());
    new android.os.Handler(Looper.getMainLooper()).post(() -> ToastUtils.show("服务器出错了！"));
  }


  /**
   * 返回正确的UserAgent
   *
   * @return
   */
  public static String getUserAgent() {
    String userAgent = "";
    StringBuffer sb = new StringBuffer();
    userAgent = System.getProperty("http.agent");//Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo X9L Build/MMB29M)

    for (int i = 0, length = userAgent.length(); i < length; i++) {
      char c = userAgent.charAt(i);
      if (c <= '\u001f' || c >= '\u007f') {
        sb.append(String.format("\\u%04x", (int) c));
      } else {
        sb.append(c);
      }
    }

    LogUtils.i("User-Agent", "User-Agent: " + sb.toString());
    return sb.toString();
  }

  // UserAgent 过滤器 只有通过这个方式才能改变http的user-agent
  public static class UserAgentInterceptor implements Interceptor {
    private final String mUserAgent;

    public UserAgentInterceptor(String userAgent) {
      mUserAgent = userAgent;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
      Request request = chain.request()
        .newBuilder()
        .header("User-Agent", mUserAgent)
        .build();
      return chain.proceed(request);
    }
  }

}
