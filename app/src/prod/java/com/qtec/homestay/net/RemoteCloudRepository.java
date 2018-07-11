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


import com.orhanobut.logger.Logger;
import com.qtec.homestay.data.net.CloudRestApi;
import com.qtec.homestay.data.net.CloudRestApiImpl;
import com.qtec.homestay.domain.mapper.JsonMapper;
import com.qtec.homestay.domain.model.core.QtecResult;
import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.domain.model.mapp.rsp.TransmitResponse;
import com.qtec.homestay.domain.params.IRequest;
import com.qtec.homestay.domain.repository.CloudRepository;

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
@SuppressWarnings("unchecked")
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

}