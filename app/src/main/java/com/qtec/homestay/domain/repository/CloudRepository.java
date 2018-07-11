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
package com.qtec.homestay.domain.repository;


import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.domain.model.mapp.rsp.TransmitResponse;
import com.qtec.homestay.domain.model.router.rsp.BindRouterToLockResponse;
import com.qtec.homestay.domain.params.IRequest;

import io.reactivex.Observable;
/**
 * App与量子云端的服务接口
 * Interface that represents a CloudRepository for getting related data.
 */
public interface CloudRepository {

  Observable<LoginResponse> login(IRequest request);

}
