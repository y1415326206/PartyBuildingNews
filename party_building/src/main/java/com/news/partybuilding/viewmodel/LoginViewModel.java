package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.news.partybuilding.base.BaseViewModel;

public class LoginViewModel extends BaseViewModel {

  public MutableLiveData<Boolean> isPhoneEditEmpty = new MutableLiveData<>(true);
  public MutableLiveData<Boolean> isCodeEditEmpty = new MutableLiveData<>(true);
  public MutableLiveData<Boolean> isBothEditFull = new MutableLiveData<>(false);






}
