package com.news.partybuilding.response;

import com.google.gson.annotations.SerializedName;
import com.news.partybuilding.model.UserCenter;

public class PersonCenterResponse {
  @SerializedName("code")
  private int code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private UserCenter userCenter;

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

  public UserCenter getUserCenter() {
    return userCenter;
  }

  public void setUserCenter(UserCenter userCenter) {
    this.userCenter = userCenter;
  }
}
