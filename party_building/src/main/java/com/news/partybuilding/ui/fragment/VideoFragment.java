package com.news.partybuilding.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.databinding.FragmentVideoBinding;
import com.news.partybuilding.viewmodel.PublicViewModel;

public class VideoFragment extends BaseFragment<FragmentVideoBinding, PublicViewModel> {


  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_video;
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