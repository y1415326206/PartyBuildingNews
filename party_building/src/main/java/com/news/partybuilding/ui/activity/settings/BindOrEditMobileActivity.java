package com.news.partybuilding.ui.activity.settings;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.geetest.sdk.GT3GeetestUtils;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivityBindOrEditMobileBinding;
import com.news.partybuilding.model.GT3Bean;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.utils.GeetestUtils;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.viewmodel.BindOrEditMobileViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BindOrEditMobileActivity extends BaseActivity<ActivityBindOrEditMobileBinding, BindOrEditMobileViewModel> implements View.OnClickListener {

  private GT3GeetestUtils gt3GeetestUtils;
  @Override
  protected int getLayoutResId() {
    return R.layout.activity_bind_or_edit_mobile;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(BindOrEditMobileViewModel.class);
    binding.setData(viewModel);
  }

  @Override
  protected void init() {
    gt3GeetestUtils = new GT3GeetestUtils(this);
    setStatusBar();
    initListener();
    onEditTextChangeListener();
  }

  /**
   * 设置状态栏颜色
   */
  private void setStatusBar() {
    UiUtils.setStatusBar(this);
  }

  private void initListener() {
    binding.closeButton.setOnClickListener(this);
    binding.requestCodeButton.setOnClickListener(this);
    binding.bind.setOnClickListener(this);
  }

  private void onEditTextChangeListener() {
    binding.phoneNumberEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 当手机号和验证码都不为空时
        if (!binding.phoneNumberEdit.getText().toString().isEmpty() && !binding.codeEdit.getText().toString().isEmpty()) {
          viewModel.isBothEditFull.postValue(true);
        } else {
          viewModel.isBothEditFull.postValue(false);
        }
        if (!binding.requestCodeButton.getText().toString().contains("s")) {
          // 如果EditText为空 设置isPhoneEditEmpty为true
          viewModel.isPhoneEditEmpty.postValue(binding.phoneNumberEdit.getText().toString().isEmpty());
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    binding.codeEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!binding.phoneNumberEdit.getText().toString().isEmpty() && !binding.codeEdit.getText().toString().isEmpty()) {
          viewModel.isBothEditFull.postValue(true);
        } else {
          viewModel.isBothEditFull.postValue(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {


      }
    });
  }

  // 发送验证码极验
  private void checkGeeTestWhenSendCode() {
    GeetestUtils.initGt3(this, gt3GeetestUtils, new GeetestUtils.OnGT3Listener() {
      @Override
      public void onSuccess(String gt3Result) {
        //Log.i("log message", "PasswordLoginActivity------onSuccess" + gt3Result);
        gt3GeetestUtils.dismissGeetestDialog();
        GT3Bean gt3Bean = new Gson().fromJson(gt3Result, GT3Bean.class);
        sendSms();
      }

      @Override
      public void onFail(String gt3Result) {
        //Log.i("log message", "PasswordLoginActivity------onFail" + gt3Result);
        gt3GeetestUtils.dismissGeetestDialog();
      }
    });
  }

  // 发送验证码
  private void sendSms() {
    new Http(Urls.SEND_BIND_MOBILE_SMS_TOKEN_TO_MEMBER, new HashMap<String, String>() {{
      put("mobile", binding.phoneNumberEdit.getText().toString());
//      put("geetest_challenge", gt3Bean.geetest_challenge);
//      put("geetest_seccode", gt3Bean.geetest_seccode);
//      put("geetest_validate", gt3Bean.geetest_validate);
    }}).post(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          int code = jsonObject.getInt("code");
          String message = jsonObject.getString("message");
          ToastUtils.show(message);
          if (code == Constants.SUCCESS_CODE) {
            runOnUiThread(() -> {
              // 修改验证码
              UiUtils.resendValidationCodeCommon(binding.requestCodeButton, binding.phoneNumberEdit, getApplicationContext());
            });
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void OnFailure(String exception) {

      }
    });
  }


      // 绑定或换绑
      private void bindOrChangeBind() {
        new Http(Urls.BIND_MOBILE, new HashMap<String, String>() {{
          put("public_key", SharePreferenceUtil.getString(Constants.PUBLIC_KEY,""));
          put("mobile", binding.phoneNumberEdit.getText().toString());
          put("sms_code", binding.codeEdit.getText().toString());
        }}).post(new Http.ResponseCallBack() {
          @Override
          public void onResponse(String response) {
            try {
              JSONObject jsonObject = new JSONObject(response);
              int code = jsonObject.getInt("code");
              String message = jsonObject.getString("message");
              if (code == Constants.SUCCESS_CODE) {
                SharePreferenceUtil.setParam(Constants.MOBILE,UiUtils.hideUserAccount(binding.phoneNumberEdit.getText().toString()));
                runOnUiThread(()->{
                  finish();
                });
              } else {
                ToastUtils.show(message);
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          @Override
          public void OnFailure(String exception) {

          }
        });
      }
  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.close_button:
        finish();
        break;
      case R.id.request_code_button:
        // 检查极验
        sendSms();
        break;
      case R.id.bind:
        bindOrChangeBind();
        break;
    }
  }
}