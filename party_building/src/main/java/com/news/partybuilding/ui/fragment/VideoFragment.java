package com.news.partybuilding.ui.fragment;

import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.guoxiaoxing.phoenix.picker.util.Constant;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.FragmentVideoBinding;
import com.news.partybuilding.model.Article;
import com.news.partybuilding.model.Video;
import com.news.partybuilding.ui.activity.webview.WebViewActivity;
import com.news.partybuilding.utils.GenerateSignatureUtil;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.viewmodel.VideoViewModel;
import com.umeng.commonsdk.debug.I;

public class VideoFragment extends BaseFragment<FragmentVideoBinding, VideoViewModel> {


  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_video;
  }

  @Override
  protected void initAndBindViewModel() {
    mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
    mDataBinding.setVideoViewModel(mViewModel);
  }

  @Override
  protected void init() {
    // 请求视频列表
    mViewModel.requestVideoList();
    videoOnclick();
  }

  @Override
  protected boolean isSupportLoad() {
    return false;
  }


  private void videoOnclick() {
    mViewModel.onClickVideo.observe(this, new Observer<Article>() {
      @Override
      public void onChanged(Article article) {
        LogUtils.i("================",article.getLinkUrl());
        String url = article.getLinkUrl() + "?public_key=" + GenerateSignatureUtil.getPublicKey() + "&nonce=" + GenerateSignatureUtil.getTimeStamp() + "&signature=" + GenerateSignatureUtil.getSignature(String.valueOf(article.getId())) + "&type=app";
        LogUtils.i("================",url);
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(Constants.WEB_VIEW_URL, url);
        startActivity(intent);
      }
    });
  }

}