package com.news.partybuilding.ui.fragment;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.databinding.FragmentHomeBinding;
import com.news.partybuilding.databinding.LayoutSearchBottomSheetBinding;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.NetWorkUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

  private final String[] mTitles = {"热门", "iOS", "Android", "前端", "后端", "设计", "工具资源"};
  private TabLayoutMediator mediator;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;
  // 底部弹窗
  private BottomSheetDialog dialog;
  private LayoutSearchBottomSheetBinding bottomSheetBinding;
  // 全部城市列表
  private final List<CityModel> allCities = new ArrayList<>();
  //设置热门城市列表
  final List<CityModel> hotCities = new ArrayList<>();

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_home;
  }

  @Override
  protected boolean isSupportLoad() {
    return true;
  }

  @Override
  protected void initAndBindViewModel() {
    mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    mDataBinding.setViewModel(mViewModel);
  }

  @Override
  protected void init() {
    // 初始化tabLayout
    if (NetWorkUtils.isConnected()){
      initTabLayout();
    }else {
      mViewModel.loadState.postValue(LoadState.NO_NETWORK);
    }

    // 初始化高德定位
    initGaoDeLocation();
    // 获取权限
    if (!SharePreferenceUtil.getBoolean(Constants.IS_USER_ACCESS_FINE_LOCATION, false)) {
      requestPermission();
    }
    // 给城市赋值
    setDataToCities();
    // 设置搜索按钮大小
    setSearchIconBounds();
    mDataBinding.location.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showSearchDialog();
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    // 开始定位
    startLocation();
  }

  private void setSearchIconBounds(){
    @SuppressLint("UseCompatLoadingForDrawables")
    Drawable drawable = getResources().getDrawable(R.drawable.icon_search);
    // 设置图片的大小
    drawable.setBounds(0, 0, 70, 70);
    // 设置图片的位置，左、上、右、下
    mDataBinding.searchText.setCompoundDrawables(drawable, null, null, null);
  }


  /**
   * 给城市赋值
   */
  private void setDataToCities() {
    allCities.add(new CityModel("北京"));
    allCities.add(new CityModel("伤害"));
    allCities.add(new CityModel("广州"));
    allCities.add(new CityModel("深圳"));
    allCities.add(new CityModel("香港"));
    allCities.add(new CityModel("澳门"));
    allCities.add(new CityModel("新加坡"));
    allCities.add(new CityModel("新疆"));
    allCities.add(new CityModel("赫曼"));
    allCities.add(new CityModel("马鞍山"));
    hotCities.add(new CityModel("北京"));
    hotCities.add(new CityModel("新疆"));
  }


  /**
   * 初始化tabLayout和ViewPager2
   */
  private void initTabLayout() {
    //禁用预加载
    mDataBinding.viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
    //Adapter
    mDataBinding.viewPager.setAdapter(new FragmentStateAdapter(this) {
      @NonNull
      @Override
      public Fragment createFragment(int position) {
        return SimpleCardFragment.getInstance(mTitles[position]);
      }

      @Override
      public int getItemCount() {
        return mTitles.length;
      }
    });
    mediator = new TabLayoutMediator(mDataBinding.tabs, mDataBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
      @Override
      public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        //这里可以自定义TabView
        tab.setText(mTitles[position]);
      }
    });
    //要执行这一句才是真正将两者绑定起来
    mediator.attach();
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
            //ToastUtils.show("获取成功");
            startLocation();
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
      mLocationOption.setInterval(5000);
      //设置定位参数
      mLocationClient.setLocationOption(mLocationOption);
    } catch (Exception e) {
      ToastUtils.show("高德定位初始化失败");
      mDataBinding.location.setText("新疆");
    }

  }

  /**
   * 开始定位
   */
  private void startLocation() {
    //启动定位
    mLocationClient.startLocation();
  }

  //异步获取定位结果
  AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
      if (amapLocation != null) {
        if (amapLocation.getErrorCode() == 0) {
          //解析定位结果
          //解析定位结果
          mDataBinding.location.setText(amapLocation.getCity());
          //获取当前定位结果来源，如网络定位结果，详见定位类型表
          LogUtils.i("定位类型", amapLocation.getLocationType() + "");
          LogUtils.i("获取纬度", amapLocation.getLatitude() + "");
          LogUtils.i("获取经度", amapLocation.getLongitude() + "");
          LogUtils.i("获取精度信息", amapLocation.getAccuracy() + "");

          //如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
          LogUtils.i("地址", amapLocation.getAddress());
          LogUtils.i("国家信息", amapLocation.getCountry());
          LogUtils.i("省信息", amapLocation.getProvince());
          LogUtils.i("城市信息", amapLocation.getCity());
          LogUtils.i("城区信息", amapLocation.getDistrict());
          LogUtils.i("街道信息", amapLocation.getStreet());
          LogUtils.i("街道门牌号信息", amapLocation.getStreetNum());
          LogUtils.i("城市编码", amapLocation.getCityCode());
          LogUtils.i("地区编码", amapLocation.getAdCode());
          LogUtils.i("获取当前定位点的AOI信息", amapLocation.getAoiName());
          LogUtils.i("获取当前室内定位的建筑物Id", amapLocation.getBuildingId());
          LogUtils.i("获取当前室内定位的楼层", amapLocation.getFloor());
          LogUtils.i("获取GPS的当前状态", amapLocation.getGpsAccuracyStatus() + "");
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