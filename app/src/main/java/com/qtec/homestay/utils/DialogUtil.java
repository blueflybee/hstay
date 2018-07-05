/**
 *
 */
package com.qtec.homestay.utils;


import android.app.Dialog;
import android.content.Context;

import com.qtec.homestay.view.component.CustomProgressDialog;

/**
 * Dialog 工具类
 */
public class DialogUtil {

  private static Dialog mProgressDialog = null;

  /**
   * 显示进度对话框（可取消）
   */
  public static void showProgress(Context context) {
    try {
      hideProgress();
      mProgressDialog = CustomProgressDialog.createDialog(context);
      mProgressDialog.setCancelable(true);
      mProgressDialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void hideProgress() {
    try {
      if (mProgressDialog != null) mProgressDialog.dismiss();
      mProgressDialog = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
