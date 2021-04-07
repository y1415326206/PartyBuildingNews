package com.news.partybuilding.network;


import com.news.partybuilding.response.HomeBannerResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

  @GET("banner/json")
  Call<HomeBannerResponse> getHomeBanner();

}
