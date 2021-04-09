package com.news.partybuilding.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.databinding.FragmentMineBinding;
import com.news.partybuilding.ui.activity.login.LoginActivity;
import com.news.partybuilding.viewmodel.CommunityViewModel;


public class MineFragment extends BaseFragment<FragmentMineBinding, CommunityViewModel> implements View.OnClickListener {

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_mine;
  }

  @Override
  protected void initAndBindViewModel() {
   mViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
  }

  @Override
  protected void init() {
   initListener();
  }

  private void initListener(){
    mDataBinding.goLogin.setOnClickListener(this);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.go_login:
        startActivity(new Intent(getActivity(), LoginActivity.class));
        break;
    }
  }
}