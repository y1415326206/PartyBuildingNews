package com.news.partybuilding.ui.activity.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.gyf.immersionbar.ImmersionBar;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivitySettingsBinding;
import com.news.partybuilding.ui.activity.mydownloads.MyDownloadsActivity;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.viewmodel.SettingViewModel;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding, SettingViewModel> implements View.OnClickListener {


  @Override
  protected int getLayoutResId() {
    return R.layout.activity_settings;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
  }

  @Override
  protected void init() {
    setStatusBar();
    initListener();
    viewModel.requestPersonCenter(SharePreferenceUtil.getString(Constants.PUBLIC_KEY,""));
  }

  private void initListener() {
    binding.goBack.setOnClickListener(this);
    binding.logout.setOnClickListener(this);
    binding.accountSetting.setOnClickListener(this);
    binding.editInformation.setOnClickListener(this);
  }

  /**
   * 设置状态栏颜色
   */
  private void setStatusBar() {
    UiUtils.setStatusBar(this);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.go_back:
        finish();
        break;
      case R.id.logout:
        SharePreferenceUtil.setParam(Constants.PUBLIC_KEY, "");
        SharePreferenceUtil.setParam(Constants.PRIVATE_KEY, "");
        SharePreferenceUtil.setParam(Constants.IS_LOGIN, false);
        finish();
        break;
      case R.id.account_setting:
        startActivity(new Intent(this, AccountSettingActivity.class));
        break;
      case R.id.edit_information:
        break;
    }
  }
}