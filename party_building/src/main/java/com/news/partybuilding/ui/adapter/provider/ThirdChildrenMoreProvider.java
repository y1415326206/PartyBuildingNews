package com.news.partybuilding.ui.adapter.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drakeet.multitype.ItemViewBinder;
import com.news.partybuilding.R;
import com.news.partybuilding.databinding.ItemProviderNewsCategoriesMoreBinding;
import com.news.partybuilding.model.EmptyValue;
import org.jetbrains.annotations.NotNull;

public class ThirdChildrenMoreProvider extends ItemViewBinder<EmptyValue, ThirdChildrenMoreProvider.ViewHolder> {
  @NotNull
  @Override
  public ThirdChildrenMoreProvider.ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
    ItemProviderNewsCategoriesMoreBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_provider_news_categories_more,viewGroup,false);
    return new ThirdChildrenMoreProvider.ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(@NotNull ThirdChildrenMoreProvider.ViewHolder viewHolder, EmptyValue emptyValue) {
    ItemProviderNewsCategoriesMoreBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
    binding.executePendingBindings();
    binding.setData(emptyValue);
  }

  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
