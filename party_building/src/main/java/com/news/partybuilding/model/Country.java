package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

// 区县类
public class Country {
  @SerializedName("value")
  private int value;
  @SerializedName("label")
  private String label;
  @SerializedName("is_hot")
  private boolean isHot;

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public boolean isHot() {
    return isHot;
  }

  public void setHot(boolean hot) {
    isHot = hot;
  }
}
