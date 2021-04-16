package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.utils.SharePreferenceUtil;

public class MineViewModel extends BaseViewModel {
  // 用户是否登录
  public MutableLiveData<Boolean> loginOrNot = new MutableLiveData<>(false);
  // 用户昵称
  public MutableLiveData<String> defaultNickName = new MutableLiveData<>();

}
