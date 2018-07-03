package com.qtec.homestay.data.constant;


import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GlobleConstant {
  public static final String NO_ID = "no_id";
  private static String gDeviceId = NO_ID;
  private static String gKeyRepoId;
  private static String gDeviceType;

  public static String getgKeyRepoId() {
    return gKeyRepoId;
  }

  public static void setgKeyRepoId(String gKeyRepoId) {
    GlobleConstant.gKeyRepoId = gKeyRepoId;
  }
  public static String getgDeviceType() {
    return gDeviceType;
  }

  public static void setgDeviceType(String gDeviceType) {
    GlobleConstant.gDeviceType = gDeviceType;
  }

  public static String getgDeviceId() {
    return gDeviceId;
  }

  public static void setgDeviceId(String gDeviceId) {
    GlobleConstant.gDeviceId = gDeviceId;
  }

}
