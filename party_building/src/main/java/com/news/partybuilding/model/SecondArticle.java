package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

public class SecondArticle {
  @SerializedName("id")
  private int id;
  @SerializedName("title")
  private String title;
  @SerializedName("type")
  private String type;
  @SerializedName("cover_picture_url")
  private String coverPictureUrl;
  @SerializedName("author_name")
  private String authorName;
  @SerializedName("reading_volume")
  private String readingVolume;
  @SerializedName("link_url")
  private String linkUrl;
  @SerializedName("created_at")
  private String createdAt;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCoverPictureUrl() {
    return coverPictureUrl;
  }

  public void setCoverPictureUrl(String coverPictureUrl) {
    this.coverPictureUrl = coverPictureUrl;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public String getReadingVolume() {
    return readingVolume;
  }

  public void setReadingVolume(String readingVolume) {
    this.readingVolume = readingVolume;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }
}
