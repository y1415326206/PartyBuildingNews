package com.news.partybuilding.ui.adapter.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.news.partybuilding.R;
import com.news.partybuilding.databinding.ItemProviderNewsWithPictureBinding;
import com.news.partybuilding.model.Article;

import org.jetbrains.annotations.NotNull;

public class FirstArticleViewProvider extends ItemViewBinder<Article, FirstArticleViewProvider.ViewHolder> {


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
  }

  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
