package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.model.UserCenter;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.PersonCenterResponse;
import com.news.partybuilding.utils.NetWorkUtils;
import java.util.HashMap;

public class IdentifyViewModel extends BaseViewModel {

  public MutableLiveData<PersonCenterResponse> personalCenterResponse = new MutableLiveData<>();
  // 是否认证成功
  public MutableLiveData<Boolean> isIdentifySuccess = new MutableLiveData<>(false);
  // 个人中心数据
  public MutableLiveData<UserCenter> userCenter = new MutableLiveData<>();

  // 请求个人中心接口
  public void requestPersonCenter(String publicKey){
    if (NetWorkUtils.isConnected()) {
      loadState.postValue(LoadState.LOADING);
      new Http(Urls.USER_CENTER, new HashMap<String, String>(){{
        put("public_key",publicKey);
      }}).get(new Http.ResponseCallBack() {
        @Override
        public void onResponse(String response) {
          if (response != null){
            loadState.postValue(LoadState.SUCCESS);
            PersonCenterResponse response1 = new Gson().fromJson(response,PersonCenterResponse.class);
            personalCenterResponse.postValue(response1);
          }else {
            loadState.postValue(LoadState.NO_DATA);
          }
        }
        @Override
        public void OnFailure(String exception) {
          loadState.postValue(LoadState.ERROR);
        }
      });
    } else {
      loadState.postValue(LoadState.NO_NETWORK);
    }

  }




}
