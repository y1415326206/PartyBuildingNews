package com.news.partybuilding.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.databinding.FragmentMineBinding;
import com.news.partybuilding.viewmodel.CommunityViewModel;


public class MineFragment extends BaseFragment<FragmentMineBinding, CommunityViewModel> {

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

  }
}