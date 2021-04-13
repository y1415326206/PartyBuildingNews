package com.news.partybuilding.response;

import com.google.gson.annotations.SerializedName;
import com.news.partybuilding.model.Video;

public class VideoListResponse {
  @SerializedName("code")
  private int code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private Video video;

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

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }
}
