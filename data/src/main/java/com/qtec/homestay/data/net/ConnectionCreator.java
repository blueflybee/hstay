package com.qtec.homestay.data.net;

/**
 * @author shaojun
 * @name ConnectionCreator
 * @package com.fernandocejas.android10.sample.data.net
 * @date 15-12-9
 */
public class ConnectionCreator {

  public static final String CLOUD_CONNECTION = "cloud_connection";
  public static final String ROUTER_CONNECTION = "router_connection";

  private static IPostConnection mConnection;

  public static IPostConnection create(String which, String url) {
    if (which.equalsIgnoreCase(CLOUD_CONNECTION)) {
      return new CloudApiConnection(url);
    } else if (which.equalsIgnoreCase(ROUTER_CONNECTION)) {
      return new RouterApiConnection(url);
    } else {
      return null;
    }
  }
}
