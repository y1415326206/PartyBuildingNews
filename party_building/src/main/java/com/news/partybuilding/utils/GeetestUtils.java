package com.news.partybuilding.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.geetest.sdk.GT3ConfigBean;
import com.geetest.sdk.GT3ErrorBean;
import com.geetest.sdk.GT3GeetestUtils;
import com.geetest.sdk.GT3Listener;
import com.news.partybuilding.base.BaseApplication;
import com.news.partybuilding.network.Urls;

import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeetestUtils {
  private static GT3ConfigBean gt3ConfigBean;

  public static OnGT3Listener onGT3Listener;

  public interface OnGT3Listener {
    void onSuccess(String gt3Result);

    void onFail(String gt3Result);
  }


  public static void initGt3(Context context, GT3GeetestUtils gt3GeetestUtils, OnGT3Listener listener) {
    Locale locale = context.getResources().getConfiguration().locale;
    //Log.i("TAG in geetest",locale.getLanguage());
    onGT3Listener = listener;
    // 配置bean文件，也可在oncreate初始化
    gt3ConfigBean = new GT3ConfigBean();
    // 设置验证模式，1：bind，2：unbind
    gt3ConfigBean.setPattern(1);
    // 设置点击灰色区域是否消失，默认不消失
    gt3ConfigBean.setCanceledOnTouchOutside(false);
    // 设置语言，如果为null则使用系统默认语言
    gt3ConfigBean.setLang(locale.getLanguage());
    Log.i("TAG in geetest","geetest language=====" + gt3ConfigBean.getLang());
    // 设置加载webview超时时间，单位毫秒，默认10000，仅且webview加载静态文件超时，不包括之前的http请求
    gt3ConfigBean.setTimeout(10000);
    // 设置webview请求超时(用户点选或滑动完成，前端请求后端接口)，单位毫秒，默认10000
    gt3ConfigBean.setWebviewTimeout(10000);
    // 设置自定义view
//        gt3ConfigBean.setLoadImageView(new TestLoadingView(this));
    // 设置回调监听
    gt3ConfigBean.setListener(new GT3Listener() {

//      /**
//       * api1结果回调
//       * @param result
//       */
//      @Override
//      public void onApi1Result(String result) {
//
//      }

      /**
       * 验证码加载完成
       * @param duration 加载时间和版本等信息，为json格式
       */
      @Override
      public void onDialogReady(String duration) {

      }

      @Override
      public void onReceiveCaptchaCode(int i) {

      }

      /**
       * 验证结果
       * @param result
       */
      @Override
      public void onDialogResult(String result) {
//        Locale locale = context.getResources().getConfiguration().locale;
//        Log.i("TAG geetest","_onDialogResult====="+locale.getLanguage());
//        Log.i("log message", "onDialogResult----RequestAPI2---" + result);
        // 开启api2逻辑
//                new RequestAPI2().execute(result);
//                gt3GeetestUtils.showSuccessDialog();
        listener.onSuccess(result);
      }

//      /**
//       * api2回调
//       * @param result
//       */
//      @Override
//      public void onApi2Result(String result) {
//
//      }

      /**
       * 统计信息，参考接入文档
       * @param result
       */
      @Override
      public void onStatistics(String result) {

      }

      /**
       * 验证码被关闭
       * @param num 1 点击验证码的关闭按钮来关闭验证码, 2 点击屏幕关闭验证码, 3 点击返回键关闭验证码
       */
      @Override
      public void onClosed(int num) {

      }

      /**
       * 验证成功回调
       * @param result
       */
      @Override
      public void onSuccess(String result) {
//        Locale locale = context.getResources().getConfiguration().locale;
//        Log.i("TAG geetest","_onSuccess====="+locale.getLanguage());
        gt3GeetestUtils.showSuccessDialog();

      }

      /**
       * 验证失败回调
       * @param errorBean 版本号，错误码，错误描述等信息
       */
      @Override
      public void onFailed(GT3ErrorBean errorBean) {
        gt3GeetestUtils.showFailedDialog();
        listener.onFail(errorBean.toString());
      }

      /**
       * api1回调
       */
      @Override
      public void onButtonClick() {
        new RequestAPI1(context, gt3GeetestUtils).execute();
      }
    });
    gt3GeetestUtils.init(gt3ConfigBean);
    // 开启验证
    gt3GeetestUtils.startCustomFlow();
  }


  private static RequestQueue requestQueue;

  /**
   * 请求api1
   */
  static class RequestAPI1 extends AsyncTask<Void, Void, JSONObject> {

    private static final String TAG = "GeetestUtils";
    private final GT3GeetestUtils gt3GeetestUtils;
    private final Context context;

    public RequestAPI1(Context context, GT3GeetestUtils gt3GeetestUtils) {
      this.context = context;
      this.gt3GeetestUtils = gt3GeetestUtils;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
      if (requestQueue == null) {
        requestQueue = Volley.newRequestQueue(BaseApplication.getContext());
      }
      HttpsTrustManager.allowAllSSL();
      RequestFuture<JSONObject> future = RequestFuture.newFuture();
      JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST,
        Urls.GEETEST, new JSONObject(), future, future);
      requestQueue.add(request);
      JSONObject jsonObject = null;
      try {
        // 发起volley的同步请求
        jsonObject = future.get();
      } catch (Exception e) {
        Log.e(TAG, Log.getStackTraceString(e));
      }
      return jsonObject;
    }

    /**
     * @deprecated
     * FIXME okhttp请求极验接口，暂时想不到办法解决SSL证书错误
     */
    protected JSONObject doInBackgroundOld(Void... params) {
      OkHttpClient okHttpClient = new OkHttpClient();
      okHttpClient.followRedirects();
      okHttpClient.followSslRedirects();
      HttpsTrustManager.allowAllSSL();
      Request request = new Request.Builder().url(Urls.GEETEST).build();
      JSONObject jsonObject = null;
      try {
        Response response = okHttpClient.newCall(request).execute();
        assert response.body() != null;
        String responseString = response.body().string();
        Log.i(TAG, "doInBackground: " + responseString);
        jsonObject = new JSONObject(responseString);
      } catch (Exception e) {
        Log.e(TAG, Log.getStackTraceString(e));
      }
      return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject params) {
      // 继续验证

      // SDK可识别格式为
      // {"success":1,"challenge":"06fbb267def3c3c9530d62aa2d56d018","gt":"019924a82c70bb123aae90d483087f94"}
      // TODO 将api1请求结果传入此方法，即使为null也要传入，SDK内部已处理，否则易出现无限loading
      gt3ConfigBean.setApi1Json(params);
      // 继续api验证
      gt3GeetestUtils.getGeetest();
    }
  }

}
