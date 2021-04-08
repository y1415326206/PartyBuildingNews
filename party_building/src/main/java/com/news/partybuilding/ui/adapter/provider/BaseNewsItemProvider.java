package com.news.partybuilding.ui.adapter.provider;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.news.partybuilding.model.News;

/**
 * 设置新闻列表公共部分
 */
public abstract class BaseNewsItemProvider extends BaseItemProvider<News, BaseViewHolder> {

  private String mChannelCode;

  public BaseNewsItemProvider(String channelCode) {
    mChannelCode = channelCode;
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, News news, int i) {
    if (TextUtils.isEmpty(news.title)) {
      //如果没有标题，则直接跳过
      return;
    }

   setData(baseViewHolder,news);
  }

  protected abstract void setData(BaseViewHolder helper, News news);


}
