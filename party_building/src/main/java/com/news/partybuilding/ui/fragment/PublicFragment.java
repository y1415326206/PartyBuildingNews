package com.news.partybuilding.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.databinding.FragmentPublicBinding;
import com.news.partybuilding.viewmodel.PublicViewModel;

public class PublicFragment extends BaseFragment<FragmentPublicBinding, PublicViewModel> {


  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_public;
  }

  @Override
  protected void initAndBindViewModel() {
   mViewModel = new ViewModelProvider(this).get(PublicViewModel.class);
  }

  @Override
  protected void init() {

  }

  @Override
  protected boolean isSupportLoad() {
    return true;
  }
}