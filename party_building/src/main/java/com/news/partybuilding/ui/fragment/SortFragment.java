package com.news.partybuilding.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.databinding.FragmentSortBinding;
import com.news.partybuilding.viewmodel.SortViewModel;


public class SortFragment extends BaseFragment<FragmentSortBinding, SortViewModel> {


  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_sort;
  }

  @Override
  protected void initAndBindViewModel() {
    mViewModel = new ViewModelProvider(this).get(SortViewModel.class);
  }

  @Override
  protected void init() {

  }
}