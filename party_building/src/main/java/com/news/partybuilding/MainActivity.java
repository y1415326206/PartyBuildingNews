package com.news.partybuilding;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.databinding.ActivityMainBinding;
import com.news.partybuilding.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_main;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    binding.setViewModel(viewModel);
  }

  @Override
  protected void init() {
    // 初始化底部导航栏和fragment
    initFragment();
  }

  private void initFragment() {
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    assert navHostFragment != null;
    binding.navViewBottom.setItemIconTintList(null);
    NavController navController = navHostFragment.getNavController();
    NavigationUI.setupWithNavController(binding.navViewBottom, navController);
  }


  // 初始化侧边栏点击事件
  private void initUserDada() {
//    SwitchButton switchButton = binding.navView.getHeaderView(0).findViewById(R.id.switch_button);
//    int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//    switchButton.setChecked(mode == Configuration.UI_MODE_NIGHT_YES);
//    switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//      @Override
//      public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//        if (isChecked) {
//          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//          SharePreferenceUtil.setParam("is_set_night_theme",true);
//        } else {
//          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//          SharePreferenceUtil.setParam("is_set_night_theme",false);
//        }
//      }
//    });
  }
  // 在AndroidManifest.xml 中加入 android:configChanges="uiMode" 当主题改变是不会重启Activity
  // AppCompatDelegate.setDefaultNightMode()这个方法会调用recreate()方法重启当前的activity 但是会有闪屏
  // 当主题改变时 下面这个方法会走 在这个方法里我们手动重启Activity并加入动画来解决闪屏的问题
  @Override
  public void onConfigurationChanged(@NonNull Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    restartActivity();
  }

  private void restartActivity() {
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(intent);
    finish();
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }

  @Override
  protected boolean isSupportLoad() {
    return false;
  }

  // 两秒内连续按两下就退出APP-当前是否已经按了第一下
  boolean doubleBackToExitPressedOnce = false;

  @Override
  public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      //super.onBackPressed();
      // 这里设置一下 退出app时再次点击 不会出现启动页
      Intent intent = new Intent(Intent.ACTION_MAIN);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.addCategory(Intent.CATEGORY_HOME);
      startActivity(intent);
    }
    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, R.string.exit_the_program, Toast.LENGTH_SHORT).show();
    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
  }


}