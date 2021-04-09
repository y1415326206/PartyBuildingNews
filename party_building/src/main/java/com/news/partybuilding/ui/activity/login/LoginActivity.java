package com.news.partybuilding.ui.activity.login;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.databinding.ActivityLoginBinding;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements View.OnClickListener {





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
    setStatusBar();
    initListener();
    onEditTextChangeListener();
  }

  private void initListener(){
    binding.closeButton.setOnClickListener(this);
  }

  private void onEditTextChangeListener(){
    binding.phoneNumberEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!binding.phoneNumberEdit.getText().toString().isEmpty() && !binding.codeEdit.getText().toString().isEmpty()){
          viewModel.isBothEditFull.postValue(true);
        }else {
          viewModel.isBothEditFull.postValue(false);
        }
        // 如果EditText为空 设置isPhoneEditEmpty为false
        viewModel.isPhoneEditEmpty.postValue(binding.phoneNumberEdit.getText().toString().isEmpty());
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
        if (!binding.phoneNumberEdit.getText().toString().isEmpty() && !binding.codeEdit.getText().toString().isEmpty()){
          viewModel.isBothEditFull.postValue(true);
        }else {
          viewModel.isBothEditFull.postValue(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {


      }
    });
  }

  /**
   * 设置状态栏颜色
   */
  private void setStatusBar(){
    ImmersionBar.with(this)
      .statusBarColor(R.color.colorTransparent)     //状态栏颜色，不写默认透明色
      .navigationBarColor(R.color.colorWhite)  //导航栏颜色，不写默认黑色;
      .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
      .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
      .init();
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.close_button:
        finish();
        break;
    }
  }
}