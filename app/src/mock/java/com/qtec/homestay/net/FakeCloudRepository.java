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
package com.qtec.homestay.net;

import android.content.Context;


import com.google.gson.reflect.TypeToken;
import com.qtec.homestay.data.net.CloudUrlPath;
import com.qtec.homestay.data.utils.CloudConverter;
import com.qtec.homestay.data.utils.IConverter;
import com.qtec.homestay.domain.mapper.JsonMapper;
import com.qtec.homestay.domain.model.core.QtecEncryptInfo;
import com.qtec.homestay.domain.model.core.QtecResult;
import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.domain.model.mapp.rsp.TransmitResponse;
import com.qtec.homestay.domain.model.router.rsp.BindRouterToLockResponse;
import com.qtec.homestay.domain.params.IRequest;
import com.qtec.homestay.domain.repository.CloudRepository;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/08
 *     desc   : {@link FakeCloudRepository} for retrieving user data from fake remote repository.
 *     version: 1.0
 * </pre>
 */
@Singleton
public class FakeCloudRepository implements CloudRepository {

  private final JsonMapper mJsonMapper = new JsonMapper();
  private final IConverter mCloudConverter = new CloudConverter();
  private Context mContext;

  @Inject
  public FakeCloudRepository(Context context) {
    mContext = context;
  }

  @Override
  public Observable<LoginResponse> login(IRequest request) {
    return null;

//    return Observable.create(subscriber -> {
//
////      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_LOGIN, CloudUrlPath.METHOD_POST, 0, false);
//
//      LoginResponse response = new LoginResponse();
//      response.setToken("asd9f8as9df8asd7gadsfua90sd8f09asd");
//      response.setUserHeadImg("img");
//      response.setUserUniqueKey("unikey");
//      response.setUserNickName("jklolin");
//      response.setUserPhone("13866666666");
//      response.setId(Integer.MAX_VALUE);
//      QtecResult<LoginResponse> result = new QtecResult<>();
//      result.setData(response);
//      Type type = new TypeToken<QtecResult<LoginResponse>>() {
//      }.getType();
//
//      QtecResult<LoginResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
//      subscriber.onNext(qtecResult.getData());
//      subscriber.onComplete();
//    });

  }

  @Override
  public Observable<BindRouterToLockResponse> bindRouterToLockTrans(IRequest request) {
    return null;
//    return Observable
//        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
//          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 0, true);
//
//          //封装网关层
//          BindRouterToLockResponse response = new BindRouterToLockResponse();
//
//          QtecResult<BindRouterToLockResponse> routerQtecResult = new QtecResult<>();
//          routerQtecResult.setCode(0);
//          routerQtecResult.setMsg("ok");
//          routerQtecResult.setData(response);
//          //封装Transmit层
//          TransmitResponse<String> transmit = new TransmitResponse<>();
//          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));
//
//          //封装Cloud层
//          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
//          result.setData(transmit);
//
//          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
//          }.getType();
//
//          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 0, true, subscriber);
//
//          if (qtecResult.getCode() != 0) {
//            subscriber.onError(new IOException(qtecResult.getMsg()));
//          }
//          subscriber.onNext(qtecResult.getData());
//          //          subscriber.onCompleted();
//        })
//        .map(transmit -> {
//          Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
//          }.getType();
//          QtecResult<BindRouterToLockResponse> routerResult = logRouterResult(transmit, type);
//          return routerResult;
//        })
//        .flatMap(new Func1<QtecResult<BindRouterToLockResponse>, Observable<BindRouterToLockResponse>>() {
//          @Override
//          public Observable<BindRouterToLockResponse> call(final QtecResult<BindRouterToLockResponse> routerResult) {
//            return Observable.create(subscriber -> {
//              if (routerResult.getCode() != 0) {
//                subscriber.onError(new IOException(routerResult.getMsg()));
//              }
//              subscriber.onNext(routerResult.getData());
//              //              subscriber.onCompleted();
//            });
//          }
//        });
  }

}