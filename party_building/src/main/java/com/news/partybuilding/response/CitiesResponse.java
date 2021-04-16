package com.news.partybuilding.response;

import com.google.gson.annotations.SerializedName;
import com.news.partybuilding.model.CitiesProvinces;

import java.util.List;

public class CitiesResponse {
  @SerializedName("code")
  private int code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private List<CitiesProvinces> citiesProvinces;

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

  public List<CitiesProvinces> getCitiesProvinces() {
    return citiesProvinces;
  }

  public void setCitiesProvinces(List<CitiesProvinces> citiesProvinces) {
    this.citiesProvinces = citiesProvinces;
  }
}
