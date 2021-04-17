package com.news.partybuilding.utils;

import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

// 这个Util 目前只用于首页轮播图 需要登录时 拼签名
public class GenerateSignatureUtil {

  // 获取时间戳
  public static String getTimeStamp() {
    return Long.toString(System.currentTimeMillis() / 1000);
  }

  public static String getPublicKey() {
    return SharePreferenceUtil.getString("public_key","");
  }

  // 加入public_key、nonce拼接出queryString格式的字符串，用于生成数字签名
  public static String getSignature(String articleId) {
    String queryString;
    Map<String, String> params = new HashMap<>();
    String publicKey = SharePreferenceUtil.getString("public_key","");
    params.put("article_id", articleId);
    params.put("public_key", publicKey);
//    params.put("private_key",SharedPrefsHelper.getString("private_key"));
    //params.put("lang", SharePreferenceUtil.getString("language", "zh"));
    params.put("nonce", Long.toString(System.currentTimeMillis() / 1000));
    StringBuilder stringBuilder = new StringBuilder();
    // 把params的keys排序后遍历
    for (String key : (new TreeSet<>(params.keySet()))) {
      stringBuilder.append(key).append("=").append(params.get(key)).append("&");
    }
    // 生成的queryString最后一位是&
    queryString = stringBuilder.toString() + "private_key="+ SharePreferenceUtil.getString("private_key","");
    Log.i("queryString", "queryString => " + queryString);
    return new String(Hex.encodeHex(DigestUtils.md5(queryString)));
  }
}
