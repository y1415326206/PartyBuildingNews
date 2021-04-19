package com.news.partybuilding.config;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseApplication;

/**
 * desc : 全局常量
 */
public final class Constants {

    public static final String APP_NAME = BaseApplication.getContext().getResources().getString(R.string.app_name_english).toLowerCase();
    public static final String BASE_URL = "https://www.wanandroid.com/";
    public static final String DEFAULT_STRING = "nothing";
    public static final String DEFAULT_LANGUAGE = "zh";
    // 是否有授权定位
    public static final String IS_USER_FINE_LOCATION = "is_user_access_fine_location";
    // 用户是否登录
    public static final String IS_LOGIN = "is_login";
    // webView 链接
    public static final String WEB_VIEW_URL = "web_view_url";
    // 网络访问成功的标识
    public static final int SUCCESS_CODE = 20000;
    // 广告标识
    public static final String ADVERTISEMENT = "advertisement";
    // 文章标识
    public static final String ARTICLE = "article";
    // 视频标识
    public static final String VIDEO = "video";
    // banner 图片标识
    public static final String IMAGE = "image";
    // public_key
    public static final String PUBLIC_KEY = "public_key";
    // private_key
    public static final String PRIVATE_KEY = "private_key";
    // 用户默认昵称
    public static final String DEFAULT_NICK_NAME = "default_nick_name";
    // 用户手机号
    public static final String MOBILE = "mobile";
    // 性别标识
    public static final String MAN = "man";

    // 身份证扫描 和 人脸识别 api_key api_secret
    public static final String  API_KEY = "FapKF0hrk84iFr_EwAlYvFD_Cyf5sv6v";
    public static final String  API_SECRET = "5mkpAqAhFB2SS3jiw9-0GGmX-bjFB7ze";
    // 人脸识别的 置信度 阀值
    public static final Float CONTRAST_VALUE = 75f;
    public static final Float THRESHOLDS_1E_5 = 65f;
}
