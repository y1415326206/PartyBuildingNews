package com.news.partybuilding.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.databinding.FragmentMediaBinding;
import com.news.partybuilding.viewmodel.SquareViewModel;


public class MediaFragment extends BaseFragment<FragmentMediaBinding, SquareViewModel> {


  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_media;
  }

  @Override
  protected void initAndBindViewModel() {
    mViewModel = new ViewModelProvider(this).get(SquareViewModel.class);
  }

  @Override
  protected void init() {

  }
}