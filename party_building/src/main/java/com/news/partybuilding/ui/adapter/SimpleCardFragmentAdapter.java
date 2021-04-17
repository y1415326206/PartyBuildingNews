package com.news.partybuilding.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.news.partybuilding.ui.fragment.SimpleCardFragment;
import java.util.List;

public class SimpleCardFragmentAdapter extends FragmentStateAdapter {

  private final List<SimpleCardFragment> fragmentList;

  public SimpleCardFragmentAdapter(@NonNull Fragment fragment, List<SimpleCardFragment> fragmentList) {
    super(fragment);
    this.fragmentList = fragmentList;
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    return fragmentList.get(position);
  }

  @Override
  public int getItemCount() {
    return fragmentList.size();
  }
}
