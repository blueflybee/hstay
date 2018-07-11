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
package com.qtec.homestay.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.homestay.data.exception.CloudNoSuchLockException;
import com.qtec.homestay.data.exception.LoginInvalidException;
import com.qtec.homestay.data.exception.NetworkConnectionException;
import com.qtec.homestay.data.exception.PasswordErrMoreTimesException;
import com.qtec.homestay.data.exception.PasswordErrThreeTimesException;
import com.qtec.homestay.data.utils.CloudConverter;
import com.qtec.homestay.data.utils.IConverter;
import com.qtec.homestay.domain.mapper.JsonMapper;
import com.qtec.homestay.domain.model.core.QtecEncryptInfo;
import com.qtec.homestay.domain.model.core.QtecResult;
import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.domain.params.IRequest;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

import static com.qtec.homestay.data.net.ApiResult.ApiResultType.FAILED;
import static com.qtec.homestay.data.net.ApiResult.ApiResultType.SUCCESS;

/**
 * {@link CloudRestApi} implementation for retrieving data from the network.
 */
@SuppressWarnings("unchecked")
@Singleton
public class CloudRestApiImpl implements CloudRestApi {
  //PASSWORD_ERROR_THREE_TIMES("输入密码连续错误三次以上,是否重置密码",10080),
  //PASSWORD_ERROR_MORE_TIMES("短时间内登录次数过多,请稍后重试",10081)
  private static final int PASSWORD_ERROR_THREE_TIMES = 10080;
  private static final int PASSWORD_ERROR_MORE_TIMES = 10081;
  private static final int NO_SUCH_LOCK = 10;
  private static final int LOGIN_INVALID = 10086;

  private static IPostConnection apiPostConnection;
  private final Context context;

  private final IConverter mCloudConverter = new CloudConverter();

  /**
   * Constructor of the class
   *
   * @param context {@link Context}.
   */
  @Inject
  public CloudRestApiImpl(Context context) {

    if (context == null) {
      throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
    }
    this.context = context;
  }

  public static IPostConnection getApiPostConnection() {
    return apiPostConnection;
  }

  public static void setApiPostConnection(IPostConnection apiPostConnection) {
    CloudRestApiImpl.apiPostConnection = apiPostConnection;
  }

  @Override
  public Observable<LoginResponse> login(final IRequest request) {
    return Observable.create(emitter -> {
      if (isThereInternetConnection()) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_LOGIN);
        Type type = new TypeToken<QtecResult<LoginResponse>>() {
        }.getType();
        ApiResult<LoginResponse> apiResult = requestService(encryptInfo, type);
        if (apiResult.success()) {
          emitter.onNext(apiResult.getData());
          emitter.onComplete();
        } else {
          emitter.onError(apiResult.getException());
        }
      } else {
        emitter.onError(new NetworkConnectionException("没有网络连接，请先连接网络"));
      }
    });
  }

  private ApiResult requestService(IRequest request, Type type) {
    ((QtecEncryptInfo) request).setToken(CloudUrlPath.getToken());
    ApiResult apiResult = new ApiResult();
    JsonMapper jsonMapper = new JsonMapper();
    String requestMsg;
    try {
      requestMsg = jsonMapper.toJson(request);
    } catch (JsonIOException e) {
      e.printStackTrace();
      apiResult.setResultType(FAILED);
      apiResult.setException(new JsonIOException("请求数据解析异常"));
      return apiResult;
    }

    Logger.t("c_request").json(requestMsg);

    String result;
    try {
      result = apiPostConnection.requestSyncCall(requestMsg, ((QtecEncryptInfo) request).getRequestUrl(), ((QtecEncryptInfo) request));
    } catch (IOException e) {
      e.printStackTrace();
      apiResult.setResultType(FAILED);
      apiResult.setException(e);
      return apiResult;
    }

    Logger.t("c_response").json(result);

    QtecResult qtecResult;
    try {
      qtecResult = (QtecResult) jsonMapper.fromJson(result, type);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      Logger.t("c_response_exp_json").json(result);
      apiResult.setResultType(FAILED);
      apiResult.setException(new JsonSyntaxException("应答数据解析异常"));
      return apiResult;
    }

    if (qtecResult == null) {
      apiResult.setResultType(FAILED);
      apiResult.setException(new IOException("应答数据为空"));
      return apiResult;
    }

    if (qtecResult.getCode() == -1) {
      apiResult.setResultType(FAILED);
      apiResult.setException(new IOException("异常的服务 code:-1"));
      return apiResult;
    }

    if (qtecResult.getCode() == LOGIN_INVALID) {
      apiResult.setResultType(FAILED);
      apiResult.setException(new LoginInvalidException(qtecResult.getMsg()));
      return apiResult;
    }

    if (qtecResult.getCode() == PASSWORD_ERROR_THREE_TIMES) {
      apiResult.setResultType(FAILED);
      apiResult.setException(new PasswordErrThreeTimesException(qtecResult.getMsg(), PASSWORD_ERROR_THREE_TIMES));
      return apiResult;
    }

    if (qtecResult.getCode() == PASSWORD_ERROR_MORE_TIMES) {
      apiResult.setResultType(FAILED);
      apiResult.setException(new PasswordErrMoreTimesException(qtecResult.getMsg(), PASSWORD_ERROR_MORE_TIMES));
      return apiResult;
    }

    if (qtecResult.getCode() == NO_SUCH_LOCK) {
      apiResult.setResultType(FAILED);
      apiResult.setException(new CloudNoSuchLockException(qtecResult.getMsg(), NO_SUCH_LOCK));
      return apiResult;
    }

    if (qtecResult.getCode() != 0) {
      String msg = TextUtils.isEmpty(qtecResult.getMsg()) ? "未知服务异常" : qtecResult.getMsg();
      apiResult.setResultType(FAILED);
      apiResult.setException(new IOException(msg + " code:" + qtecResult.getCode()));
      return apiResult;
    }

    Object data = qtecResult.getData();
    if (data == null) {
      apiResult.setResultType(FAILED);
      apiResult.setException(new IOException("应答数据data部分为空"));
      return apiResult;
    }

    apiResult.setResultType(SUCCESS);
    apiResult.setException(null);
    apiResult.setData(data);
    return apiResult;
  }

  /**
   * Checks if the device has any active internet connection.
   *
   * @return true device with internet connection, otherwise false.
   */
  private boolean isThereInternetConnection() {
    boolean isConnected;
    ConnectivityManager connectivityManager =
        (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
    return isConnected;
  }
}
