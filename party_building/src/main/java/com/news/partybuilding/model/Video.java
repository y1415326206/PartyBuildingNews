package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

public class Video {

  @SerializedName("video_url")
  private String videoUrl;
  @SerializedName("title")
  private String title;
  @SerializedName("video_views")
  private String videoViews;
  @SerializedName("time")
  private String time;
  @SerializedName("url")
  private String url;

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getVideoViews() {
    return videoViews;
  }

  public void setVideoViews(String videoViews) {
    this.videoViews = videoViews;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
