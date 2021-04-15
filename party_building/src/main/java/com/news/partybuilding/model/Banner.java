package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

public class Banner {
  @SerializedName("type")
  private String type;
  @SerializedName("banner_type")
  private String bannerType;
  @SerializedName("link_url")
  private String linkUrl;
  @SerializedName("cover_picture_url")
  private String coverPictureUrl;
  @SerializedName("img_height_and_width_ratio")
  private String imgHeightAndWidthRatio;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBannerType() {
    return bannerType;
  }

  public void setBannerType(String bannerType) {
    this.bannerType = bannerType;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public String getCoverPictureUrl() {
    return coverPictureUrl;
  }

  public void setCoverPictureUrl(String coverPictureUrl) {
    this.coverPictureUrl = coverPictureUrl;
  }

  public String getImgHeightAndWidthRatio() {
    return imgHeightAndWidthRatio;
  }

  public void setImgHeightAndWidthRatio(String imgHeightAndWidthRatio) {
    this.imgHeightAndWidthRatio = imgHeightAndWidthRatio;
  }
}
