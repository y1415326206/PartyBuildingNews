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
import com.news.partybuilding.databinding.ItemProviderNewsWithPictureBinding;
import com.news.partybuilding.model.Article;
import com.news.partybuilding.ui.activity.webview.WebViewActivity;

import org.jetbrains.annotations.NotNull;

public class FirstArticleViewProvider extends ItemViewBinder<Article, FirstArticleViewProvider.ViewHolder> {

  private Context context;

  public FirstArticleViewProvider(Context context) {
    this.context = context;
  }

  @NotNull
  @Override
  public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
    ItemProviderNewsWithPictureBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_provider_news_with_picture,viewGroup,false);
    return new FirstArticleViewProvider.ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(@NotNull ViewHolder viewHolder, Article article) {
    ItemProviderNewsWithPictureBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
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

  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
