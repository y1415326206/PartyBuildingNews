package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesProvincesChildren {
  @SerializedName("value")
  private int value;
  @SerializedName("label")
  private String label;
  @SerializedName("is_hot")
  private boolean isHot;
  @SerializedName("children")
  private List<Country> countryList;

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

  public List<Country> getCountryList() {
    return countryList;
  }

  public void setCountryList(List<Country> countryList) {
    this.countryList = countryList;
  }
}
