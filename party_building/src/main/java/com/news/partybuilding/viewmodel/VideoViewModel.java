package com.news.partybuilding.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;

import com.news.partybuilding.BR;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.event.SingleLiveEvent;
import com.news.partybuilding.listener.VideoOnClickListener;
import com.news.partybuilding.model.Video;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class VideoViewModel extends BaseViewModel {


  // 点击事件
  public final SingleLiveEvent<Video> onClickVideo = new SingleLiveEvent<>();

  public VideoOnClickListener videoOnClickListener = new VideoOnClickListener() {
    @Override
    public void onVideoClick(Video video) {
     onClickVideo.postValue(video);
    }
  };

  // 如果变量要在布局文件中使用的话要用Observable<>来包裹才能实现观察
  public final ObservableList<Video> items = new ObservableArrayList<>();
  // 这个地方要用Object因为类型不一样  tvShow是TVShow类型 点击事件是TVShowListener类型 如果没有点击事件可以将Object改为TVShow
  public final ItemBinding<Object> itemBinding = ItemBinding.of(BR.video, R.layout.item_video)
    .bindExtra(BR.listener, videoOnClickListener);



}
