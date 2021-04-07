package com.news.partybuilding.viewmodel;


import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.network.ApiClient;
import com.news.partybuilding.network.ApiService;
import com.news.partybuilding.response.HomeBannerResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.NetWorkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeViewModel extends BaseViewModel {

  public MutableLiveData<HomeBannerResponse> bannerResponse = new MutableLiveData<>();

  public void getHomeBannerData() {
    if (NetWorkUtils.isConnected()){
      loadState.postValue(LoadState.LOADING);
      ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
      apiService.getHomeBanner().enqueue(new Callback<HomeBannerResponse>() {
        @Override
        public void onResponse(@NonNull Call<HomeBannerResponse> call, @NonNull Response<HomeBannerResponse> response) {
          LogUtils.i("============", response.body().toString());
          if (response.body() != null){
            loadState.postValue(LoadState.SUCCESS);
            bannerResponse.postValue(response.body());
          }else {
            loadState.postValue(LoadState.NO_DATA);
          }
        }

        @Override
        public void onFailure(@NonNull Call<HomeBannerResponse> call, @NonNull Throwable t) {
          loadState.postValue(LoadState.ERROR);
        }
      });
    }else {
      loadState.postValue(LoadState.NO_NETWORK);
    }



  }

  @SuppressLint("NonConstantResourceId")
  public void handleOnclick(View view) {
    switch (view.getId()) {

    }
  }
}
