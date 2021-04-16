package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.news.partybuilding.base.BaseViewModel;

public class AccountSettingViewModel extends BaseViewModel {

  // 手机号是否为空
  public MutableLiveData<Boolean> mobileEmpty = new MutableLiveData<>();
  // 手机号
  public MutableLiveData<String> mobile = new MutableLiveData<>();

}
