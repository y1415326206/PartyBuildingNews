package com.news.partybuilding.model;

public class EmptyValue {
  // 标题
  public static final int TYPE_MORE_TITLE = 1;

  // 分割线
  public static final int TYPE_LINE = 2;

  public int type;
  private String titleMore;

  public EmptyValue(int type, String title) {
    this.type = type;
    this.titleMore = title;
  }

  public String getTitleMore() {
    return titleMore;
  }

  public void setTitleMore(String titleMore) {
    this.titleMore = titleMore;
  }
}
