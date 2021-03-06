package com.news.partybuilding.ui.fragment;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cretin.tools.cityselect.callback.OnCitySelectListener;
import com.cretin.tools.cityselect.model.CityModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.FragmentHomeBinding;
import com.news.partybuilding.databinding.LayoutSearchBottomSheetBinding;
import com.news.partybuilding.response.CityByNameResponse;
import com.news.partybuilding.response.FirstLevelCategoriesResponse;
import com.news.partybuilding.response.CitiesResponse;
import com.news.partybuilding.ui.adapter.SimpleCardFragmentAdapter;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

  private TabLayoutMediator mediator;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;

  private SimpleCardFragment simpleCardFragment;
  private ArrayList<SimpleCardFragment> fragmentList = new ArrayList<>();

  // ????????????
  private BottomSheetDialog dialog;
  private LayoutSearchBottomSheetBinding bottomSheetBinding;
  // ??????????????????
  private final List<CityModel> allCities = new ArrayList<>();
  //????????????????????????
  final List<CityModel> hotCities = new ArrayList<>();
  //
  private final ArrayList<String> tabTitles = new ArrayList<>();
  private final ArrayList<String> tabTitlesId = new ArrayList<>();
  // ??????id
  private String categoryId;
  // ??????id
  private String cityId;

  // ????????????
  private String cityName;

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_home;
  }

  @Override
  protected boolean isSupportLoad() {
    return false;
  }

  @Override
  protected void initAndBindViewModel() {
    mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
  }

  @Override
  protected void init() {
    // ????????????????????????
    setSearchIconBounds();
    // ?????????????????????id
    mViewModel.getCityIdByCityName(mDataBinding.location.getText().toString());
    mViewModel.cityByNameResponse.observe(this, new Observer<CityByNameResponse>() {
      @Override
      public void onChanged(CityByNameResponse cityByNameResponse) {
        cityId = String.valueOf(cityByNameResponse.getCityId().getId());
        // ????????????????????????
        mViewModel.requestFirstLevelArticleCategories();
      }
    });
    mViewModel.firstLevelResponse.observe(this, new Observer<FirstLevelCategoriesResponse>() {
      @Override
      public void onChanged(FirstLevelCategoriesResponse firstLevelCategoriesResponse) {
        if (tabTitles.size() > 0) {
          tabTitles.clear();
        }
        if (tabTitlesId.size() > 0) {
          tabTitlesId.clear();
        }
        int firstLevelSize = firstLevelCategoriesResponse.getFirstLevelCategoriesList().size();
        if (firstLevelSize > 0) {
          for (int i = 0; i < firstLevelSize; i++) {
            tabTitles.add(i, firstLevelCategoriesResponse.getFirstLevelCategoriesList().get(i).getName());
            tabTitlesId.add(i, String.valueOf(firstLevelCategoriesResponse.getFirstLevelCategoriesList().get(i).getId()));
            simpleCardFragment = new SimpleCardFragment();
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", tabTitlesId.get(i));
            bundle.putString("cityId", cityId);
            simpleCardFragment.setArguments(bundle);
            fragmentList.add(simpleCardFragment);
          }
        }
        // ?????????tabLayout
        initTabLayout();
      }
    });

    // ??????????????????
    mViewModel.requestAllCities();
    mViewModel.provincesCitiesResponse.observe(this, new Observer<CitiesResponse>() {
      @Override
      public void onChanged(CitiesResponse citiesResponse) {
        if (citiesResponse.getCitiesProvinces().size() > 0) {
          for (int i = 0; i < citiesResponse.getCitiesProvinces().size(); i++) {
            allCities.add(new CityModel(citiesResponse.getCitiesProvinces().get(i).getLabel()));
            if (citiesResponse.getCitiesProvinces().get(i).isHot()) {
              hotCities.add(new CityModel(citiesResponse.getCitiesProvinces().get(i).getLabel()));
            }
          }
        }
      }
    });
  }

  // ???????????????????????????
  private void setSearchIconBounds() {
    @SuppressLint("UseCompatLoadingForDrawables")
    Drawable drawable = getResources().getDrawable(R.drawable.icon_search);
    // ?????????????????????
    drawable.setBounds(0, 0, 70, 70);
    // ?????????????????????????????????????????????
    mDataBinding.searchText.setCompoundDrawables(drawable, null, null, null);
  }

  private void initTabLayout() {
    //???????????????
    mDataBinding.viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
    //viewPager ??????????????????
    mDataBinding.viewPager.registerOnPageChangeCallback(changeCallback);
    //Adapter
    SimpleCardFragmentAdapter adapter = new SimpleCardFragmentAdapter(this, fragmentList);
    mDataBinding.viewPager.setAdapter(adapter);
    mediator = new TabLayoutMediator(mDataBinding.tabs, mDataBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
      @Override
      public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        //?????????????????????TabView
        tab.setText(tabTitles.get(position));
      }
    });
    //???????????????????????????????????????????????????
    mediator.attach();
  }

  private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
    @Override
    public void onPageSelected(int position) {
      SimpleCardFragment currentFragment = (SimpleCardFragment) fragmentList.get(position);
      //????????????????????????tab?????????
      Bundle bundle = new Bundle();
      bundle.putString("categoryId", tabTitlesId.get(position));
      bundle.putString("cityId", cityId);
      currentFragment.setArguments(bundle);
    }
  };



  // ????????????????????????????????????
  private void requestPermissionAndSetGaoDe() {
    // ?????????????????????
    initGaoDeLocation();
    // ??????????????????
    if (!SharePreferenceUtil.getBoolean(Constants.IS_USER_FINE_LOCATION, false)) {
      requestPermission();
    }
  }


  @Override
  public void onResume() {
    super.onResume();
    // ?????????????????????????????????????????????
    initListenerOnLocationChange();
    mDataBinding.location.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // ???????????????
        showSearchDialog();
      }
    });
  }


  /**
   * ?????????????????????TextView ???????????????????????????ID??????
   */
  private void initListenerOnLocationChange() {
    mDataBinding.location.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        mViewModel.getCityIdByCityName(cityName);
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

  }




  /**
   * ??????????????????
   */
  private void requestPermission() {
    XXPermissions.with(this)
      // ????????????????????????
      .permission(Permission.ACCESS_FINE_LOCATION)
      .request(new OnPermissionCallback() {
        @Override
        public void onGranted(List<String> permissions, boolean all) {
          if (all) {
            mLocationClient.startLocation();
          }
        }

        @Override
        public void onDenied(List<String> permissions, boolean never) {
          if (never) {
            ToastUtils.show("??????????????????????????????");
            // ??????????????????????????????????????????????????????????????????
            //XXPermissions.startPermissionActivity(getActivity(), permissions);
          } else {
            SharePreferenceUtil.setParam(Constants.IS_USER_FINE_LOCATION, true);
            ToastUtils.show("??????????????????");
            if (cityName == null) {
              cityName = mDataBinding.location.getText().toString();
            }
            mViewModel.getCityIdByCityName(cityName);
          }
        }
      });
  }

  /**
   * ?????????????????????
   */
  private void initGaoDeLocation() {
    try {
      mLocationClient = new AMapLocationClient(getContext());
      mLocationOption = new AMapLocationClientOption();
      // ????????????????????????
      mLocationClient.setLocationListener(mAMapLocationListener);
      //???????????????????????????????????????Battery_Saving?????????????????????Device_Sensors??????????????????
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      // ??????????????????
      mLocationOption.setOnceLocation(true);
      //mLocationOption.setInterval(5000);
      //??????????????????
      mLocationClient.setLocationOption(mLocationOption);
    } catch (Exception e) {
      ToastUtils.show("???????????????????????????");
      mDataBinding.location.setText("????????????");
    }

  }


  //????????????????????????
  AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
      if (amapLocation != null) {
        if (amapLocation.getErrorCode() == 0) {
          //??????????????????
          LogUtils.i("??????????????????", "=========");
          mDataBinding.location.setText(amapLocation.getCity());
          cityName = amapLocation.getCity();
          mViewModel.getCityIdByCityName(cityName);
        } else {
          //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
          LogUtils.e("HomeFragment==> ????????????", "location Error, ErrCode:"
            + amapLocation.getErrorCode() + ", errInfo:"
            + amapLocation.getErrorInfo());

        }
      }
    }
  };


  /**
   * ????????????
   */
  private void destroyLocation() {
    if (null != mLocationClient) {
      /**
       * ??????AMapLocationClient????????????Activity???????????????
       * ???Activity???onDestroy??????????????????AMapLocationClient???onDestroy
       */
      mLocationClient.onDestroy();
      mLocationClient = null;
    }
  }

  /**
   * ???????????????????????????
   */
  private void destroyBottomSheetDialog() {
    if (dialog != null) {
      dialog.dismiss();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    destroyLocation();
    destroyBottomSheetDialog();
  }

  /**
   * ?????????????????????????????????
   */
  private void showSearchDialog() {
    if (dialog == null) {
      dialog = new BottomSheetDialog(getContext());
      bottomSheetBinding = DataBindingUtil.inflate(
        LayoutInflater.from(getActivity()),
        R.layout.layout_search_bottom_sheet,
        getActivity().findViewById(R.id.episodesContainer),
        false
      );
      dialog.setContentView(bottomSheetBinding.getRoot());
      //bottomSheetBinding.textTitle.setText(String.format("Episodes | %s", "hello"));
      //??????????????????????????????
      bottomSheetBinding.cityView.setSearchTips("?????????????????????");
      // ????????????
      if (!allCities.isEmpty() && !hotCities.isEmpty()) {
        bottomSheetBinding.cityView.bindData(allCities, hotCities, new CityModel(mDataBinding.location.getText().toString()));
      } else if (hotCities.isEmpty() && !allCities.isEmpty()) {
        bottomSheetBinding.cityView.bindData(allCities, null, new CityModel(mDataBinding.location.getText().toString()));
      } else {
        bottomSheetBinding.cityView.bindData(null, null, new CityModel(mDataBinding.location.getText().toString()));
      }
//      bottomSheetBinding.imageClose.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          dialog.dismiss();
//        }
//      });
    }
    // ---option select start--- ???????????? //
    FrameLayout frameLayout = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
    if (frameLayout != null) {
      // ??????FrameLayout????????????????????????
      ViewGroup.LayoutParams originLayoutParams = frameLayout.getLayoutParams();
      originLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
      frameLayout.setLayoutParams(originLayoutParams);
      BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
      // ??????bottomDialog???????????????
      bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    // ---option select end--- //
    dialog.show();

    initCitySelectListener();
  }

  /**
   * ???????????????????????????????????????
   */
  private void initCitySelectListener() {

    bottomSheetBinding.cityView.setOnCitySelectListener(new OnCitySelectListener() {
      @Override
      public void onCitySelect(CityModel cityModel) {
        mDataBinding.location.setText(cityModel.getCityName());
        dialog.dismiss();
      }

      @Override
      public void onSelectCancel() {
        dialog.dismiss();
      }
    });
  }


}