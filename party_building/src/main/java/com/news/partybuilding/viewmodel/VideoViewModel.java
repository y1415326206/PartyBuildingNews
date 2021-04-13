package com.news.partybuilding.viewmodel;

import android.os.Handler;
import android.os.Looper;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.news.partybuilding.BR;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseViewModel;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.event.SingleLiveEvent;
import com.news.partybuilding.listener.VideoOnClickListener;
import com.news.partybuilding.model.Article;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.VideoListResponse;
import com.news.partybuilding.utils.NetWorkUtils;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class VideoViewModel extends BaseViewModel {

  public MutableLiveData<VideoListResponse> videoListResponse = new MutableLiveData<>();

  // 点击事件
  public final SingleLiveEvent<Article> onClickVideo = new SingleLiveEvent<>();

  public VideoOnClickListener videoOnClickListener = new VideoOnClickListener() {
    @Override
    public void onVideoClick(Article article) {
     onClickVideo.postValue(article);
    }
  };

  // 如果变量要在布局文件中使用的话要用Observable<>来包裹才能实现观察
  public final ObservableList<Article> items = new ObservableArrayList<>();
  // 这个地方要用Object因为类型不一样  tvShow是TVShow类型 点击事件是TVShowListener类型 如果没有点击事件可以将Object改为TVShow
  public final ItemBinding<Object> itemBinding = ItemBinding.of(BR.article, R.layout.item_video)
    .bindExtra(BR.listener, videoOnClickListener);


  // 请求视频列表
  public void requestVideoList(){
      if (NetWorkUtils.isConnected()) {
        loadState.postValue(LoadState.LOADING);
        new Http(Urls.VIDEO_LISTS).get(new Http.ResponseCallBack() {
          @Override
          public void onResponse(String response) {
            if (!response.isEmpty()) {
              final VideoListResponse videoResponse = new Gson().fromJson(response, VideoListResponse.class);
              if (videoResponse.getCode() == Constants.SUCCESS_CODE) {
                loadState.postValue(LoadState.SUCCESS);
                videoListResponse.postValue(videoResponse);
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                  @Override
                  public void run() {
                    //已在主线程中
                    items.addAll(videoResponse.getVideo().getArticles());
                  }
                });
              }
            } else {
              loadState.postValue(LoadState.NO_DATA);
            }
          }

          @Override
          public void OnFailure(String exception) {
            loadState.postValue(LoadState.ERROR);
          }
        });
      } else {
        loadState.postValue(LoadState.NO_NETWORK);
      }
    }

}
