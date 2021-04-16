package com.news.partybuilding.utils;


import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CacheUtils {

  public static String readJson(Context context, String fileName) {
    try {
      FileInputStream fis = context.openFileInput(fileName);
      InputStreamReader isr = new InputStreamReader(fis);
      BufferedReader bufferedReader = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        sb.append(line);
      }
      return sb.toString();
    } catch (IOException fileNotFound) {
      return null;
    }
  }

  public static boolean createJsonFile(Context context, String fileName, String jsonString){
    String FILENAME = fileName;
    try {
      FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
      if (jsonString != null) {
        fos.write(jsonString.getBytes());
      }
      fos.close();
      return true;
    } catch (IOException fileNotFound) {
      return false;
    }

  }

  public static boolean isFilePresent(Context context, String fileName) {
    String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
    File file = new File(path);
    return file.exists();
  }
}
