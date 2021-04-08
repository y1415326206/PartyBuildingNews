package com.news.partybuilding.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.news.partybuilding.base.BaseApplication;

import java.util.Map;

public class SharePreferenceUtil {
  /**
   *  
   *      * 保存在手机里面的文件名 
   *      
   */
  private static final String FILE_NAME = "Preference";

  /**
   *  
   *      * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 
   *      * @param context 
   *      * @param key 
   *      * @param object  
   *      
   */
  public static void setParam(String key, Object object) {

    String type = object.getClass().getSimpleName();
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    if ("String".equals(type)) {
      editor.putString(key, (String) object);
    } else if ("Integer".equals(type)) {
      editor.putInt(key, (Integer) object);
    } else if ("Boolean".equals(type)) {
      editor.putBoolean(key, (Boolean) object);
    } else if ("Float".equals(type)) {
      editor.putFloat(key, (Float) object);
    } else if ("Long".equals(type)) {
      editor.putLong(key, (Long) object);
    }
    editor.apply();
  }

  /**
   *  
   *      * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值 
   *      * @param context 
   *      * @param key 
   *      * @param defaultObject 
   *      * @return 
   *      
   */
  public static Object getParam(String key, Object defaultObject) {
    String type = defaultObject.getClass().getSimpleName();
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    if ("String".equals(type)) {
      return sp.getString(key, (String) defaultObject);
    } else if ("Integer".equals(type)) {
      return sp.getInt(key, (Integer) defaultObject);
    } else if ("Boolean".equals(type)) {
      return sp.getBoolean(key, (Boolean) defaultObject);
    } else if ("Float".equals(type)) {
      return sp.getFloat(key, (Float) defaultObject);
    } else if ("Long".equals(type)) {
      return sp.getLong(key, (Long) defaultObject);
    }
    return null;
  }


  public static boolean getBoolean(String key, boolean defaultValue){
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    return sp.getBoolean(key, defaultValue);
  }

  public static String getString(String key, String defaultValue){
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    return sp.getString(key, defaultValue);
  }

  /**
   * 是否登录
   */
  public static boolean isLogin() {
    return getBoolean("is_login", false);
  }

  /**
   * 移除某个key值已经对应的值
   *
   * @param key
   */
  public static boolean remove(String key) {
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(
      FILE_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.remove(key);
    return editor.commit();
  }

  /**
   * 清除所有数据
   *
   * @return 是否成功
   */
  public static boolean clear() {
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(
      FILE_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.clear();
    return editor.commit();
  }

  /**
   * 查询某个key是否已经存在
   *
   * @param key
   * @return 是否存在
   */
  public static boolean contains(String key) {
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(
      FILE_NAME, Context.MODE_PRIVATE);
    return sp.contains(key);
  }

  /**
   * 返回所有的键值对
   *
   * @return Map<String, ?>
   */
  public static Map<String, ?> getAll() {
    SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(
      FILE_NAME, Context.MODE_PRIVATE);
    return sp.getAll();
  }

}
