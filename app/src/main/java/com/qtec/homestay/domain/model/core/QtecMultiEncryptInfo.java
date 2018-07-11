package com.qtec.homestay.domain.model.core;


import com.qtec.homestay.domain.model.mapp.req.TransmitRequest;
import com.qtec.homestay.domain.params.IRequest;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03
 *     desc   : 多端并发请求，封装多个request
 *     version: 1.0
 * </pre>
 */
public class QtecMultiEncryptInfo implements IRequest {

  private QtecEncryptInfo routerEncryptInfo;
  private QtecEncryptInfo<TransmitRequest> cloudEncryptInfo;

  public QtecEncryptInfo getRouterEncryptInfo() {
    return routerEncryptInfo;
  }

  public void setRouterEncryptInfo(QtecEncryptInfo routerEncryptInfo) {
    this.routerEncryptInfo = routerEncryptInfo;
  }

  public QtecEncryptInfo<TransmitRequest> getCloudEncryptInfo() {
    return cloudEncryptInfo;
  }

  public void setCloudEncryptInfo(QtecEncryptInfo<TransmitRequest> cloudEncryptInfo) {
    this.cloudEncryptInfo = cloudEncryptInfo;
  }

}
