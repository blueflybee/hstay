package com.qtec.homestay.data.net;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 网关requestUrl
 *     version: 1.0
 * </pre>
 */
public class RouterUrlPath {

  private static String sToken;

  public static String getToken() {
    return sToken;
  }

  public static void setToken(String token) {
    RouterUrlPath.sToken = token;
  }

  public static final String METHOD_GET = "get";
  public static final String METHOD_POST = "post";
  public static final String METHOD_PUT = "put";
  public static final String METHOD_DELETE = "delete";

  public static final String PATH_SEARCH_ROUTER = "routerinfo";
  public static final String PATH_GET_ROUTER_STATUS = "routerstatus";
  public static final String PATH_FIRST_GET_KEY = "firstkeyrequest";
}
