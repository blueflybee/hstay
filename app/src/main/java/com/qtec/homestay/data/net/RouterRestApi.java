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


import com.qtec.homestay.domain.model.router.rsp.RouterStatusResponse;
import com.qtec.homestay.domain.model.router.rsp.RouterStatusResponse.Status;
import com.qtec.homestay.domain.params.IRequest;

import java.util.List;

import io.reactivex.Observable;

/**
 * CloudRestApi for retrieving data from the router.
 */
@Deprecated
public interface RouterRestApi {

  String URL_DEBUG = "http://router.qtec.cn/cgi-bin/json.cgi";
  String URL_FOR_SAMBA_IP = "router.qtec.cn";

  Observable<RouterStatusResponse<List<Status>>> getRouterStatus(IRequest request);

}
