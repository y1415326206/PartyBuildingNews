package com.news.partybuilding.model;

import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesProvinces implements IPickerViewData {
  @SerializedName("value")
  private int value;
  @SerializedName("label")
  private String label;
  @SerializedName("is_hot")
  private boolean isHot;
  @SerializedName("children")
  private List<CitiesProvincesChildren> citiesProvincesChildrenList;

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

  public List<CitiesProvincesChildren> getCitiesProvincesChildrenList() {
    return citiesProvincesChildrenList;
  }

  public void setCitiesProvincesChildrenList(List<CitiesProvincesChildren> citiesProvincesChildrenList) {
    this.citiesProvincesChildrenList = citiesProvincesChildrenList;
  }

  @Override
  public String getPickerViewText() {
    return this.label;
  }
}
