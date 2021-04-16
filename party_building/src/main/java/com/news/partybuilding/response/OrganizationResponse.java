package com.news.partybuilding.response;

import com.google.gson.annotations.SerializedName;
import com.news.partybuilding.model.OrganizationBean;

import java.util.List;

public class OrganizationResponse {

  @SerializedName("code")
  private Integer code;
  @SerializedName("message")
  private String message;
  @SerializedName("data")
  private List<OrganizationBean> organizationBeanList;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<OrganizationBean> getOrganizationBeanList() {
    return organizationBeanList;
  }

  public void setOrganizationBeanList(List<OrganizationBean> organizationBeanList) {
    this.organizationBeanList = organizationBeanList;
  }
}
