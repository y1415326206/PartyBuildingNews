package com.news.partybuilding.response;

import com.google.gson.annotations.SerializedName;
import com.news.partybuilding.model.FirstLevelCategories;

import java.util.List;

public class FirstLevelCategoriesResponse {
  @SerializedName("code")
  private int code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private List<FirstLevelCategories> firstLevelCategoriesList;

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

  public List<FirstLevelCategories> getFirstLevelCategoriesList() {
    return firstLevelCategoriesList;
  }

  public void setFirstLevelCategoriesList(List<FirstLevelCategories> firstLevelCategoriesList) {
    this.firstLevelCategoriesList = firstLevelCategoriesList;
  }
}
