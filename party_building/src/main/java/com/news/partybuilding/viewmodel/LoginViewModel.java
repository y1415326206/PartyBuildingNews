package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.news.partybuilding.base.BaseViewModel;

public class LoginViewModel extends BaseViewModel {

  // 手机号码输入框是否为空
  public MutableLiveData<Boolean> isPhoneEditEmpty = new MutableLiveData<>(true);
  // 验证码输入框是否为空
  public MutableLiveData<Boolean> isCodeEditEmpty = new MutableLiveData<>(true);
  // 手机号码输入框 + 验证码输入框 是否都有值
  public MutableLiveData<Boolean> isBothEditFull = new MutableLiveData<>(false);






}
