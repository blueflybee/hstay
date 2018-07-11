package com.qtec.homestay.data.net;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 云端requestUrl
 *     version: 1.0
 * </pre>
 */
public class CloudUrlPath {


  private static String sToken;

  public static String getToken() {
    return sToken;
  }

  public static void setToken(String token) {
    CloudUrlPath.sToken = token;
  }

  public static final String METHOD_GET = "get";
  public static final String METHOD_POST = "post";

  public static final String PATH_LOGIN = "/routerservice/login/login";
  public static final String PATH_TRANSMIT = "/routerservice/router/dispatch";

  public static final String PATH_LOGIN_LIST = "/routerservice/loginlist/pwdlogin";
  public static final String PATH_UPLOAD_LOGCAT = "/routerservice/test/addandroidlog";

}
