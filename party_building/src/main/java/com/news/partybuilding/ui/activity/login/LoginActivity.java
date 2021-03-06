package com.news.partybuilding.ui.activity.login;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.geetest.sdk.GT3GeetestUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivityLoginBinding;
import com.news.partybuilding.model.GT3Bean;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.utils.GeetestUtils;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.viewmodel.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements View.OnClickListener {

  private GT3GeetestUtils gt3GeetestUtils;


  @Override
  protected int getLayoutResId() {
    return R.layout.activity_login;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    binding.setData(viewModel);
  }

  @Override
  protected void init() {
    gt3GeetestUtils = new GT3GeetestUtils(this);
    setStatusBar();
    initListener();
    onEditTextChangeListener();
  }

  private void initListener() {
    binding.closeButton.setOnClickListener(this);
    binding.requestCodeButton.setOnClickListener(this);
    binding.login.setOnClickListener(this);
  }

  private void onEditTextChangeListener() {
    binding.phoneNumberEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // ???????????????????????????????????????
        if (!binding.phoneNumberEdit.getText().toString().isEmpty() && !binding.codeEdit.getText().toString().isEmpty()) {
          viewModel.isBothEditFull.postValue(true);
        } else {
          viewModel.isBothEditFull.postValue(false);
        }
        if (!binding.requestCodeButton.getText().toString().contains("s")) {
          // ??????EditText?????? ??????isPhoneEditEmpty???true
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

  /**
   * ?????????????????????
   */
  private void setStatusBar() {
    ImmersionBar.with(this)
      .statusBarColor(R.color.colorTransparent)     //???????????????????????????????????????
      .navigationBarColor(R.color.colorWhite)  //????????????????????????????????????;
      .statusBarDarkFont(true)   //????????????????????????????????????????????????
      .navigationBarDarkIcon(true) //????????????????????????????????????????????????
      .init();
  }

  // ????????????
  private void checkGeeTest() {
    GeetestUtils.initGt3(this, gt3GeetestUtils, new GeetestUtils.OnGT3Listener() {
      @Override
      public void onSuccess(String gt3Result) {
        //Log.i("log message", "PasswordLoginActivity------onSuccess" + gt3Result);
        gt3GeetestUtils.dismissGeetestDialog();
        gt3GeetestUtils.destory();
        GT3Bean gt3Bean = new Gson().fromJson(gt3Result, GT3Bean.class);
        loginWithSms(gt3Bean);
      }

      @Override
      public void onFail(String gt3Result) {
        //Log.i("log message", "PasswordLoginActivity------onFail" + gt3Result);
        //gt3GeetestUtils.dismissGeetestDialog();
        gt3GeetestUtils.destory();
      }
    });
  }

  // ?????????????????????
  private void checkGeeTestWhenSendCode() {
    GeetestUtils.initGt3(this, gt3GeetestUtils, new GeetestUtils.OnGT3Listener() {
      @Override
      public void onSuccess(String gt3Result) {
        //Log.i("log message", "PasswordLoginActivity------onSuccess" + gt3Result);
        gt3GeetestUtils.dismissGeetestDialog();
        GT3Bean gt3Bean = new Gson().fromJson(gt3Result, GT3Bean.class);
        sendSms(gt3Bean);
      }

      @Override
      public void onFail(String gt3Result) {
        //Log.i("log message", "PasswordLoginActivity------onFail" + gt3Result);
        gt3GeetestUtils.dismissGeetestDialog();
      }
    });
  }

  // ???????????????
  private void sendSms(GT3Bean gt3Bean) {
    new Http(Urls.SEND_SIGN_SMS_TOKEN_TO_MEMBER, new HashMap<String, String>() {{
      put("mobile", binding.phoneNumberEdit.getText().toString());
      put("geetest_challenge", gt3Bean.geetest_challenge);
      put("geetest_seccode", gt3Bean.geetest_seccode);
      put("geetest_validate", gt3Bean.geetest_validate);
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
              // ???????????????
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


  // ??????????????????????????????
  private void loginWithSms(GT3Bean gt3Bean) {
    new Http(Urls.MOBILE_SIGN_IN, new HashMap<String, String>() {{
      put("mobile", binding.phoneNumberEdit.getText().toString());
      put("sms_code", binding.codeEdit.getText().toString());
      put("geetest_challenge", gt3Bean.geetest_challenge);
      put("geetest_seccode", gt3Bean.geetest_seccode);
      put("geetest_validate", gt3Bean.geetest_validate);
    }}).post(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          int code = jsonObject.getInt("code");
          String message = jsonObject.getString("message");
          if (code == Constants.SUCCESS_CODE) {
            JSONObject data = jsonObject.getJSONObject("data");
            SharePreferenceUtil.setParam(Constants.PUBLIC_KEY, data.getString("public_key"));
            SharePreferenceUtil.setParam(Constants.PRIVATE_KEY, data.getString("private_key"));
            SharePreferenceUtil.setParam(Constants.DEFAULT_NICK_NAME, binding.phoneNumberEdit.getText().toString());
            SharePreferenceUtil.setParam(Constants.IS_LOGIN, true);
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
        // ????????????
        checkGeeTestWhenSendCode();
        break;
      case R.id.login:
        checkGeeTest();
        break;
    }
  }
}