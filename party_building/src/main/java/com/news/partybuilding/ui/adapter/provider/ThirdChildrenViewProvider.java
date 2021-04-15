package com.news.partybuilding.ui.adapter.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.news.partybuilding.R;
import com.news.partybuilding.databinding.ItemProviderNewsSecondChildrenCategoriesBinding;
import com.news.partybuilding.databinding.ItemProviderNewsThirdChildrenCategoriesBinding;
import com.news.partybuilding.model.SecondChildren;
import com.news.partybuilding.model.ThirdChildren;

import org.jetbrains.annotations.NotNull;

public class ThirdChildrenViewProvider extends ItemViewBinder<ThirdChildren, ThirdChildrenViewProvider.ViewHolder> {
  @NotNull
  @Override
  public ThirdChildrenViewProvider.ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
    ItemProviderNewsThirdChildrenCategoriesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_provider_news_third_children_categories,viewGroup,false);
    return new ThirdChildrenViewProvider.ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(@NotNull ThirdChildrenViewProvider.ViewHolder viewHolder, ThirdChildren thirdChildren) {
    ItemProviderNewsThirdChildrenCategoriesBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
    binding.executePendingBindings();
    binding.setData(thirdChildren);
  }


  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
