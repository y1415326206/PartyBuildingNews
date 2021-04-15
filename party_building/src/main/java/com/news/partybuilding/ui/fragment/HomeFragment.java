package com.news.partybuilding.ui.fragment;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.TextViewBindingAdapter;
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
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.databinding.FragmentHomeBinding;
import com.news.partybuilding.databinding.LayoutSearchBottomSheetBinding;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.CityByNameResponse;
import com.news.partybuilding.response.FirstLevelCategoriesResponse;
import com.news.partybuilding.response.ProvincesCitiesResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.NetWorkUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.utils.UiOperation;
import com.news.partybuilding.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

  private final ArrayList<String> mTitles = new ArrayList<>();
  private final ArrayList<String> mTitlesKey = new ArrayList<>();
  private TabLayoutMediator mediator;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;
  private SimpleCardFragment simpleCardFragment;
  private ArrayList<SimpleCardFragment> simpleCardFragments = new ArrayList<>();
  private HashMap<String, String> firstLevelTitles = new HashMap<>();
  // 底部弹窗
  private BottomSheetDialog dialog;
  private LayoutSearchBottomSheetBinding bottomSheetBinding;
  // 全部城市列表
  private final List<CityModel> allCities = new ArrayList<>();
  //设置热门城市列表
  final List<CityModel> hotCities = new ArrayList<>();
  // 分类id
  private String categoryId;
  // 城市id
  private String cityId;
  // 定位结果
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
    mDataBinding.setViewModel(mViewModel);
  }

  @Override
  protected void init() {
    // 设置搜索按钮大小
    setSearchIconBounds();
    // 初始化高德定位并获取权限
    requestPermissionAndSetGaoDe();
    // 请求接口数据
    requestData();
    // 观察viewModel数据
    observeViewModelData();

    //initTabLayout();

  }

  // 设置搜索框图标大小
  private void setSearchIconBounds() {
    @SuppressLint("UseCompatLoadingForDrawables")
    Drawable drawable = getResources().getDrawable(R.drawable.icon_search);
    // 设置图片的大小
    drawable.setBounds(0, 0, 70, 70);
    // 设置图片的位置，左、上、右、下
    mDataBinding.searchText.setCompoundDrawables(drawable, null, null, null);
  }

  // 初始化高德定位并获取权限
  private void requestPermissionAndSetGaoDe() {
    // 初始化高德定位
    initGaoDeLocation();
    // 请求获取权限
    if (!SharePreferenceUtil.getBoolean(Constants.IS_USER_ACCESS_FINE_LOCATION, false)) {
      requestPermission();
    }
  }

  // 请求接口数据
  private void requestData() {
    // 请求省市数据
    mViewModel.requestAllCities();
    // 请求一级菜单栏
    mViewModel.requestFirstLevelArticleCategories();
  }


  // 观察viewModel数据
  private void observeViewModelData() {
    mViewModel.cityByNameResponse.observe(this, new Observer<CityByNameResponse>() {
      @Override
      public void onChanged(CityByNameResponse cityByNameResponse) {
        if (cityByNameResponse != null){
          cityId = String.valueOf(cityByNameResponse.getCityId());
        }
      }
    });

    mViewModel.provincesCitiesResponse.observe(this, new Observer<ProvincesCitiesResponse>() {
      @Override
      public void onChanged(ProvincesCitiesResponse provincesCitiesResponse) {
        if (provincesCitiesResponse.getCitiesProvinces().size() > 0) {
          for (int i = 0; i < provincesCitiesResponse.getCitiesProvinces().size(); i++) {
            allCities.add(new CityModel(provincesCitiesResponse.getCitiesProvinces().get(i).getLabel()));
            if (provincesCitiesResponse.getCitiesProvinces().get(i).isHot()) {
              hotCities.add(new CityModel(provincesCitiesResponse.getCitiesProvinces().get(i).getLabel()));
            }
          }
        }
      }
    });

    mViewModel.firstLevelResponse.observe(this, new Observer<FirstLevelCategoriesResponse>() {
      @Override
      public void onChanged(FirstLevelCategoriesResponse firstLevelCategoriesResponse) {
        if (mTitles.size() > 0) {
          mTitles.clear();
        }
        if (mTitlesKey.size() > 0){
          mTitlesKey.clear();
        }
        int firstLevelSize = firstLevelCategoriesResponse.getFirstLevelCategoriesList().size();
        if (firstLevelSize > 0) {
          for (int i = 0; i < firstLevelSize; i++) {
            mTitles.add(i, firstLevelCategoriesResponse.getFirstLevelCategoriesList().get(i).getName());
            mTitlesKey.add(i, String.valueOf(firstLevelCategoriesResponse.getFirstLevelCategoriesList().get(i).getId()));
            simpleCardFragment = new SimpleCardFragment();
            simpleCardFragments.add(simpleCardFragment);
          }
        }
      }
    });
  }



  private void setPageData() {

    // 获取城市id
    getCityIdByName();
    // 地理位置的点击事件
    locationOnClick();
  }


  @Override
  public void onResume() {
    super.onResume();
    // 监听显示位置的文本是否发生改变
    //initListenerOnLocationChange();
  }


  // 根据城市名称获取城市Id
  private void getCityIdByName() {
    // 调用给cityId赋值
    //setCityIdData();
  }

  // 地理位置的点击事件
  private void locationOnClick() {
    mDataBinding.location.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 给城市赋值
       // setDataToCities();
        showSearchDialog();
      }
    });
  }

  /**
   * 监听显示位置的TextView 改变是调用返回城市ID接口
   */
  private void initListenerOnLocationChange() {
    mDataBinding.location.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        getCityIdByName();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

  }




  private void initTabLayout() {
    //禁用预加载
    mDataBinding.viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
    //viewPager 页面切换监听
    mDataBinding.viewPager.registerOnPageChangeCallback(changeCallback);
    //Adapter
    mDataBinding.viewPager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
      @NonNull
      @Override
      public Fragment createFragment(int position) {
        // 实例化Fragment的时候传入两个参数
        LogUtils.i("categoryId and cityID SHI ============>>>", categoryId + "  " + cityId);
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", categoryId);
        bundle.putString("cityId", cityId);
        simpleCardFragment.setArguments(bundle);
        return simpleCardFragments.get(position);
      }

      @Override
      public int getItemCount() {
        return mTitles.size();
      }
    });
    mediator = new TabLayoutMediator(mDataBinding.tabs, mDataBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
      @Override
      public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        //这里可以自定义TabView
        tab.setText(mTitles.get(position));
      }
    });
    //要执行这一句才是真正将两者绑定起来
    mediator.attach();
  }


  private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
    @Override
    public void onPageSelected(int position) {

      LogUtils.i("----------cateId  and cityId-----------", categoryId + " " + cityId);
      //可以来设置选中时tab的大小
      Bundle bundle = new Bundle();
      bundle.putString("categoryId", categoryId);
      bundle.putString("cityId", cityId);
      simpleCardFragment.setArguments(bundle);
    }
  };

  /**
   * 请求定位权限
   */
  private void requestPermission() {
    XXPermissions.with(this)
      // 申请精确定位权限
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
            ToastUtils.show("拒绝授权，请手动授予");
            // 如果是被永久拒绝就跳转到应用权限系统设置页面
            //XXPermissions.startPermissionActivity(getActivity(), permissions);
          } else {
            SharePreferenceUtil.setParam(Constants.IS_USER_ACCESS_FINE_LOCATION, true);
            ToastUtils.show("获取权限失败");
            if (cityName == null) {
              cityName = mDataBinding.location.getText().toString();
            }
            mViewModel.getCityId(cityName);
          }
        }
      });
  }

  /**
   * 初始化高德定位
   */
  private void initGaoDeLocation() {
    try {
      mLocationClient = new AMapLocationClient(getContext());
      mLocationOption = new AMapLocationClientOption();
      // 设置定位回调监听
      mLocationClient.setLocationListener(mAMapLocationListener);
      //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      // 设置单次定位
      mLocationOption.setOnceLocation(true);
      //mLocationOption.setInterval(5000);
      //设置定位参数
      mLocationClient.setLocationOption(mLocationOption);
    } catch (Exception e) {
      ToastUtils.show("高德定位初始化失败");
      mDataBinding.location.setText("乌鲁木齐");
    }

  }


  //异步获取定位结果
  AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
      if (amapLocation != null) {
        if (amapLocation.getErrorCode() == 0) {
          //解析定位结果
          LogUtils.i("解析定位结果", "=========");
          mDataBinding.location.setText(amapLocation.getCity());
          cityName = amapLocation.getCity();
          mViewModel.getCityId(cityName);
        } else {
          //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
          LogUtils.e("HomeFragment==> 高德定位", "location Error, ErrCode:"
            + amapLocation.getErrorCode() + ", errInfo:"
            + amapLocation.getErrorInfo());

        }
      }
    }
  };


  /**
   * 销毁定位
   */
  private void destroyLocation() {
    if (null != mLocationClient) {
      /**
       * 如果AMapLocationClient是在当前Activity实例化的，
       * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
       */
      mLocationClient.onDestroy();
      mLocationClient = null;
    }
  }

  /**
   * 销毁底部弹出框实例
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
   * 显示搜索城市底部弹出框
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
      //设置搜索框的文案提示
      bottomSheetBinding.cityView.setSearchTips("请输入城市名称");
      // 绑定数据
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
    // ---option select start--- 可选参数 //
    FrameLayout frameLayout = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
    if (frameLayout != null) {
      // 获取FrameLayout的参数并设置全屏
      ViewGroup.LayoutParams originLayoutParams = frameLayout.getLayoutParams();
      originLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
      frameLayout.setLayoutParams(originLayoutParams);
      BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
      // 设置bottomDialog显示的位置
      bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    // ---option select end--- //
    dialog.show();

    initCitySelectListener();
  }

  /**
   * 设置城市选择之后的事件监听
   */
  private void initCitySelectListener() {

    bottomSheetBinding.cityView.setOnCitySelectListener(new OnCitySelectListener() {
      @Override
      public void onCitySelect(CityModel cityModel) {

      }

      @Override
      public void onSelectCancel() {
        dialog.dismiss();
      }
    });
  }


}