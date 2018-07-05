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


import com.qtec.homestay.data.utils.IConverter;
import com.qtec.homestay.data.utils.RouterConverter;
import com.qtec.homestay.domain.mapper.JsonMapper;
import com.qtec.homestay.domain.repository.RouterRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/08
 *     desc   : {@link FakeRouterRepository} for retrieving user data from fake remote repository.
 *     version: 1.0
 * </pre>
 */
@Singleton
public class FakeRouterRepository implements RouterRepository {

  private final JsonMapper mJsonMapper = new JsonMapper();
  private final IConverter mRouterConverter = new RouterConverter();
  private Context mContext;

  @Inject
  public FakeRouterRepository(Context context) {
    mContext = context;
  }

}
