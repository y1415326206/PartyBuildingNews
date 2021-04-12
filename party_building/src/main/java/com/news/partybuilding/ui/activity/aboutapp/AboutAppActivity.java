package com.news.partybuilding.ui.activity.aboutapp;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.gyf.immersionbar.ImmersionBar;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.databinding.ActivityAboutAppBinding;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.viewmodel.AboutAppViewModel;

public class AboutAppActivity extends BaseActivity<ActivityAboutAppBinding, AboutAppViewModel> {

  private String version = "";

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_about_app;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(AboutAppViewModel.class);
  }

  @Override
  protected void init() {
   getVersion();
   initListener();
   setStatusBar();
  }

  /**
   * 设置状态栏颜色
   */
  private void setStatusBar(){
    UiUtils.setStatusBar(this);
  }


  /**
   * 获取版本号
   * @return 当前应用的版本号
   */

  @SuppressLint("SetTextI18n")
  public String getVersion() {
    try {
      PackageManager manager = this.getPackageManager();
      PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
      version = info.versionName;
      binding.version.setText("版本号" + " " + version);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return version;
  }

  /**
   * 点击事件监听
   */
  private void initListener(){
    binding.goBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}