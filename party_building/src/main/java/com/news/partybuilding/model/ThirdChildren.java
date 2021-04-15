package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ThirdChildren {
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;
  @SerializedName("category_level")
  private int categoryLevel;
  @SerializedName("articles")
  private List<Article> articles;

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

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }
}
