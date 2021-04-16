package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.PersonCenterResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;

import java.util.HashMap;

public class SettingViewModel extends BaseViewModel {

public MutableLiveData<PersonCenterResponse> personalCenterResponse = new MutableLiveData<>();




  // 请求个人中心接口
  public void requestPersonCenter(String publicKey){
    new Http(Urls.USER_CENTER, new HashMap<String, String>(){{
      put("public_key",publicKey);
    }}).get(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        if (response != null){
          PersonCenterResponse response1 = new Gson().fromJson(response,PersonCenterResponse.class);
          SharePreferenceUtil.setParam(Constants.MOBILE,response1.getUserCenter().getMobile());
        }
      }
      @Override
      public void OnFailure(String exception) {

      }
    });
  }
}
