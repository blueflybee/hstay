/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qtec.homestay;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.qtec.homestay.data.net.CloudRestApi;
import com.qtec.homestay.data.net.CloudRestApiImpl;
import com.qtec.homestay.data.net.ConnectionCreator;
import com.qtec.homestay.data.net.IPostConnection;
import com.qtec.homestay.data.net.RouterRestApi;
import com.qtec.homestay.data.net.RouterRestApiImpl;
import com.qtec.homestay.internal.di.components.ApplicationComponent;
import com.qtec.homestay.internal.di.components.DaggerApplicationComponent;
import com.qtec.homestay.internal.di.modules.ApplicationModule;
import com.umeng.analytics.MobclickAgent;


/**
 * Android Main Application
 */
public class AndroidApplication extends Application implements Thread.UncaughtExceptionHandler {

  public static final String APP = "RT";

  private ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    Utils.init(this);
    initCrashLog();
    initializeInjector();
    initContext();

    Logger.addLogAdapter(new AndroidLogAdapter());

//    initBle();

    initConnection();

//    initKeystoreRepertory();

//    initImagePhotoUtil();

    /* 此处要保证initCrashLogUtil 在initUmengAnalytics 之前被初始化 */
//    initCrashLogUtil();

    initUmengAnalytics();

//    initLog();

  }

  private void initCrashLog() {
//    CrashUtils.init(getFilesDir(), new CrashUtils.OnCrashListener() {
//      @Override
//      public void onCrash(String crashInfo, Throwable e) {
//
//      }
//    });
  }


//  private void initLog() {
//    LogcatUtil.start(this, "QtecLog", "blelog.txt");
//  }

//  private void initImagePhotoUtil() {
//    PhotoUtil.init(getApplicationContext(), new GlideIniter());
//  }

//  private void initBle() {
//    BleService.setBleSdkType(BleService.BLE_SDK_TYPE_ANDROID_LOCK);
//    Intent bindIntent = new Intent(this, BleService.class);
//    bindService(bindIntent, new ServiceConnection() {
//      @Override
//      public void onServiceConnected(ComponentName className,
//                                     IBinder binder) {
//        BleService service = ((BleService.LocalBinder) binder).getService();
//        mBle = service.getBle();
//
//        if (mBle != null && !mBle.adapterEnabled()) {
//          // TODO: enalbe adapter
//        }
//      }
//
//      @Override
//      public void onServiceDisconnected(ComponentName classname) {
//      }
//    }, Context.BIND_AUTO_CREATE);
//
//
//  }

  private void initContext() {
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  private void initUmengAnalytics() {
    //MobclickAgent.openActivityDurationTrack(false); // 禁止默认的页面统计方式，这样将不会再自动统计Activity。
    MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    MobclickAgent.setDebugMode(true);//调试模式
    MobclickAgent.setSessionContinueMillis(40000);
  }

  private void initializeInjector() {
    this.mApplicationComponent = DaggerApplicationComponent
        .builder()
        .applicationModule(new ApplicationModule(this))
        .build();
  }

//  private void initKeystoreRepertory() {
//    IKeystoreRepertory repertory = KeystoreRepertory.getInstance();
//    repertory.init(getApplicationContext());
//  }

  private void initConnection() {
    //  PrefConstant.getCloudUrl()
    IPostConnection cloudConnection = ConnectionCreator.create(ConnectionCreator.CLOUD_CONNECTION, CloudRestApi.URL_DEBUG_RELEASE);
    CloudRestApiImpl.setApiPostConnection(cloudConnection);

    IPostConnection routerConnection =
        ConnectionCreator.create(ConnectionCreator.ROUTER_CONNECTION, RouterRestApi.URL_DEBUG);
    RouterRestApiImpl.setApiPostConnection(routerConnection);
  }

  public ApplicationComponent getApplicationComponent() {
    return this.mApplicationComponent;
  }

  public static void exit() {
//        MobclickAgent.onKillProcess(sContext);
    android.os.Process.killProcess(android.os.Process.myPid());
  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    ex.printStackTrace();
    thread.destroy();
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
//    MultiDex.install(this);
  }

//  public IBle getIBle() {
//    return mBle;
//  }

}
