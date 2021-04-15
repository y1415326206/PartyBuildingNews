package com.news.partybuilding.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.news.partybuilding.R;
import com.news.partybuilding.config.LoadState;
import com.news.partybuilding.databinding.ActivityBaseBinding;
import com.news.partybuilding.databinding.ViewLoadErrorBinding;
import com.news.partybuilding.databinding.ViewLoadingBinding;
import com.news.partybuilding.databinding.ViewNoDataBinding;
import com.news.partybuilding.databinding.ViewNoNetworkBinding;
import com.news.partybuilding.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

  public DB binding;
  public VM viewModel;

  private ActivityBaseBinding mActivityBaseBinding;
  private ViewLoadingBinding mViewLoadingBinding;
  private ViewLoadErrorBinding mViewLoadErrorBinding;
  private ViewNoNetworkBinding mViewNoNetworkBinding;
  private ViewNoDataBinding mViewNoDataBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mActivityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
    binding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutResId(),
      mActivityBaseBinding.flContentContainer, true);
    LogUtils.i("==========baseActivity","baseActivity onCreate");
    initAndBindViewModel();
    initLoadState();
    initLifecycle();
    init();
  }

  private void initLifecycle(){
    binding.setLifecycleOwner(this);
    // ViewModel订阅生命周期事件 这样viewModel就可以观察到Activity的生命周期了
    if (viewModel != null) {
      getLifecycle().addObserver(viewModel);
    }
  }

  private void initLoadState() {
    LogUtils.i("==========baseActivity","baseActivity initLoadState");
    if (viewModel != null && isSupportLoad()) {
      viewModel.loadState.observe(this, new Observer<LoadState>() {
        @Override
        public void onChanged(LoadState loadState) {
          switchLoadView(loadState);
        }
      });
    }
  }

  private void removeLoadView() {
    int childCount = mActivityBaseBinding.flContentContainer.getChildCount();
    LogUtils.i("==========baseActivity","baseActivity " + childCount);
    if (childCount > 1) {
      mActivityBaseBinding.flContentContainer.removeViews(1, childCount - 1);
    }
  }

  private void switchLoadView(LoadState loadState) {
    LogUtils.i("==========baseActivity","baseActivity switchLoadView");
    removeLoadView();

    switch (loadState) {
      case LOADING:
        if (mViewLoadingBinding == null) {
          mViewLoadingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_loading,
            mActivityBaseBinding.flContentContainer, false);
        }
        mActivityBaseBinding.flContentContainer.addView(mViewLoadingBinding.getRoot());
        break;

      case NO_NETWORK:
        if (mViewNoNetworkBinding == null) {
          mViewNoNetworkBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_no_network,
            mActivityBaseBinding.flContentContainer, false);
          mViewNoNetworkBinding.setViewModel(viewModel);
        }
        mActivityBaseBinding.flContentContainer.addView(mViewNoNetworkBinding.getRoot());
        break;

      case NO_DATA:
        if (mViewNoDataBinding == null) {
          mViewNoDataBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_no_data,
            mActivityBaseBinding.flContentContainer, false);
        }
        mActivityBaseBinding.flContentContainer.addView(mViewNoDataBinding.getRoot());
        break;

      case ERROR:
        if (mViewLoadErrorBinding == null) {
          mViewLoadErrorBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_load_error,
            mActivityBaseBinding.flContentContainer, false);
        }
        mActivityBaseBinding.flContentContainer.addView(mViewLoadErrorBinding.getRoot());
        break;

      default:
        break;
    }
  }


  /**
   * 处理参数
   *
   * @param intent 参数容器
   */
  protected void handleIntent(Intent intent) {

  }


  /**
   * 是否支持页面加载。默认不支持
   *
   * @return true表示支持，false表示不支持
   */
  protected boolean isSupportLoad() {
    return true;
  }

  /**
   * 获取当前页面的布局资源ID
   *
   * @return 布局资源ID
   */
  protected abstract int getLayoutResId();

  /**
   * 初始化和绑定ViewModel
   */
  protected abstract void initAndBindViewModel();

  // 处理
  protected abstract void init();

}