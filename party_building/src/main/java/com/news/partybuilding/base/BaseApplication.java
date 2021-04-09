package com.news.partybuilding.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatDelegate;

import com.news.partybuilding.BuildConfig;
import com.news.partybuilding.R;
import com.news.partybuilding.manager.MyActivityManager;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;


// 当您的应用及其引用的库包含的方法数超过 65536 时，您会遇到一个构建错误，指明您的应用已达到 Android 构建架构规定的引用限制
// 如果您的 minSdkVersion 设为 21 或更高版本，系统会默认启用 MultiDex，并且您不需要 MultiDex 库。
// 如果小于 21 可以添加依赖 implementation "androidx.multidex:multidex:2.0.1"
// 并这样继承就行了 BaseApplication extends MultiDexApplication


public class BaseApplication extends Application {

  // 并不会造成内存泄漏，因为这是一个Application Context，如果你执有的是一个静态的Activity Context，那就会内存泄漏的。
  // Android Studio并没有智能地判断出来这是Application Context，所以这是一个误报，并没有任何影响，如果你看着觉得不舒服，
  // 可以在字段上加个这个注解 @SuppressLint("StaticFieldLeak")

  @SuppressLint("StaticFieldLeak")
  private static Context context;

  public static Context getContext() {
    return context;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    context = this;
    // 初始化一些工具
    init();
    initActivityManager();
    setCurrentTheme();
  }

  private void init() {
    // 在 Application 中初始化 toast
    ToastUtils.init(this);
    // 初始化OkHttpClient
    Http.initOkHttpClient();
    // 初始化友盟
    initUmeng();
  }

  /**
   * 初始化友盟
   */
  private void initUmeng() {
    //获取渠道号
    //String channelName = AnalyticsConfig.getChannel(this);
    /**
     * 初始化common库
     * 参数1:上下文，必须的参数，不能为空
     * 参数2:友盟 app key
     * 参数3:友盟 channel 渠道 表示用户从哪个地方安装的比如 YingYongBao  WanDouJia
     * 参数4:设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；
     * 传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机
     * 参数5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
     */
    UMConfigure.init(this,"606e814518b72d2d244a5ed1","Umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
    // 设置组件化的Log开关
    // 参数: boolean 默认为false，如需查看LOG设置为true
    UMConfigure.setLogEnabled(BuildConfig.IS_DEBUG);
    // 选用AUTO页面采集模式
    MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
  }

  private void setCurrentTheme() {
    //根据app上次退出的状态来判断是否需要设置夜间模式,提前在SharedPreference中存了一个是否是夜间模式的boolean值
    boolean isNightMode = SharePreferenceUtil.getBoolean("is_set_night_theme", false);
    if (isNightMode) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
  }

  /**
   * 管理Activity
   */
  private void initActivityManager() {
    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
      }

      @Override
      public void onActivityStarted(@NonNull Activity activity) {
      }

      @Override
      public void onActivityResumed(@NonNull Activity activity) {
        MyActivityManager.getInstance().setCurrentActivity(activity);
      }

      @Override
      public void onActivityPaused(@NonNull Activity activity) {
      }

      @Override
      public void onActivityStopped(@NonNull Activity activity) {
      }

      @Override
      public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
      }

      @Override
      public void onActivityDestroyed(@NonNull Activity activity) {
      }
    });
  }

  /*
   * 设置全局的上拉加载和下拉刷新的样式, static 代码段可以防止内存泄露
   */
  static {
    //设置全局的Header构建器
    SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
      layout.setPrimaryColorsId(R.color.colorWhite, R.color.colorMainColor);//全局设置主题颜色
      return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
    });
    //设置全局的Footer构建器
    SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
      //指定为经典Footer，默认是 BallPulseFooter
      return new ClassicsFooter(context).setDrawableSize(20);
    });
  }


  static {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
  }


}
