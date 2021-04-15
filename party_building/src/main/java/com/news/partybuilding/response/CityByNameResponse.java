package com.news.partybuilding.response;

import com.google.gson.annotations.SerializedName;
import com.news.partybuilding.model.CityId;

public class CityByNameResponse {
  @SerializedName("code")
  private int code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private CityId cityId;

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

  public CityId getCityId() {
    return cityId;
  }

  public void setCityId(CityId cityId) {
    this.cityId = cityId;
  }
}
