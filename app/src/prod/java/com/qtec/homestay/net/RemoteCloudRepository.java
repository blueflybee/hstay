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


import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.homestay.data.constant.ExceptionConstant;
import com.qtec.homestay.data.net.CloudRestApi;
import com.qtec.homestay.data.net.CloudRestApiImpl;
import com.qtec.homestay.domain.mapper.JsonMapper;
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
 *     desc   : {@link RemoteCloudRepository} for retrieving user data from remote repository.
 *     version: 1.0
 * </pre>
 */
@Singleton
public class RemoteCloudRepository implements CloudRepository {

  private final CloudRestApi cloudRestApi;
  private JsonMapper mJsonMapper;

  @Inject
  public RemoteCloudRepository(CloudRestApiImpl cloudRestApi) {
    this.cloudRestApi = cloudRestApi;
    this.mJsonMapper = new JsonMapper();
  }

  @Override
  public Observable<LoginResponse> login(IRequest request) {
    return cloudRestApi.login(request);
  }

  @Override
  public Observable<BindRouterToLockResponse> bindRouterToLockTrans(IRequest request) {
    return Observable.create(emitter -> cloudRestApi.bindRouterToLockTrans(request)
        .map(transmitResponse -> {
          Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
          }.getType();
          QtecResult<BindRouterToLockResponse> qtecResult = logRouterResult(transmitResponse, type);
          if (qtecResult.getCode() != 0) {
            emitter.onError(new IOException(ExceptionConstant.convertCodeToMsg(qtecResult.getCode(), qtecResult.getMsg())));
            return null;
          } else {
            emitter.onNext(qtecResult.getData());
            return qtecResult.getData();
          }
        }));
  }

//  @Override
//  public Observable<TransmitResponse<String>> bindRouterToLockTrans(IRequest request) {
//    return cloudRestApi.bindRouterToLockTrans(request)
//        .map(new Function<TransmitResponse<String>, TransmitResponse<String>>() {
//          @Override
//          public TransmitResponse<String> apply(TransmitResponse<String> transmit) throws Exception {
//                      Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
//          }.getType();
//          return (QtecResult<BindRouterToLockResponse>) logRouterResult(transmit, type);
//          }
//        })
//        .flatMap(new Function<TransmitResponse<String>, ObservableSource<?>>() {
//          @Override
//          public ObservableSource<?> apply(TransmitResponse<String> stringTransmitResponse) throws Exception {
//            return null;
//          }
//        })
//  }


//  @Override
//  public Observable<BindRouterToLockResponse> bindRouterToLockTrans(IRequest request) {
//    return cloudRestApi.bindRouterToLockTrans(request)
//        .map(new Function<TransmitResponse<String>, QtecResult<BindRouterToLockResponse>>() {
//          @Override
//          public QtecResult<BindRouterToLockResponse> apply(TransmitResponse<String> transmit) throws Exception {
//            Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
//            }.getType();
//            return (QtecResult<BindRouterToLockResponse>) RemoteCloudRepository.this.logRouterResult(transmit, type);
//          }
//        })
//        .flatMap(new Func1<QtecResult<BindRouterToLockResponse>, Observable<BindRouterToLockResponse>>() {
//          @Override
//          public Observable<BindRouterToLockResponse> call(final QtecResult<BindRouterToLockResponse> routerQtecResult) {
//            return Observable.create(subscriber -> {
//              if (routerQtecResult.getCode() != 0) {
//                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
//              } else {
//                subscriber.onNext(routerQtecResult.getData());
//              }
//            });
//          }
//        });
//  }

  private QtecResult logRouterResult(TransmitResponse<String> transmit, Type type) {
    try {
      String routerEncryptInfo = transmit.getEncryptInfo();
      Logger.t("router-response").json(routerEncryptInfo);
      return (QtecResult) mJsonMapper.fromJson(routerEncryptInfo, type);
    } catch (Exception e) {
      e.printStackTrace();
      QtecResult result = new QtecResult();
      result.setMsg("网关数据格式异常");
      result.setCode(-1000);
      return result;
    }

  }
}