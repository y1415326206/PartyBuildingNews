package com.news.partybuilding.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.databinding.FragmentCommunityBinding;
import com.news.partybuilding.viewmodel.CommunityViewModel;


public class CommunityFragment extends BaseFragment<FragmentCommunityBinding, CommunityViewModel> {

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_community;
  }

  @Override
  protected void initAndBindViewModel() {
   mViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
  }

  @Override
  protected void init() {

  }
}