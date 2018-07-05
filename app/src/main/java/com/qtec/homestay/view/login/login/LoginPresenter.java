package com.qtec.homestay.view.login.login;

import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.qtec.homestay.constant.AppConstant;
import com.qtec.homestay.data.constant.PrefConstant;
import com.qtec.homestay.data.exception.PasswordErrMoreTimesException;
import com.qtec.homestay.data.exception.PasswordErrThreeTimesException;
import com.qtec.homestay.data.net.CloudRestApiImpl;
import com.qtec.homestay.data.net.CloudUrlPath;
import com.qtec.homestay.domain.constant.CloudUseCaseComm;
import com.qtec.homestay.domain.interactor.UseCase;
import com.qtec.homestay.domain.model.core.QtecEncryptInfo;
import com.qtec.homestay.domain.model.mapp.req.LoginRequest;
import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.interactor.AppSubscriber;
import com.qtec.homestay.internal.di.PerActivity;
import com.qtec.homestay.presenter.Presenter;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.internal.Preconditions;



/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class LoginPresenter implements Presenter {

  private final UseCase loginUseCase;
  private LoginView loginView;

  @Inject
  public LoginPresenter(@Named(CloudUseCaseComm.LOGIN) UseCase loginUseCase) {
    this.loginUseCase = Preconditions.checkNotNull(loginUseCase, "loginUseCase is null");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    loginUseCase.dispose();
    loginView = null;
  }

  public void setView(LoginView loginView) {
    this.loginView = loginView;
  }

  public void login(String username, String password) {
    if (TextUtils.isEmpty(username)) {
      loginView.showUsernameEmp();
      return;
    }
    if (TextUtils.isEmpty(password)) {
      loginView.showPasswordEmp();
      return;
    }
    LoginRequest request = new LoginRequest();
    request.setUserPhone(username);
    request.setUserPassword(password);
    request.setDeviceSerialNumber(PrefConstant.getMsgDeviceID());
    request.setPlatform(AppConstant.PLATFORM_ANDROID);
//    request.setDeviceToken(MQQTUtils.getAndroidID(loginView.getContext()));
    request.setPhoneModel(DeviceUtils.getManufacturer());

    QtecEncryptInfo<LoginRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);

    loginView.showLoading();
    loginUseCase.execute(encryptInfo, new AppSubscriber<LoginResponse>(loginView) {

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        if (e instanceof PasswordErrThreeTimesException) {
          loginView.showPasswordErrorThreeTimes(e);
        } else if (e instanceof PasswordErrMoreTimesException) {
          loginView.showPasswordErrorMoreTimes(e);
        }
      }

      @Override
      protected void doNext(LoginResponse response) {

        SPUtils spUtils = SPUtils.getInstance(PrefConstant.SP_NAME);
        PrefConstant.setUserId(response.getId());
        spUtils.put(PrefConstant.SP_USER_HEAD_IMG, response.getUserHeadImg());
        spUtils.put(PrefConstant.SP_USER_NICK_NAME, response.getUserNickName());
        spUtils.put(PrefConstant.SP_USER_UNIQUE_KEY, response.getUserUniqueKey());
        spUtils.put(PrefConstant.SP_USER_PHONE, response.getUserPhone());
        spUtils.put(PrefConstant.SP_USER_PWD, EncryptUtils.encryptMD5ToString(password));

        //登录成功之后再绑定账户(消息中心)
//        System.out.println("AndroidApplication.getPushService() = " + AndroidApplication.getPushService());
//        System.out.println("PrefConstant.getUserPhone() = " + PrefConstant.getUserPhone());
//        AndroidApplication.getPushService().bindAccount(PrefConstant.getUserPhone(), new CommonCallback() {
//          @Override
//          public void onSuccess(String s) {
//            Log.i("message bingAccount", "pushService bind account success!");
//          }
//
//          @Override
//          public void onFailed(String s, String s1) {
//            Log.i("message bingAccount", "pushService bind account failed!");
//          }
//        });

        CloudUrlPath.setToken(response.getToken());
        PrefConstant.putAppToken(response.getToken());
        PrefConstant.putCloudUrl(CloudRestApiImpl.getApiPostConnection().getUrl());
        loginView.openMain(response);
      }
    });
  }


}
