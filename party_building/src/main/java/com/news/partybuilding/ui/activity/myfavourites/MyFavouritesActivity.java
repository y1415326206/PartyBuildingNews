package com.news.partybuilding.ui.activity.myfavourites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.databinding.ActivityMyFavouritesBinding;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.viewmodel.MyFavouritesViewModel;

public class MyFavouritesActivity extends BaseActivity<ActivityMyFavouritesBinding, MyFavouritesViewModel> implements View.OnClickListener {


  @Override
  protected int getLayoutResId() {
    return R.layout.activity_my_favourites;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(MyFavouritesViewModel.class);
  }

  @Override
  protected void init() {
    setStatusBar();
    initListener();
  }

  /**
   * 设置状态栏颜色
   */
  private void setStatusBar() {
    UiUtils.setStatusBar(this);
  }

  private void initListener() {
    binding.goBack.setOnClickListener(this);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.go_back:
        finish();
        break;
    }
  }
}