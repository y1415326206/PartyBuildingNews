package com.news.partybuilding.utils;


import java.util.List;

/**
 * 判断一个List是否为空
 */
public class ListUtils {
  public static boolean isEmpty(List list){
    if (list == null){
      return true;
    }
    return list.size() == 0;
  }
}
