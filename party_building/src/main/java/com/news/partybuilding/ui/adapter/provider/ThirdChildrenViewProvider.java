package com.news.partybuilding.ui.adapter.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.news.partybuilding.R;
import com.news.partybuilding.databinding.ItemProviderNewsSecondChildrenCategoriesBinding;
import com.news.partybuilding.databinding.ItemProviderNewsThirdChildrenCategoriesBinding;
import com.news.partybuilding.model.SecondChildren;
import com.news.partybuilding.model.ThirdChildren;
import com.news.partybuilding.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThirdChildrenViewProvider extends ItemViewBinder<ThirdChildren, ThirdChildrenViewProvider.ViewHolder> {

  private Context context;

  public ThirdChildrenViewProvider(Context context) {
    this.context = context;
  }

  private int checkId;

  private ArrayList<Integer> checkedId = new ArrayList<>();

  public ArrayList<Integer> getCheckedId() {
    return checkedId;
  }

  public void setCheckedId(ArrayList<Integer> checkedId) {
    this.checkedId = checkedId;
  }

  public int getCheckId() {
    return checkId;
  }

  public void setCheckId(int checkId) {
    this.checkId = checkId;
  }

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
    // 往布局中赋值
    binding.setData(thirdChildren);
    binding.thirdChildrenText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
    binding.setCheckId(checkId);

  }


  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

    }
  }
}
