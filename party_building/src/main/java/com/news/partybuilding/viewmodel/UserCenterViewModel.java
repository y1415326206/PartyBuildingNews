package com.news.partybuilding.viewmodel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.cretin.tools.cityselect.CityResponse;
import com.google.gson.Gson;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.model.CitiesProvinces;
import com.news.partybuilding.model.UserCenter;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.CitiesResponse;
import com.news.partybuilding.response.PersonCenterResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;

import java.util.HashMap;

public class UserCenterViewModel extends BaseViewModel {


  public MutableLiveData<PersonCenterResponse> personalCenterResponse = new MutableLiveData<>();
  public MutableLiveData<UserCenter> userCenter = new MutableLiveData<>();
  public MutableLiveData<CitiesResponse> citiesResponse = new MutableLiveData<>();

  // 性别
  public MutableLiveData<String> gender = new MutableLiveData<>();

  // 所在地
  public MutableLiveData<String> provinceCities = new MutableLiveData<>();

  // 请求个人中心接口
  public void requestPersonCenter(String publicKey){
    new Http(Urls.USER_CENTER, new HashMap<String, String>(){{
      put("public_key",publicKey);
    }}).get(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        if (response != null){
          PersonCenterResponse response1 = new Gson().fromJson(response,PersonCenterResponse.class);
          personalCenterResponse.postValue(response1);
        }
      }
      @Override
      public void OnFailure(String exception) {

      }
    });
  }


}
