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
import com.news.partybuilding.model.Video;
import com.news.partybuilding.ui.activity.webview.WebViewActivity;
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
   setVideo();
   videoOnclick();
  }

  @Override
  protected boolean isSupportLoad() {
    return false;
  }

  private void setVideo() {
    Video video = new Video();
    video.setTitle("hhhhhhhh");
    video.setVideoViews("33万次播放");
    video.setTime("2023-3-30");
    video.setUrl("https://www.baidu.com/");

    Video video1 = new Video();
    video1.setTitle("hhhhhhhh");
    video1.setVideoViews("33万次播放");
    video1.setTime("2023-3-30");
    video1.setUrl("https://sina.cn/");

    Video video2 = new Video();
    video2.setTitle("hhhhhhhh");
    video2.setVideoViews("33万次播放");
    video2.setTime("2023-3-30");
    video2.setUrl("https://www.qq.com/");

    mViewModel.items.add(video);
    mViewModel.items.add(video1);
    mViewModel.items.add(video2);
  }

  private void videoOnclick(){
    mViewModel.onClickVideo.observe(this, new Observer<Video>() {
      @Override
      public void onChanged(Video video) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(Constants.WEB_VIEW_URL,video.getUrl());
        startActivity(intent);
      }
    });
  }

}