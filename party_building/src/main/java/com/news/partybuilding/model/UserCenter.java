package com.news.partybuilding.model;

import com.google.gson.annotations.SerializedName;

public class UserCenter {
  @SerializedName("mobile")
  private String mobile;
  @SerializedName("nickname")
  private String nickname;
  @SerializedName("avatar")
  private String avatar;
  @SerializedName("gender")
  private String gender;
  @SerializedName("province_id")
  private String provinceId;
  @SerializedName("city_id")
  private String cityId;
  @SerializedName("county_id")
  private String countyId;
  @SerializedName("party_organization_id")
  private Integer partyOrganizationId;
  @SerializedName("province_name")
  private String provinceName;
  @SerializedName("city_name")
  private String cityName;
  @SerializedName("county_name")
  private String countyName;
  @SerializedName("party_organization_name")
  private String partyOrganizationName;
  @SerializedName("identity_state")
  private String identityState;
  @SerializedName("identity_state_name")
  private String identityStateName;
  @SerializedName("real_name")
  private String realName;
  @SerializedName("id_card_real_name")
  private String idCardRealName;
  @SerializedName("id_card_number")
  private String idCardNumber;
  @SerializedName("id_card_number_not_hide")
  private String idCardNumberNotHide;

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(String provinceId) {
    this.provinceId = provinceId;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public String getCountyId() {
    return countyId;
  }

  public void setCountyId(String countyId) {
    this.countyId = countyId;
  }

  public Integer getPartyOrganizationId() {
    return partyOrganizationId;
  }

  public void setPartyOrganizationId(Integer partyOrganizationId) {
    this.partyOrganizationId = partyOrganizationId;
  }

  public String getProvinceName() {
    return provinceName;
  }

  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getCountyName() {
    return countyName;
  }

  public void setCountyName(String countyName) {
    this.countyName = countyName;
  }

  public String getPartyOrganizationName() {
    return partyOrganizationName;
  }

  public void setPartyOrganizationName(String partyOrganizationName) {
    this.partyOrganizationName = partyOrganizationName;
  }

  public String getIdentityState() {
    return identityState;
  }

  public void setIdentityState(String identityState) {
    this.identityState = identityState;
  }

  public String getIdentityStateName() {
    return identityStateName;
  }

  public void setIdentityStateName(String identityStateName) {
    this.identityStateName = identityStateName;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getIdCardRealName() {
    return idCardRealName;
  }

  public void setIdCardRealName(String idCardRealName) {
    this.idCardRealName = idCardRealName;
  }

  public String getIdCardNumber() {
    return idCardNumber;
  }

  public void setIdCardNumber(String idCardNumber) {
    this.idCardNumber = idCardNumber;
  }

  public String getIdCardNumberNotHide() {
    return idCardNumberNotHide;
  }

  public void setIdCardNumberNotHide(String idCardNumberNotHide) {
    this.idCardNumberNotHide = idCardNumberNotHide;
  }
}
