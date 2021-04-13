package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Video {

  @SerializedName("current_page")
  private int currentPage;
  @SerializedName("total_page")
  private int totalPage;
  @SerializedName("total_num")
  private int totalNum;
  @SerializedName("page_size")
  private int pageSize;
  @SerializedName("articles")
  private List<Article> articles;

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(int totalPage) {
    this.totalPage = totalPage;
  }

  public int getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(int totalNum) {
    this.totalNum = totalNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }
}
