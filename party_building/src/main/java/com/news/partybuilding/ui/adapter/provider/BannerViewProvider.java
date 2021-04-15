package com.news.partybuilding.ui.adapter.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drakeet.multitype.ItemViewBinder;
import com.news.partybuilding.R;
import com.news.partybuilding.databinding.ItemProviderNewsCenterPictureVideoBinding;
import com.news.partybuilding.model.Banner;
import org.jetbrains.annotations.NotNull;

public class BannerViewProvider extends ItemViewBinder<Banner, BannerViewProvider.ViewHolder> {


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
  }

  static class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
