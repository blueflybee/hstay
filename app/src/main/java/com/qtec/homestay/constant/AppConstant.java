package com.qtec.homestay.constant;

import android.os.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03null
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppConstant {
  public static final String PLATFORM_ANDROID = "ANDROID";
  private static Map<String, String> mLockStatus = new HashMap<>();
  private static Map<String, String> mFirmUpdateStatus = new HashMap<>();

  private static boolean gFirstAddFingerPrint = false;

  public static final String KEY_LOCK_STATUS_0 = "0";
  public static final String KEY_LOCK_STATUS_1 = "1";

  public static final String DEVICE_TYPE_ROUTER = "0";
  public static final String DEVICE_TYPE_LOCK = "1";
  public static final String DEVICE_TYPE_CAT = "2";
  public static final String DEVICE_TYPE_CAMERA = "3";

  public static final String IMG_UPLOADING = "IMG UPLOADING";
  public static final String IMG_CHECKING = "IMG CHECKING";
  public static final String IMG_CHECK_FAILED = "IMG CHECK FAILED";
  public static final String IMG_FLASHING = "IMG FLASHING";
  public static final String IMG_UPLOADING_FAILED = "IMG UPLOADING FAILED";

  public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
  public static final String APP_PATH = SDCARD_PATH + "Qtec" + File.separator;
  public static final String SAMBA_DOWNLOAD_PATH = APP_PATH + "QtecSamba" + File.separator; //Samba下载路径
  public static final String SAMBA_DOWNLOAD_CACHE_PATH = APP_PATH + "QtecSambaCache" + File.separator; //Samba预览缓存路径

  static {
    mLockStatus.put(KEY_LOCK_STATUS_0, "未使用");
    mLockStatus.put(KEY_LOCK_STATUS_1, "使用中");

    mFirmUpdateStatus.put(IMG_UPLOADING, "正在下载中...");
    mFirmUpdateStatus.put(IMG_CHECKING, "正在校验中...");
    mFirmUpdateStatus.put(IMG_CHECK_FAILED, "校验失败");
    mFirmUpdateStatus.put(IMG_FLASHING, "正在升级中...");
    mFirmUpdateStatus.put(IMG_UPLOADING_FAILED, "下载固件失败...");
  }


  public static String getLockStatus(String key) {
    return mLockStatus.get(key);
  }

  public static String getFirmUpdateStatus(String key) {
    return mFirmUpdateStatus.get(key);
  }


  public static boolean isgFirstAddFingerPrint() {
    return gFirstAddFingerPrint;
  }

  public static void setgFirstAddFingerPrint(boolean gFirstAddFingerPrint) {
    AppConstant.gFirstAddFingerPrint = gFirstAddFingerPrint;
  }

}
