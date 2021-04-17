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

  // 底部弹窗
  private BottomSheetDialog dialog;
  private LayoutSearchBottomSheetBinding bottomSheetBinding;
  // 全部城市列表
  private final List<CityModel> allCities = new ArrayList<>();
  //设置热门城市列表
  final List<CityModel> hotCities = new ArrayList<>();
  //
  private final ArrayList<String> tabTitles = new ArrayList<>();
  private final ArrayList<String> tabTitlesId = new ArrayList<>();
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
  }

  @Override
  protected void init() {
    // 设置搜索按钮大小
    setSearchIconBounds();
    // 获取默认城市的id
    mViewModel.getCityIdByCityName(mDataBinding.location.getText().toString());
    mViewModel.cityByNameResponse.observe(this, new Observer<CityByNameResponse>() {
      @Override
      public void onChanged(CityByNameResponse cityByNameResponse) {
        cityId = String.valueOf(cityByNameResponse.getCityId().getId());
        // 请求一级大类接口
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
        // 初始化tabLayout
        initTabLayout();
      }
    });

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

  // 设置搜索框图标大小
  private void setSearchIconBounds() {
    @SuppressLint("UseCompatLoadingForDrawables")
    Drawable drawable = getResources().getDrawable(R.drawable.icon_search);
    // 设置图片的大小
    drawable.setBounds(0, 0, 70, 70);
    // 设置图片的位置，左、上、右、下
    mDataBinding.searchText.setCompoundDrawables(drawable, null, null, null);
  }

  private void initTabLayout() {
    //禁用预加载
    mDataBinding.viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
    //viewPager 页面切换监听
    mDataBinding.viewPager.registerOnPageChangeCallback(changeCallback);
    //Adapter
    SimpleCardFragmentAdapter adapter = new SimpleCardFragmentAdapter(this, fragmentList);
    mDataBinding.viewPager.setAdapter(adapter);
    mediator = new TabLayoutMediator(mDataBinding.tabs, mDataBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
      @Override
      public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        //这里可以自定义TabView
        tab.setText(tabTitles.get(position));
      }
    });
    //要执行这一句才是真正将两者绑定起来
    mediator.attach();
  }

  private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
    @Override
    public void onPageSelected(int position) {
      SimpleCardFragment currentFragment = (SimpleCardFragment) fragmentList.get(position);
      //可以来设置选中时tab的大小
      Bundle bundle = new Bundle();
      bundle.putString("categoryId", tabTitlesId.get(position));
      bundle.putString("cityId", cityId);
      currentFragment.setArguments(bundle);
    }
  };



  // 初始化高德定位并获取权限
  private void requestPermissionAndSetGaoDe() {
    // 初始化高德定位
    initGaoDeLocation();
    // 请求获取权限
    if (!SharePreferenceUtil.getBoolean(Constants.IS_USER_FINE_LOCATION, false)) {
      requestPermission();
    }
  }

  // 请求接口数据
  private void requestData() {
    // 请求省市数据
    mViewModel.requestAllCities();
    // 请求一级菜单栏
    mViewModel.requestFirstLevelArticleCategories();
    //
    mViewModel.getCityIdByCityName(mDataBinding.location.getText().toString());
  }


  @Override
  public void onResume() {
    super.onResume();
    // 监听显示位置的文本是否发生改变
    initListenerOnLocationChange();
    mDataBinding.location.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 给城市赋值
        showSearchDialog();
      }
    });
  }


  /**
   * 监听显示位置的TextView 改变时调用返回城市ID接口
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
            SharePreferenceUtil.setParam(Constants.IS_USER_FINE_LOCATION, true);
            ToastUtils.show("获取权限失败");
            if (cityName == null) {
              cityName = mDataBinding.location.getText().toString();
            }
            mViewModel.getCityIdByCityName(cityName);
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
          mViewModel.getCityIdByCityName(cityName);
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