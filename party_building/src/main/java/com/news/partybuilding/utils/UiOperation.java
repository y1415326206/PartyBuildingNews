package com.news.partybuilding.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseApplication;

/**
 * UI相关的通用操作
 */
public class UiOperation {

  private static final String TAG = "UiOperation";

  private static ProgressDialog loading;

  // TODO 先不考虑不在Ui线程调用showLoading

  /**
   * loading动画相关_showLoading需要在Ui线程中调用
   * 建议在inflater.inflate或setContextView的后一行调用
   */
  public static void showLoading(Context context) {
    if (loading != null) {
      // 防止用户没等loading结束强行离开页面，进入别的页面时loading容易出错
      loading.dismiss();
      loading = null;
    }
    loading = new ProgressDialog(context, R.style.loading_dialog_center);
    loading.setCancelable(true);
    try {
      ((Activity) context).runOnUiThread(() -> loading.show());
    } catch (WindowManager.BadTokenException e) {
      // Unable to add window -- token android.os.BinderProxy@91c87de is not valid; is your activity running?
      Log.w(TAG, Log.getStackTraceString(e));
      loading = null;
    }
  }

  /**
   * loading动画相关_在「Ui线程中」调用closeLoading
   */
  public static void closeLoading() {
    if (loading == null) {
      return;
    }
    // 解决友盟上的bug
    // java.lang.IllegalArgumentException: View=DecorView@27e245a[WebViewActivity] not attached to window manager
    try {
      loading.dismiss();
    } catch (Exception e) {
      Log.i(TAG, "closeLoading: " + e.toString());
    } finally {
      loading = null;
    }
  }

  /**
   * loading动画相关_在「非Ui线程中」调用closeLoading
   * 未使用
   *
   * @deprecated
   */
  public static void closeLoading(Context context) {
    if (loading == null) {
      return;
    }
    if (isCurrentThreadIsUiThread()) {
      loading.dismiss();
      loading = null;
    } else {
      ((Activity) context).runOnUiThread(() -> {
        loading.dismiss();
        loading = null;
      });
    }
  }


  /**
   * toast相关_发送居中的toast消息，显示时间为长
   * TODO toast消息会不会带有app名称的前缀，因厂商而异(如小米有而魅族没有)
   */
  public static void toastCenter(String message) {
    Toast toast = Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  /**
   * toast相关_发送居中的toast消息，显示时间长度可设置Toast.LENGTH_LONG或Toast.LENGTH_SHORT
   */
  public static void toastCenter(String message, int messageDuration) {
    Toast toast = Toast.makeText(BaseApplication.getContext(), message, messageDuration);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  /**
   * toast相关_发送默认的toast消息，显示时间长度为Toast.LENGTH_SHORT
   */
  public static void toastShortDuration(String message) {
    Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_SHORT).show();
  }

  /**
   * 页面透明度相关_改变页面对比度，用于popupWindow出现时将背后的图层设为半透明
   */
  private static void changeOpacity(Window window, float ratio) {
    WindowManager.LayoutParams layoutParams = window.getAttributes();
    layoutParams.alpha = ratio;
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    window.setAttributes(layoutParams);
  }



  /**
   * 将显示模式设为全屏并将状态栏设为全透明
   * 用于有背景图的页面，如资产详情、邀请朋友
   * 所谓全屏显示指的是从状态栏开始占用状态栏空间进行渲染，而正常的页面是从状态栏下方开始渲染
   * TODO 先不考虑黑暗主题下的状态栏
   */
  public static void fullScreen(Activity activity) {
    Window window = activity.getWindow();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.TRANSPARENT);
    } else {
      window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
  }

  /**
   * loading动画相关_判断当前线程是否是Ui线程(Main Thread)
   */
  private static boolean isCurrentThreadIsUiThread() {
    return Looper.getMainLooper().getThread() == Thread.currentThread();
  }

}
