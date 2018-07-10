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
import android.text.TextUtils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.homestay.data.constant.ExceptionConstant;
import com.qtec.homestay.data.exception.NetworkConnectionException;
import com.qtec.homestay.data.utils.IConverter;
import com.qtec.homestay.data.utils.RouterConverter;
import com.qtec.homestay.domain.mapper.JsonMapper;
import com.qtec.homestay.domain.model.core.QtecEncryptInfo;
import com.qtec.homestay.domain.model.core.QtecResult;
import com.qtec.homestay.domain.model.router.rsp.RouterStatusResponse;
import com.qtec.homestay.domain.model.router.rsp.RouterStatusResponse.Status;
import com.qtec.homestay.domain.params.IRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * {@link CloudRestApi} implementation for retrieving data from the network.
 */
@SuppressWarnings("unchecked")
@Singleton
public class RouterRestApiImpl implements RouterRestApi {

  public static final String KEY_INVALID_ROUTER_ID = "key_invalid_router_id";
  private static IPostConnection apiPostConnection;
  private final Context context;

  private IConverter mRouterConverter;

  /**
   * Constructor of the class
   *
   * @param context {@link Context}.
   */
  @Inject
  public RouterRestApiImpl(Context context) {

    if (context == null) {
      throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
    }
    this.mRouterConverter = new RouterConverter();
    this.context = context;

  }

  public static IPostConnection getApiPostConnection() {
    return apiPostConnection;
  }

  public static void setApiPostConnection(IPostConnection apiPostConnection) {
    RouterRestApiImpl.apiPostConnection = apiPostConnection;
  }


  @Override
  public Observable<RouterStatusResponse<List<Status>>> getRouterStatus(final IRequest request) {
    return Observable.create(emitter -> {
      QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
      encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_ROUTER_STATUS);
      Type type = new TypeToken<QtecResult<RouterStatusResponse<List<Status>>>>() {
      }.getType();
      RouterStatusResponse<List<Status>> response = (RouterStatusResponse<List<Status>>) requestService(encryptInfo, type, emitter, 1);
      if (response != null) {
        emitter.onNext(response);
      }
    });

  }


  private Object requestService(IRequest request, Type type, ObservableEmitter<?> subscriber, int encryption) {

//    ((QtecEncryptInfo) request).setToken(CloudUrlPath.getToken());
    Object data;
    checkNetworkConnection(subscriber);

    JsonMapper jsonMapper = new JsonMapper();

    String requestMsg = null;

    try {

      requestMsg = jsonMapper.toJson(request);

    } catch (JsonIOException e) {
      e.printStackTrace();
      subscriber.onError(new JsonIOException("请求数据不完整"));
    }

    Logger.t("router-none-request").json(requestMsg);

    String encryptReq = mRouterConverter.convertTo(requestMsg, encryption);
    Logger.t("router-sm4-request").json(encryptReq);


    String responseJson = null;
    try {
      responseJson = apiPostConnection.requestSyncCall(encryptReq, "", (QtecEncryptInfo) request);
      System.out.println("raw responseJson = " + responseJson);
    } catch (IOException e) {
      e.printStackTrace();
      subscriber.onError(e);
    }

    Logger.t("router-sm4-response").json(responseJson);

    String decryptResult = mRouterConverter.convertFrom(responseJson, ((QtecEncryptInfo) request).getRequestUrl());
    if (IConverter.EXP_ACCESS_DEVICE_NOT_CONNECTED.equals(decryptResult)) {
      subscriber.onError(new IOException("未链接当前网关的wifi或当前网关服务异常，尝试转发"));
    }

    if (!TextUtils.isEmpty(decryptResult) && decryptResult.contains(IConverter.EXP_KEY_INVALID)) {
      Intent intent = new Intent();
      if (decryptResult.contains(":")) {
        String[] result = decryptResult.split(":");
        intent.putExtra(KEY_INVALID_ROUTER_ID, result[1]);
      }
      intent.setAction(CloudRestApiImpl.ACTION_ROUTER_KEY_INVALID);
      context.sendBroadcast(intent);
    }

    Logger.t("router-none-response").json(decryptResult);

    QtecResult qtecResult = null;
    try {
      qtecResult = (QtecResult) jsonMapper.fromJson(decryptResult, type);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      Logger.t("router-response").d(decryptResult);
      subscriber.onError(new IOException("返回非法数据格式"));
    }

    if (qtecResult == null) {
      subscriber.onError(new IOException("数据异常"));
    }

    if (qtecResult.getCode() != 0) {
      String msg = TextUtils.isEmpty(qtecResult.getMsg()) ? "业务异常" : ExceptionConstant.convertCodeToMsg(qtecResult.getCode(), qtecResult.getMsg());
      subscriber.onError(new IOException(msg));
    }

    data = qtecResult.getData();
    if (data == null) {
      subscriber.onError(new IOException("data数据不完整"));
    }

    return data;
  }



  private void checkNetworkConnection(ObservableEmitter<?> subscriber) {
    if (!isThereInternetConnection()) {
      subscriber.onError(new NetworkConnectionException());
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
