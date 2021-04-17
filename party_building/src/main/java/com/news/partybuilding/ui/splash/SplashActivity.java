package com.news.partybuilding.ui.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.BuildConfig;
import com.news.partybuilding.MainActivity;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivitySplashBinding;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.viewmodel.SplashViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {
  private final String TAG = "SplashActivity==";
  private OnBoardAdapter onboardAdapter;
  private static final int SPLASH_TIME_OUT = 1000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LogUtils.i(TAG, "onCreate");
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_splash;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    binding.setViewModel(viewModel);
  }

  @Override
  protected void init() {
    //checkForUpdate();
    initStartUp();
  }

  private void checkForUpdate() {

    new Http(Urls.CHECK_UPDATE, new HashMap<String, String>() {{
      put("app_version", BuildConfig.VERSION_NAME);
    }}).getWithoutSignature(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonResponse = new JSONObject(response);
          if (jsonResponse.getInt("code") != Constants.SUCCESS_CODE) {
            runOnUiThread(SplashActivity.this::initStartUp);
            return;
          }
          jsonResponse = jsonResponse.getJSONObject("data");
          final boolean isForceUpdate = jsonResponse.getBoolean("is_force_update");
          Log.i(TAG, "checkForUpdate: " + isForceUpdate);
          if (!isForceUpdate) {
            runOnUiThread(SplashActivity.this::initStartUp);
            return;
          }
          final String version = jsonResponse.getString("version");
          final String url = jsonResponse.getString("url");
          runOnUiThread(() -> {
            AlertDialog updateDialog = new AlertDialog.Builder(SplashActivity.this)
              .setTitle("发现新版本" + version)
              .setMessage("请立即下载")
              .setPositiveButton("下载更新", (dialogInterface, i) -> {
                Toast.makeText(SplashActivity.this, "更新下载中...", Toast.LENGTH_SHORT).show();
                startDownload(url);
              })
              .create();
            updateDialog.setCanceledOnTouchOutside(false);
            updateDialog.show();
          });
        } catch (JSONException e) {
          Log.e(TAG, Log.getStackTraceString(e));
        }
      }

      @Override
      public void OnFailure(String exception) {

      }
    });
  }

  /**
   * 开始下载apk
   */
  @SuppressLint("StaticFieldLeak")
  private void startDownload(String apk_url) {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.show();

    String versionName = apk_url.substring(apk_url.lastIndexOf('/') + 1);
    File cacheDir = this.getExternalCacheDir();
    Log.d(TAG, "== cacheDir: " + cacheDir.getAbsolutePath());
    final File file = new File(cacheDir, versionName);
    if (!cacheDir.exists()) {
      Log.d(TAG, "== cacheDir not exists, making dir...");
      cacheDir.mkdirs();
    }
    String filePath = file.getAbsolutePath();
    Log.d(TAG, "== download file path: " + filePath);

    new AsyncDownloader() {
      @Override
      protected void onProgressUpdate(Long... values) {
        long progress = values[0];
        long max = values[1];

        progressDialog.setProgress((int) progress);
        progressDialog.setMax((int) max);
        progressDialog.setProgressNumberFormat((humanReadableByteCount(progress, true)) + "/" + (humanReadableByteCount(max, true)));
      }

      @Override
      protected void onPostExecute(Boolean result) {
        Log.d(TAG, "== download result: " + result);
        if (result) {
          installApk(file);
        }
        progressDialog.dismiss();
      }
    }.execute(apk_url, filePath);
  }

  /**
   * 安装apk
   *
   * @param apkFile apk的路径
   */
  private void installApk(File apkFile) {
    Log.d(TAG, "== installApk: " + apkFile);

    Intent intent;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

      Log.d(TAG, "== Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
      if (!canReadWriteExternal()) {
        Log.d(TAG, canReadWriteExternal() + ". --- before request, canReadWriteExternal()");
        requestPermission();
        Log.d(TAG, canReadWriteExternal() + ". --- after request, canReadWriteExternal()");

      }

      Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", apkFile);
      intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
      intent.setData(apkUri);
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

    } else {
      Uri apkUri = Uri.fromFile(apkFile);
      intent = new Intent(Intent.ACTION_VIEW);
      intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    Log.d(TAG, "== canReadWriteExternal: " + canReadWriteExternal());

    startActivity(intent);

  }

  /**
   * 下载apk的对话框，将Bytes格式化为Kb/Mb
   */
  @SuppressLint("DefaultLocale")
  public static String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }

  /**
   * 以下为获取权限
   */
  private static final int REQUEST_WRITE_PERMISSION = 786;

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
      Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
  }

  private void requestPermission() {
    Log.d(TAG, "== in requestPermission");
    Log.d(TAG, "== before requestPermissions....");
    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
  }

  private boolean canReadWriteExternal() {
    return ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
  }

  public static class AsyncDownloader extends AsyncTask<String, Long, Boolean> {

    @Override
    protected Boolean doInBackground(String... objects) {
      String url = objects[0];
      String filePath = objects[1];
      OkHttpClient httpClient = new OkHttpClient();
      Call call = httpClient.newCall(new Request.Builder().url(url).get().build());
      Log.d("===", "in doInBackground");
      try {
        Response response = call.execute();
        Log.d("AsyncDownloader", "== code:" + response.code());
        if (response.code() == 200) {
          InputStream inputStream = null;
          try {
            Log.d("AsyncDownloader", "== downloading");
            inputStream = response.body().byteStream();
            byte[] buff = new byte[1024 * 4];
            long downloaded = 0L;
            long target = response.body().contentLength();

            publishProgress(0L, target);

            OutputStream outputStream = new FileOutputStream(filePath);
            while (true) {
              int read = inputStream.read(buff);
              if (read == -1) {
                break;
              }
              outputStream.write(buff, 0, read);
              //write buff
              downloaded += read;
              publishProgress(downloaded, target);
              if (isCancelled()) {
                return false;
              }

            }
            return true;
          } catch (IOException ignore) {
            Log.e("==", ignore.toString());
            return false;
          } finally {
            if (inputStream != null) {
              inputStream.close();
            }
          }
        } else {
          return false;
        }
      } catch (Exception e) {
        Log.e("AsyncDownloader", Log.getStackTraceString(e));
        return false;
      }
    }
  }


  private void initStartUp() {
    if (SharePreferenceUtil.getBoolean("is_already_show_view_pager", false)) {
      binding.viewPager.setVisibility(View.GONE);
      toFinish();
    } else {
      binding.partyBuildingTopText.setVisibility(View.GONE);
      binding.partyBuildingBottomText.setVisibility(View.GONE);
      binding.viewPager.setVisibility(View.VISIBLE);
      //binding.animationView.setVisibility(View.GONE);
      setItemData();
      binding.viewPager.setAdapter(onboardAdapter);
      setIndicator();
      setCurrentIndicator(0);
      binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
          super.onPageSelected(position);
          setCurrentIndicator(position);
          if (position == 2) {
            binding.materialButton.setVisibility(View.VISIBLE);
            binding.linearIndicator.setVisibility(View.GONE);
            binding.materialButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                SharePreferenceUtil.setParam("is_already_show_view_pager", true);
                startNow();
              }
            });
          } else {
            binding.materialButton.setVisibility(View.GONE);
            binding.linearIndicator.setVisibility(View.VISIBLE);
          }
        }
      });
    }

    // 首次开启App时获取设备信息(型号与安卓系统版本)
    // 如果未获取过设备信息(型号与安卓系统版本)，则采集设备信息：
    String model = android.os.Build.MODEL;
    String product = android.os.Build.PRODUCT;
    if (model.length() > product.length()) {
      SharePreferenceUtil.setParam("device_model", model);
    } else {
      SharePreferenceUtil.setParam("device_model", product);
    }
    SharePreferenceUtil.setParam("os_version", Build.VERSION.RELEASE);
  }

  private void toFinish() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(home);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
      }
    }, SPLASH_TIME_OUT);
  }

  private void startNow() {
    Intent home = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(home);
    finish();
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }

  // 设置初次安装时的数据源
  private void setItemData() {
    List<OnboardItem> onBoardItems = new ArrayList<>();
    OnboardItem startOne = new OnboardItem();
    startOne.setTitle("Hello Welcome");
    startOne.setImage(R.drawable.start_one);

    OnboardItem startTwo = new OnboardItem();
    startTwo.setTitle("How Are You!");
    startTwo.setImage(R.drawable.start_two);

    OnboardItem startThree = new OnboardItem();
    startThree.setTitle("Ok Let's Start!");
    startThree.setImage(R.drawable.start_three);

    onBoardItems.add(startOne);
    onBoardItems.add(startTwo);
    onBoardItems.add(startThree);

    onboardAdapter = new OnBoardAdapter(onBoardItems);
  }

  private void setIndicator() {
    ImageView[] indicators = new ImageView[onboardAdapter.getItemCount()];
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(0, 0, 8, 0);
    for (int i = 0; i < indicators.length; i++) {
      indicators[i] = new ImageView(getApplicationContext());
      indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
      indicators[i].setLayoutParams(layoutParams);
      binding.linearIndicator.addView(indicators[i]);
    }
  }

  private void setCurrentIndicator(int index) {
    int childCount = binding.linearIndicator.getChildCount();
    for (int i = 0; i < childCount; i++) {
      ImageView imageView = (ImageView) binding.linearIndicator.getChildAt(i);
      if (i == index) {
        imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
      } else {
        imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    LogUtils.i(TAG, "destroy");
  }
}