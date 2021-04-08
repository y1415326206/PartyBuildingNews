package com.news.partybuilding.viewmodel;


import android.annotation.SuppressLint;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.HomeBannerResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.NetWorkUtils;


public class HomeViewModel extends BaseViewModel {




  @SuppressLint("NonConstantResourceId")
  public void handleOnclick(View view) {
    switch (view.getId()) {

    }
  }
}
