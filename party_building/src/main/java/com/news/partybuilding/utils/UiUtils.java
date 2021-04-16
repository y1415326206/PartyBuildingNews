package com.news.partybuilding.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;

import com.gyf.immersionbar.ImmersionBar;
import com.news.partybuilding.R;

public class UiUtils {

  // 目前用于登录 绑定谷歌验证码时 发送验证码样式
  @SuppressLint("UseCompatLoadingForDrawables")
  public static void resendValidationCodeCommon(Button button, EditText editText, Context context) {
    // 修改按钮样式并禁用按钮
    button.setTextColor(context.getResources().getColor(R.color.colorTextHintGray));
    button.setBackground(context.getResources().getDrawable(R.drawable.bg_gray_half_border_solid));
    button.setEnabled(false);
    new CountDownTimer(60000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
        button.setText(context.getResources().getString(R.string.resend_countdown, String.valueOf(millisUntilFinished / 1000)));
      }

      @Override
      public void onFinish() {
        if (editText.getText().toString().isEmpty()){
          button.setText(context.getResources().getString(R.string.resend_validation_code));
          button.setTextColor(context.getResources().getColor(R.color.colorTextHintGray));
          button.setBackground(context.getResources().getDrawable(R.drawable.bg_gray_half_border_solid));
          button.setEnabled(false);
        }else {
          button.setText(context.getResources().getString(R.string.resend_validation_code));
          button.setBackground(context.getResources().getDrawable(R.drawable.bg_gray_half_border_empty));
          button.setTextColor(context.getResources().getColor(R.color.colorTextColor));
          button.setEnabled(true);
        }
      }
    }.start();
  }


  /**
   * 设置状态栏颜色
   */
  public static void setStatusBar(Activity context){
    ImmersionBar.with(context)
      .statusBarColor(R.color.colorWhite)     //状态栏颜色，不写默认透明色
      .navigationBarColor(R.color.colorWhite)  //导航栏颜色，不写默认黑色;
      .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
      .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
      .fitsSystemWindows(true)
      .init();
  }

  public static String hideUserAccount(String userAccountNotHide){
    if (userAccountNotHide.contains("@")){
      return userAccountNotHide.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }else {
      return userAccountNotHide.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }
  }
}
