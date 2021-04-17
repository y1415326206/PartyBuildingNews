package com.news.partybuilding.ui.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.news.partybuilding.R;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ItemProviderNewsAdvertisementBinding;
import com.news.partybuilding.databinding.ItemProviderNewsWithPictureBinding;
import com.news.partybuilding.model.Article;
import com.news.partybuilding.ui.activity.webview.WebViewActivity;

import org.jetbrains.annotations.NotNull;

public class AdvertisementViewProvider extends ItemViewBinder<Article, AdvertisementViewProvider.ViewHolder> {

  private Context context;

  public AdvertisementViewProvider(Context context) {
    this.context = context;
  }

  @NotNull
  @Override
  public AdvertisementViewProvider.ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
    ItemProviderNewsAdvertisementBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_provider_news_advertisement, viewGroup, false);
    return new AdvertisementViewProvider.ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(@NotNull AdvertisementViewProvider.ViewHolder viewHolder, Article article) {
    ItemProviderNewsAdvertisementBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
    binding.executePendingBindings();
    binding.setData(article);
    binding.articleOrAdvertisement.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.WEB_VIEW_URL, article.getLinkUrl());
        context.startActivity(intent);
      }
    });
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
