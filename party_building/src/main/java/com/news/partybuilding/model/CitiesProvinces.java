package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesProvinces {
  @SerializedName("value")
  private int value;
  @SerializedName("label")
  private String label;

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
}
