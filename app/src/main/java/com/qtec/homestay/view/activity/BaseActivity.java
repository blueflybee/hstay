package com.qtec.homestay.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qtec.homestay.AndroidApplication;
import com.qtec.homestay.R;
import com.qtec.homestay.internal.di.components.ApplicationComponent;
import com.qtec.homestay.internal.di.modules.ActivityModule;
import com.qtec.homestay.navigation.Navigator;
import com.qtec.homestay.utils.DialogUtil;
import com.qtec.homestay.view.LoadDataView;
import com.qtec.homestay.view.component.TitleBar;
import com.qtec.homestay.view.login.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

//import com.umeng.analytics.MobclickAgent;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity implements LoadDataView {

  @Inject
  protected Navigator mNavigator;

  protected TitleBar mTitleBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getApplicationComponent().inject(this);
  }

  /**
   * Adds a {@link Fragment} to this activity's file_paths.
   *
   * @param containerViewId The container view to where add the fragment.
   * @param fragment        The fragment to be added.
   */
  protected void addFragment(int containerViewId, Fragment fragment) {
    FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(containerViewId, fragment);
    fragmentTransaction.commit();
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link ApplicationComponent}
   */
  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  /**
   * Get an Activity module for dependency injection.
   *
   * @return {@link ActivityModule}
   */
  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  public Navigator getNavigator() {
    return mNavigator;
  }


  @Override
  protected void onResume() {
    super.onResume();
    MobclickAgent.onPageStart("SplashScreen");//统计页面
    MobclickAgent.onResume(this); //统计时长
  }


  @Override
  protected void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd("SplashScreen");
    MobclickAgent.onPause(this);
  }

  protected void initTitleBar(String title) {
    if (mTitleBar == null) {
      mTitleBar = (TitleBar) findViewById(R.id.title_bar);
    }
    mTitleBar.setTitleCenter(title);
    setSupportActionBar(mTitleBar.getToolbar());
    mTitleBar.setLeftAsBackButton();
  }


  @Override
  public void showLoading() {
    DialogUtil.showProgress(this);
  }

  @Override
  public void hideLoading() {
    DialogUtil.hideProgress();
  }

  protected void hideSoftKeyBoard() {
    new Handler().postDelayed(
        () -> KeyboardUtils.hideSoftInput(this), 200);
  }

  @Override
  public Context getContext() {
    return this;
  }

  /*
   * 为子类提供权限检查方法
   * */
  protected boolean hasPermission(String... permissions) {
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission)
          != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }


  @Override
  public void onError(String message) {
    if (!TextUtils.isEmpty(message)) {
      ToastUtils.showShort(message);
    }

  }

  @Override
  public void showLoginInvalid() {
    // TODO: 18-7-5 showLoginInvalid
//    DialogUtil.showOkAlertDialogWithMessage(
//        getContext(),
//        "登录失效", "您的登录已失效，请重新登录",
//        v -> mNavigator.navigateNewAndClearTask(BaseActivity.this.getContext(), LoginActivity.class));
  }


  protected void changeTitleRightBtnStyle(String input) {
    changeTitleRightBtnStyle(TextUtils.isEmpty(input));
  }

  protected void changeTitleRightBtnStyle(boolean empty) {
    boolean isModify = !empty;
    int color = isModify ? R.color.black_424242 : R.color.gray_cdcdcd;
    mTitleBar.setRightEnable(isModify, getResources().getColor(color));
  }

  @NonNull
  protected String getText(TextView tv) {
    return tv.getText().toString().trim();
  }

}
