package com.news.partybuilding.network;

import com.news.partybuilding.BuildConfig;

public final class Urls {
  // HTTP 服务器和前缀
  static final String HTTP_SERVER_AND_URL_PREFIX = BuildConfig.BASE_URL;
  // 首页
  public static final String HOME = "home";
  // 视频列表
  public static final String VIDEO_LISTS = "video_lists";
  // 查看更多文章
  public static final String VIEW_MORE_ARTICLES = "view_more_articles";
  // 文章/视频详情
  public static final String ARTICLE_DETAILS = "article_details";
  // 用户收藏/取消文章
  public static final String FAVORITES_ARTICLE = "favorites_article";
  // 根据搜索的关键字返回对应的文章
  public static final String ARTICLES_BY_KEYWORD = "articles_by_keyword";
  // 通过城市名称返回城市信息
  public static final String CITY_BY_NAME = "city_by_name";
  // 热门搜索
  public static final String POPULAR_SEARCHES = "popular_searches";
  // 所有文章一级大类
  public static final String FIRST_LEVEL_ARTICLE_CATEGORIES = "first_level_article_categories";
  // 省市数据
  public static final String PROVINCES_CITIES = "cities";
  // 个人中心
  public static final String USER_CENTER = "user_center";
  // 修改用户信息
  public static final String EDIT_USER_INFORMATION = "edit_user_information";
  // 我的收藏
  public static final String MY_FAVORITES = "my_favorites";
  // 历史记录
  public static final String MY_HISTORY_RECORDS = "my_history_records";
  // 站内信列表
  public static final String GET_STATION_LETTER_LIST = "get_station_letter_list";
  // 站内信详情
  public static final String GET_STATION_LETTER_DETAIL = "get_station_letter_detail";
  // 站内信全部标记已读
  public static final String SET_ALL_READ = "set_all_read";
  // 上传身份证图片
  public static final String UPLOAD_ID_CARD_FILES = "upload_id_card_files";
  // 上传身份证正面背面 民族信息 性别 等
  public static final String UPLOAD_ID_CARD_INFORMATION = "upload_id_card_information";
  // 扫描完身份证后 ，进行扫描人脸 成功后调的接口
  public static final String AFTER_SCAN_FACE = "after_scan_face";
  // 登录发送手机验证码接口
  public static final String SEND_SIGN_SMS_TOKEN_TO_MEMBER = "send_sign_sms_token_to_member";
  // 手机验证码登录/注册
  public static final String MOBILE_SIGN_IN = "mobile_sign_in";
  // 换绑手机
  public static final String BIND_MOBILE = "bind_mobile";
  // 微信授权登录
  public static final String WECHAT_SIGN_IN_BY_APP = "wechat_sign_in_by_app";
  // 绑定或换绑手机发送验证码
  public static final String SEND_BIND_MOBILE_SMS_TOKEN_TO_MEMBER = "send_bind_mobile_sms_token_to_member";
  //新版极验 使用post请求
  public static final String GEETEST = "https://" + BuildConfig.HOSTNAME_URL + "/api/v2/gee_test/register";
}
