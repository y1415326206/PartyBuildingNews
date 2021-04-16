package com.news.partybuilding.ui.activity.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivityAccountSettingBinding;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.viewmodel.AccountSettingViewModel;

public class AccountSettingActivity extends BaseActivity<ActivityAccountSettingBinding, AccountSettingViewModel> {


  @Override
  protected int getLayoutResId() {
    return R.layout.activity_account_setting;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(AccountSettingViewModel.class);
    binding.setData(viewModel);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (SharePreferenceUtil.getString(Constants.MOBILE, "").isEmpty()) {
      viewModel.mobileEmpty.postValue(true);
      viewModel.mobile.postValue("");
    } else {
      viewModel.mobileEmpty.postValue(false);
      viewModel.mobile.postValue(SharePreferenceUtil.getString(Constants.MOBILE, ""));
    }
  }



  @Override
  protected void init() {
    setStatusBar();
    binding.goBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    binding.bindOrEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(AccountSettingActivity.this, BindOrEditMobileActivity.class));
      }
    });
  }

  /**
   * 设置状态栏颜色
   */
  private void setStatusBar() {
    UiUtils.setStatusBar(this);
  }


}