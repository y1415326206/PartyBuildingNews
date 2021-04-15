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
import com.news.partybuilding.model.SecondChildren;
import org.jetbrains.annotations.NotNull;

public class SecondChildrenViewProvider extends ItemViewBinder<SecondChildren, SecondChildrenViewProvider.ViewHolder> {


  @NotNull
  @Override
  public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
    ItemProviderNewsSecondChildrenCategoriesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_provider_news_second_children_categories,viewGroup,false);
    return new SecondChildrenViewProvider.ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(@NotNull ViewHolder viewHolder, SecondChildren secondChildren) {
    ItemProviderNewsSecondChildrenCategoriesBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
    binding.executePendingBindings();
    binding.setData(secondChildren);
  }

  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
