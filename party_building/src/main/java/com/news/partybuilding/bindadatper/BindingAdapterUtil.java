package com.news.partybuilding.bindadatper;


import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;

public class BindingAdapterUtil {


  @BindingAdapter({"imageUrl","error"})
  public static void setImageUrl(ImageView imageView, String url, Drawable error) {
    Glide.with(imageView.getContext()).load(url).error(error).into(imageView);
  }

}
