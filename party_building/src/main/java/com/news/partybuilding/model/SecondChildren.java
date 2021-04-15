package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecondChildren {
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;
  @SerializedName("category_level")
  private int categoryLevel;
  @SerializedName("checked_children_id")
  private int checkedChildrenId;
  @SerializedName("articles")
  private List<Article> articles;
  @SerializedName("childrens")
  private List<ThirdChildren> childrens;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCategoryLevel() {
    return categoryLevel;
  }

  public void setCategoryLevel(int categoryLevel) {
    this.categoryLevel = categoryLevel;
  }

  public int getCheckedChildrenId() {
    return checkedChildrenId;
  }

  public void setCheckedChildrenId(int checkedChildrenId) {
    this.checkedChildrenId = checkedChildrenId;
  }

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }

  public List<ThirdChildren> getChildrens() {
    return childrens;
  }

  public void setChildrens(List<ThirdChildren> childrens) {
    this.childrens = childrens;
  }
}
