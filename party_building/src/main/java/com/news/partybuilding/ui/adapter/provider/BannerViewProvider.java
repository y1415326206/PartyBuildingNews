package com.news.partybuilding.ui.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drakeet.multitype.ItemViewBinder;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ItemProviderNewsCenterPictureVideoBinding;
import com.news.partybuilding.model.Banner;
import com.news.partybuilding.ui.activity.webview.WebViewActivity;

import org.jetbrains.annotations.NotNull;

public class BannerViewProvider extends ItemViewBinder<Banner, BannerViewProvider.ViewHolder> {
   private Context context;

  public BannerViewProvider(Context context) {
    this.context = context;
  }

  @NotNull
  @Override
  public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
    ItemProviderNewsCenterPictureVideoBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_provider_news_center_picture_video,viewGroup,false);
    return new ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(@NotNull ViewHolder viewHolder, Banner banner) {
    ItemProviderNewsCenterPictureVideoBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
    binding.executePendingBindings();
    binding.setData(banner);
    binding.imageOrVideo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
       Intent intent = new Intent(context, WebViewActivity.class);
       intent.putExtra(Constants.WEB_VIEW_URL, banner.getLinkUrl());
       context.startActivity(intent);
      }
    });
  }

  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
