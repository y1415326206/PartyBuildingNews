package com.news.partybuilding.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.HomeBannerResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.NetWorkUtils;

public class SimpleCardViewModel extends BaseViewModel {
  public MutableLiveData<HomeBannerResponse> bannerResponse = new MutableLiveData<>();


  // 获取首页轮播图
  public void getHomeBannerData() {
    if (NetWorkUtils.isConnected()){
      loadState.postValue(LoadState.LOADING);
      new Http(Urls.HOME).get(new Http.ResponseCallBack() {
        @Override
        public void onResponse(String response) {
          LogUtils.i("SimpleCardViewModel", response);
          if (!response.isEmpty()){
            final HomeBannerResponse homeBannerResponse = new Gson().fromJson(response, HomeBannerResponse.class);
            loadState.postValue(LoadState.SUCCESS);
            bannerResponse.postValue(homeBannerResponse);
          }else {
            loadState.postValue(LoadState.NO_DATA);
          }
        }

        @Override
        public void OnFailure(String exception) {
          loadState.postValue(LoadState.ERROR);
        }
      });

    }else {
      loadState.postValue(LoadState.NO_NETWORK);
    }

  }




}
