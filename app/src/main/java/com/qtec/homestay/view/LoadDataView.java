/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.qtec.homestay.view;

import android.content.Context;

import com.qtec.homestay.domain.exception.ErrorBundle;

/**
 * Interface representing a View that will use to load data.
 */
public interface LoadDataView {
  /**
   * Show a view with a progress bar indicating a loading process.
   */
  void showLoading();

  /**
   * Hide a loading view.
   */
  void hideLoading();

  /**
   * Show a retry view in case of an error when retrieving data.
   */
  //void showRetry();

  /**
   * Hide a retry view shown if there was an error when retrieving data.
   */
  //void hideRetry();

  /**
   * Show an error message
   *
   * @param bundle A err form data
   */
  void onError(ErrorBundle bundle);

  /**
   * Get a {@link Context}.
   */
  Context getContext();

  void showLoginInvalid();


}
