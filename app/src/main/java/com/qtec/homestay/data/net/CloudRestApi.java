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

import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.domain.model.mapp.rsp.TransmitResponse;
import com.qtec.homestay.domain.params.IRequest;

import io.reactivex.Observable;

/**
 * CloudRestApi for retrieving data from the cloud.
 */
public interface CloudRestApi {

  String Liu_Li_Hua = "http://192.168.90.58:10082";
  String Ren_Xiao_Ting = "http://192.168.92.55:10086";
  String URL_DEBUG_RELEASE = "http://192.168.92.59:10086";
  String URL_GATEWAY_3_CARETEC = "https://gateway.3caretec.com/";

  /**
   * Retrieves an {@link Observable} which will emit a {@link LoginResponse}.
   */
  Observable<LoginResponse> login(IRequest request);

}
