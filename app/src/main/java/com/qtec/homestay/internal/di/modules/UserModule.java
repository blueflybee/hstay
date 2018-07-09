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
package com.qtec.homestay.internal.di.modules;


import com.qtec.homestay.domain.constant.CloudUseCaseComm;
import com.qtec.homestay.domain.executor.PostExecutionThread;
import com.qtec.homestay.domain.executor.ThreadExecutor;
import com.qtec.homestay.domain.interactor.UseCase;
import com.qtec.homestay.domain.interactor.cloud.Login;
import com.qtec.homestay.domain.repository.CloudRepository;
import com.qtec.homestay.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 * UserModule包括登录、注册、忘记密码、修改密码等用户模块的功能
 */
@Module
public class UserModule {

  public UserModule() {}

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.LOGIN)
  UseCase provideLoginUseCase(CloudRepository cloudRepository,
                              ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new Login(cloudRepository, threadExecutor, postExecutionThread);
  }

}