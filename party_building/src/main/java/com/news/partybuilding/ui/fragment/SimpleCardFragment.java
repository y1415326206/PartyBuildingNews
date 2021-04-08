package com.news.partybuilding.ui.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.news.partybuilding.R;
import com.news.partybuilding.adapter.ImageAdapter;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.databinding.FragmentSimpleCardBinding;
import com.news.partybuilding.model.HomeBanner;
import com.news.partybuilding.response.HomeBannerResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.viewmodel.SimpleCardViewModel;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.RotateYTransformer;


public class SimpleCardFragment extends BaseFragment<FragmentSimpleCardBinding, SimpleCardViewModel> {
  private String simpleText;

  public static SimpleCardFragment getInstance(String text) {
    SimpleCardFragment simpleCardFragment = new SimpleCardFragment();
    simpleCardFragment.simpleText = text;
    return simpleCardFragment;
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_simple_card;
  }

  @Override
  protected void initAndBindViewModel() {
   mViewModel = new ViewModelProvider(this).get(SimpleCardViewModel.class);
   mDataBinding.setViewModel(mViewModel);
  }

  @Override
  protected void init() {
   //mDataBinding.cardTitleTv.setText(simpleText);
   //initBannerData();
  }


//  private void initBannerData() {
//    // 获取首页banner数据
//    mViewModel.getHomeBannerData();
//    mViewModel.bannerResponse.observe(this, new Observer<HomeBannerResponse>() {
//
//      @Override
//      public void onChanged(HomeBannerResponse homeBannerResponse) {
//        mDataBinding.banner.setAdapter(new ImageAdapter(homeBannerResponse.getHomeBanners()), true)
//          .setPageTransformer(new RotateYTransformer())
//          .setIndicator(new RectangleIndicator(getContext()))
//          .setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(Object data, int position) {
//              HomeBanner homeBannerData = (HomeBanner)data;
//              LogUtils.i("==========", homeBannerData.getImagePath());
//            }
//          });
//      }
//    });
//  }
}
