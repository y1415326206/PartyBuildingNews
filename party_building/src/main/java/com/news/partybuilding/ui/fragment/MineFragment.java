package com.news.partybuilding.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.FragmentMineBinding;
import com.news.partybuilding.ui.activity.aboutapp.AboutAppActivity;
import com.news.partybuilding.ui.activity.login.LoginActivity;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.viewmodel.MineViewModel;


public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel> implements View.OnClickListener {

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_mine;
  }

  @Override
  protected void initAndBindViewModel() {
    mViewModel = new ViewModelProvider(this).get(MineViewModel.class);
    mDataBinding.setMineViewModel(mViewModel);
  }

  @Override
  protected void init() {
    initListener();
  }

  @Override
  public void onResume() {
    super.onResume();
    // 是否登录
    loginOrNot();
  }

  private void loginOrNot() {
    mViewModel.loginOrNot.postValue(SharePreferenceUtil.getBoolean(Constants.IS_LOGIN, false));
  }

  private void initListener() {
    mDataBinding.goLogin.setOnClickListener(this);
    mDataBinding.logout.setOnClickListener(this);
    mDataBinding.aboutApp.setOnClickListener(this);
  }


  /**
   * 退出登录
   */
  private void logout() {
    SharePreferenceUtil.setParam(Constants.IS_LOGIN, false);
    mViewModel.loginOrNot.postValue(false);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.go_login:
        startActivity(new Intent(getActivity(), LoginActivity.class));
        break;
      case R.id.logout:
        logout();
        break;
      case R.id.about_app:
        startActivity(new Intent(getActivity(), AboutAppActivity.class));
        break;
    }
  }
}