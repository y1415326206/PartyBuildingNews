package com.news.partybuilding.response;

import com.google.gson.annotations.SerializedName;
import com.news.partybuilding.model.Home;

public class HomeResponse {
  @SerializedName("code")
  private int code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private Home homeData;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Home getHomeData() {
    return homeData;
  }

  public void setHomeData(Home homeData) {
    this.homeData = homeData;
  }
}
