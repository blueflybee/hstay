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
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
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
import com.qtec.homestay.domain.model.mapp.rsp.TransmitResponse;
import com.qtec.homestay.domain.params.IRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * {@link CloudRestApi} implementation for retrieving data from the network.
 */
@Singleton
public class CloudRestApiImpl implements CloudRestApi {

  //  /** *输入密码连续错误三次以上 */ PASSWORD_ERROR_THREE_TIMES("输入密码连续错误三次以上,是否重置密码",10080),
//      /** *短时间内登录次数过多 */ PASSWORD_ERROR_MORE_TIMES("短时间内登录次数过多,请稍后重试",10081)

  public static final int PASSWORD_ERROR_THREE_TIMES = 10080;
  public static final int PASSWORD_ERROR_MORE_TIMES = 10081;
  public static final int NO_SUCH_LOCK = 10;
  public static final String ACTION_ROUTER_KEY_INVALID = "android.intent.action.KeyInvalidReceiver";
  public static final int LOGIN_INVALID = 10086;

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
      QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
      encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
      encryptInfo.setRequestUrl(CloudUrlPath.PATH_LOGIN);
      Type type = new TypeToken<QtecResult<LoginResponse>>() {
      }.getType();
      LoginResponse response = (LoginResponse) requestService(encryptInfo, type, emitter, 0, false);
//                apiPostConnection.setSessionId(response.getSessionId());
      emitter.onNext(response);
      emitter.onComplete();
    });
  }

  @Override
  public Observable<TransmitResponse<String>> bindRouterToLockTrans(IRequest request) {
    return createTransmitObservable(request, 0);
  }

  @NonNull
  private Observable<TransmitResponse<String>> createTransmitObservable(final IRequest request, final int encryption) {
    return Observable.create(emitter -> {
      QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
      encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
      encryptInfo.setRequestUrl(CloudUrlPath.PATH_TRANSMIT);
      Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
      }.getType();
      TransmitResponse<String> response =
          (TransmitResponse<String>) requestService(encryptInfo, type, emitter, encryption, true);
      if (response != null) {
        emitter.onNext(response);
      }
    });
  }

  private Object requestService(IRequest request, Type type, ObservableEmitter<?> emitter, int encryption, boolean isTrans) {

    ((QtecEncryptInfo) request).setToken(CloudUrlPath.getToken());

    Object data = null;

    checkNetworkConnection(emitter);

    JsonMapper jsonMapper = new JsonMapper();

    String requestMsg = null;

    try {
      requestMsg = jsonMapper.toJson(request);
    } catch (JsonIOException e) {
      e.printStackTrace();
      emitter.onError(new JsonIOException("请求数据不完整"));
    }

    if (!CloudUrlPath.PATH_UPLOAD_LOGCAT.equals(((QtecEncryptInfo) request).getRequestUrl())) {
      Logger.t("cloud-none-request").json(requestMsg);
    }

    if (isTrans) {
      requestMsg = mCloudConverter.convertTo(requestMsg, encryption);
      Logger.t("cloud-sm4-request").json(requestMsg);
    }

    String result = null;
    try {
      result = apiPostConnection.requestSyncCall(requestMsg, ((QtecEncryptInfo) request).getRequestUrl(), ((QtecEncryptInfo) request));
    } catch (IOException e) {
      e.printStackTrace();
      emitter.onError(e);
    }

    if (isTrans) {
      Logger.t("cloud-sm4-response").json(result);
      result = mCloudConverter.convertFrom(result, null);
      if (IConverter.EXP_KEY_INVALID.equals(result)) {
        Intent intent = new Intent();
        intent.setAction(ACTION_ROUTER_KEY_INVALID);
        context.sendBroadcast(intent);
      }
    }

    if (!CloudUrlPath.PATH_UPLOAD_LOGCAT.equals(((QtecEncryptInfo) request).getRequestUrl())) {
      Logger.t("cloud-none-response").json(result);
    }

    preHandleLoginInvalid(emitter, result);

    QtecResult qtecResult = null;
    try {
      qtecResult = (QtecResult) jsonMapper.fromJson(result, type);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      Logger.t("response_exp_json").json(result);
      emitter.onError(new IOException("返回数据不完整"));
    }

    if (qtecResult == null) {
      emitter.onError(new IOException("数据异常"));
    }

    if (qtecResult.getCode() == -1) {
      String msg = "服务异常";
      emitter.onError(new IOException(msg));
    }

    if (qtecResult.getCode() == LOGIN_INVALID) {
      emitter.onError(new LoginInvalidException(qtecResult.getMsg()));
    }

    if (qtecResult.getCode() == PASSWORD_ERROR_THREE_TIMES) {
      emitter.onError(new PasswordErrThreeTimesException(qtecResult.getMsg(), PASSWORD_ERROR_THREE_TIMES));
    }

    if (qtecResult.getCode() == PASSWORD_ERROR_MORE_TIMES) {
      emitter.onError(new PasswordErrMoreTimesException(qtecResult.getMsg(), PASSWORD_ERROR_MORE_TIMES));
    }

    if (qtecResult.getCode() == NO_SUCH_LOCK) {
      emitter.onError(new CloudNoSuchLockException(qtecResult.getMsg(), NO_SUCH_LOCK));
    }

    if (qtecResult.getCode() != 0) {
      String msg = TextUtils.isEmpty(qtecResult.getMsg()) ? "业务异常" : qtecResult.getMsg();
      emitter.onError(new IOException(msg));
    }

    data = qtecResult.getData();
    if (data == null) {
      emitter.onError(new IOException("data数据不完整"));
    }

    return data;
  }

  private void preHandleLoginInvalid(ObservableEmitter<?> subscriber, String result) {
    try {
      JSONObject jsonObject = new JSONObject(result);
      int code = jsonObject.getInt("code");
      String msg = jsonObject.getString("msg");
      if (code == LOGIN_INVALID) {
        subscriber.onError(new LoginInvalidException(msg));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void checkNetworkConnection(ObservableEmitter<?> emitter) {
    if (!isThereInternetConnection()) {
      emitter.onError(new NetworkConnectionException("没有网络连接，请先连接网络"));
    }
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
