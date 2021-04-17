package com.news.partybuilding.ui.activity.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivityWebviewBinding;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.viewmodel.WebViewViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class WebViewActivity extends BaseActivity<ActivityWebviewBinding, WebViewViewModel> {

  private WebSettings webSettings;
  private String url;

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_webview;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(WebViewViewModel.class);
  }

  @Override
  protected void init() {
    getUrl();
    settingWebView();
    goBack();
  }


  /**
   * 获取url链接
   */
  private void getUrl(){
   if (getIntent().hasExtra(Constants.WEB_VIEW_URL)){
      url = getIntent().getStringExtra(Constants.WEB_VIEW_URL);
   }else {
     url = "";
   }
  }

  /**
   * 设置webView
   */
  @SuppressLint("SetJavaScriptEnabled")
  private void settingWebView() {

    binding.webView.getSettings().setJavaScriptEnabled(true);
    binding.webView.getSettings().setDomStorageEnabled(true);
    binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
    //webView.setWebViewClient( new MyWebViewClient(webView,title));
    binding.webView.setDefaultHandler(new DefaultHandler());
    //webView.loadUrl("file:///android_asset/text.html");
    binding.webView.loadUrl(url);
//    binding.webView.setWebChromeClient(new WebChromeClient() {
//      @Override
//      public void onReceivedTitle(WebView view, String title) {
//        super.onReceivedTitle(view, title);
//
//      }
//
//    });

//    webSettings = binding.webView.getSettings();
//    //如果访问的页面中要与Javascript交互，则webView必须设置支持Javascript
//    webSettings.setJavaScriptEnabled(true);
//    //设置自适应屏幕，两者合用
//    webSettings.setUseWideViewPort(true); //将图片调整到适合webView的大小
//    webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//    //缩放操作
//    webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//    webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//    webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//
//    //其他细节操作
//    webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webView中缓存
//    webSettings.setAllowFileAccess(true); //设置可以访问文件
//    webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//    webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//    webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//    binding.webView.loadUrl(url);
//    // 在页面加载过程中显示loading动画
    binding.webView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageStarted(WebView view, String url, Bitmap facIcon) {
        binding.loading.setVisibility(View.VISIBLE);
        binding.webView.setVisibility(View.GONE);
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        // 忽略SSL的证书错误
        handler.proceed();
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        binding.loading.setVisibility(View.GONE);
        binding.webView.setVisibility(View.VISIBLE);
      }
    });



//    binding.webView.registerHandler("start_alipay", new BridgeHandler() {
//      @Override
//      public void handler(String data, CallBackFunction function) {
////        Log.i("hahah", "data from web = " + data);
//
//      }
//    });
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onStop() {
    super.onStop();
    // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）当页面不展示是改为false
    binding.webView.getSettings().setJavaScriptEnabled(false);
  }


  // 改写物理按键的返回的逻辑
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (binding.webView.canGoBack()) {
        // 返回上一页面
        binding.webView.goBack();
        return true;
      } else {
        finish();
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onDestroy() {
    if (binding.webView != null) {
      binding.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
      binding.webView.clearHistory();
      ((ViewGroup) binding.webView.getParent()).removeView(binding.webView);
      binding.webView.destroy();
    }
    super.onDestroy();
  }

  /**
   * 返回
   */
 private void goBack(){
   binding.goBack.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
       finish();
     }
   });
 }

}