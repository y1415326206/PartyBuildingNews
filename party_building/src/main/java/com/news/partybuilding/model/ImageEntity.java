package com.news.partybuilding.model;

import java.util.List;

public class ImageEntity {
  public String url;
  public int width;
  public String uri;
  public int height;
  public List<UrlListBeanX> url_list;

  public static class UrlListBeanX {
    /**
     * url : http://p3.pstatp.com/list/300x196/2c23000095ae9f56b15f.webp
     */

    public String url;
  }
}
