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
package com.qtec.homestay.navigation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {
  // TODO: 18-7-11 modify

  @Inject
  public void Navigator() {}

  /**
   * 跳转到已有的界面
   * 并且清除它之后的所有已经启动的界面
   */
  public void navigateExistAndClearTop(Context context, Class<?> toClazz) {
    navigateExistAndClearTop(context, toClazz, null);
  }

  public void navigateExistAndClearTop(Context context, Class<?> toClazz, Intent intent) {
    if (context == null) return;
    if (intent == null) {
      intent = new Intent();
    }
    intent.setClass(context, toClazz);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(intent);
  }

  /**
   * 启动新界面到新栈
   * 并且清除其它所有界面
   */
  public void navigateNewAndClearTask(Context context, Class<?> toClazz) {
    navigateNewAndClearTask(context, toClazz, null);
  }

  public void navigateNewAndClearTask(Context context, Class<?> toClazz, Intent intent) {
    if (context == null) return;
    if (intent == null) {
      intent = new Intent();
    }
    intent.setClass(context, toClazz);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
  }

  /**
   * 正常启动界面
   * @param context
   * @param toClazz
   */
  public void navigateTo(Context context, Class<?> toClazz) {
    navigateTo(context, toClazz, null);
  }

  public void navigateTo(Context context, Class<?> toClazz, Intent intent) {
    if (context == null) return;
    if (intent == null) {
      intent = new Intent();
    }
    intent.setClass(context, toClazz);
    context.startActivity(intent);
  }

}
