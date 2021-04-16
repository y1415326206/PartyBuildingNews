package com.news.partybuilding.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <读取Json文件的工具类>
 *
 * @author: 小嵩
 * @date: 2017/3/16 16:22

 */

public class GetJsonDataUtil {




  //字符串、json 写入文件
  public static void writeStringToFile(String json, String filePath) {
    File txt = new File(filePath);
    if (!txt.exists()) {
      try {
        txt.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    byte[] bytes = json.getBytes(); //新加的
    int b = json.length(); //改
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(txt);
      fos.write(bytes);
      fos.flush();
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public String getJson(Context context, String fileName) {

    StringBuilder stringBuilder = new StringBuilder();
    try {
      AssetManager assetManager = context.getAssets();
      BufferedReader bf = new BufferedReader(new InputStreamReader(
        assetManager.open(fileName)));
      String line;
      while ((line = bf.readLine()) != null) {
        stringBuilder.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }
}

