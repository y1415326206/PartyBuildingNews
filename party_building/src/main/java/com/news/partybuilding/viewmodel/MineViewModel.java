package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.news.partybuilding.base.BaseViewModel;

public class MineViewModel extends BaseViewModel {
  // 用户是否登录
  public MutableLiveData<Boolean> loginOrNot = new MutableLiveData<>(false);
}
