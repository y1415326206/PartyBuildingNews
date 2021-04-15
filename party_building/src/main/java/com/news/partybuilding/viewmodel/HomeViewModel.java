package com.news.partybuilding.viewmodel;


import android.annotation.SuppressLint;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.CityByNameResponse;
import com.news.partybuilding.response.FirstLevelCategoriesResponse;
import com.news.partybuilding.response.HomeBannerResponse;
import com.news.partybuilding.response.ProvincesCitiesResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.NetWorkUtils;

import java.util.HashMap;


public class HomeViewModel extends BaseViewModel {

  // 一级类别数据
  public MutableLiveData<FirstLevelCategoriesResponse> firstLevelResponse = new MutableLiveData<>();
  // 城市数据
  public MutableLiveData<ProvincesCitiesResponse> provincesCitiesResponse = new MutableLiveData<>();
  // 根据城市名称返回城市id
  public MutableLiveData<CityByNameResponse> cityByNameResponse = new MutableLiveData<>();

  /**
   * 请求首页一级大类数据
   */
  public void requestFirstLevelArticleCategories() {
 loadState.postValue(LoadState.LOADING);
    if (NetWorkUtils.isConnected()) {
      loadState.postValue(LoadState.LOADING);
      new Http(Urls.FIRST_LEVEL_ARTICLE_CATEGORIES).get(new Http.ResponseCallBack() {
        @Override
        public void onResponse(String response) {
          LogUtils.i("==============", response);
          if (!response.isEmpty()) {
            final FirstLevelCategoriesResponse levelResponse = new Gson().fromJson(response, FirstLevelCategoriesResponse.class);
            LogUtils.i("==============", levelResponse.getFirstLevelCategoriesList().size() + "");
            if (levelResponse.getCode() == Constants.SUCCESS_CODE) {
              loadState.postValue(LoadState.SUCCESS);
              firstLevelResponse.postValue(levelResponse);
            }
          } else {
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

  /**
   * 请求所有城市数据
   */
  public void requestAllCities() {
    if (NetWorkUtils.isConnected()) {
      loadState.postValue(LoadState.LOADING);
      new Http(Urls.PROVINCES_CITIES).get(new Http.ResponseCallBack() {
        @Override
        public void onResponse(String response) {
          if (!response.isEmpty()) {
            final ProvincesCitiesResponse cityResponse = new Gson().fromJson(response, ProvincesCitiesResponse.class);
            if (cityResponse.getCode() == Constants.SUCCESS_CODE) {
              loadState.postValue(LoadState.SUCCESS);
              provincesCitiesResponse.postValue(cityResponse);
            }
          } else {
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


  /**
   * 根据城市名返回城市id
   */
  public void getCityId(String cityName) {
    if (NetWorkUtils.isConnected()) {
      loadState.postValue(LoadState.LOADING);
      new Http(Urls.CITY_BY_NAME, new HashMap<String, String>() {{
        put("city_name", cityName);
      }}).get(new Http.ResponseCallBack() {
        @Override
        public void onResponse(String response) {
          if (!response.isEmpty()) {
            final CityByNameResponse cityResponse = new Gson().fromJson(response, CityByNameResponse.class);
            if (cityResponse.getCode() == Constants.SUCCESS_CODE) {
              loadState.postValue(LoadState.SUCCESS);
              cityByNameResponse.postValue(cityResponse);
            }
          } else {
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
