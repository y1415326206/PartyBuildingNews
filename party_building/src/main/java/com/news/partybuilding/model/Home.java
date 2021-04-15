package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Home {
  @SerializedName("category_level")
  private int categoryLevel;
  @SerializedName("is_show_banner")
  private boolean isShowBanner;
  @SerializedName("banner_data")
  private Banner banner;
  @SerializedName("articles")
  private List<Article> articles;
  @SerializedName("childrens")
  private List<SecondChildren> secondChildrenList;

  public int getCategoryLevel() {
    return categoryLevel;
  }

  public void setCategoryLevel(int categoryLevel) {
    this.categoryLevel = categoryLevel;
  }

  public boolean isShowBanner() {
    return isShowBanner;
  }

  public void setShowBanner(boolean showBanner) {
    isShowBanner = showBanner;
  }

  public Banner getBanner() {
    return banner;
  }

  public void setBanner(Banner banner) {
    this.banner = banner;
  }

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }

  public List<SecondChildren> getSecondChildrenList() {
    return secondChildrenList;
  }

  public void setSecondChildrenList(List<SecondChildren> secondChildrenList) {
    this.secondChildrenList = secondChildrenList;
  }
}
