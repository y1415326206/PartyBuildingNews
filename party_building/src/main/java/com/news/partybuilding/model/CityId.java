package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

public class CityId {
  @SerializedName("id")
  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
