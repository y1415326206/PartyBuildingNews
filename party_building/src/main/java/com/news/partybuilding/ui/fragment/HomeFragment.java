package com.news.partybuilding.ui.fragment;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.adapter.ImageAdapter;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.FragmentHomeBinding;
import com.news.partybuilding.model.HomeBanner;
import com.news.partybuilding.response.HomeBannerResponse;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.viewmodel.HomeViewModel;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.RotateYTransformer;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

  private final ArrayList<Fragment> mFragments = new ArrayList<>();
  private final String[] mTitles = {"热门", "iOS", "Android", "前端", "后端", "设计", "工具资源"};
  private TabLayoutMediator mediator;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;

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
    initTabLayout();
    // 初始化高德定位
    initGaoDeLocation();
    // 获取权限
    if (!SharePreferenceUtil.getBoolean(Constants.IS_USER_ACCESS_FINE_LOCATION,false)){
      requestPermission();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    startLocation();
  }

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


  private void requestPermission(){
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
            SharePreferenceUtil.setParam(Constants.IS_USER_ACCESS_FINE_LOCATION,true);
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

  private void startLocation(){
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

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    destroyLocation();
  }


}