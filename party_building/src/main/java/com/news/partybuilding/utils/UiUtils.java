package com.news.partybuilding.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;

import com.news.partybuilding.R;

public class UiUtils {

  // 目前用于登录 绑定谷歌验证码时 发送验证码样式
  @SuppressLint("UseCompatLoadingForDrawables")
  public static void resendValidationCodeCommon(Button button, EditText editText, Context context) {
    // 修改按钮样式并禁用按钮
    button.setTextColor(context.getResources().getColor(R.color.colorTextHintGray));
    button.setBackground(context.getResources().getDrawable(R.drawable.bg_gray_half_border_solid));
    button.setEnabled(false);
    new CountDownTimer(10000, 1000) {
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
}
