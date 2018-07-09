package com.qtec.homestay.utils;

import com.qtec.homestay.data.constant.GlobleConstant;
import com.qtec.homestay.domain.model.core.QtecEncryptInfo;
import com.qtec.homestay.domain.model.core.QtecMultiEncryptInfo;
import com.qtec.homestay.domain.model.mapp.req.TransmitRequest;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class EncryptInfoFactory {

  public static QtecMultiEncryptInfo createMultiEncryptInfo(Object routerData, String path, String method) {
    QtecEncryptInfo routerEncryptInfo = new QtecEncryptInfo<>();
    routerEncryptInfo.setRequestUrl(path);
    routerEncryptInfo.setMethod(method);
    routerEncryptInfo.setData(routerData);

    //build transmit
    TransmitRequest<QtecEncryptInfo> transmit = new TransmitRequest<>();
    transmit.setRouterSerialNo(GlobleConstant.getgDeviceId());
    transmit.setEncryptInfo(routerEncryptInfo);

    //build cloud EncryptInfo
    QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
    cloudEncryptInfo.setData(transmit);

    QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
    multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
    multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

    return multiEncryptInfo;
  }

  public static QtecMultiEncryptInfo createMultiEncryptInfo(String routerId, Object routerData, String path, String method) {
    QtecEncryptInfo routerEncryptInfo = new QtecEncryptInfo<>();
    routerEncryptInfo.setRequestUrl(path);
    routerEncryptInfo.setMethod(method);
    routerEncryptInfo.setData(routerData);

    //build transmit
    TransmitRequest<QtecEncryptInfo> transmit = new TransmitRequest<>();
    transmit.setRouterSerialNo(routerId);
    transmit.setEncryptInfo(routerEncryptInfo);

    //build cloud EncryptInfo
    QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
    cloudEncryptInfo.setData(transmit);

    QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
    multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
    multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

    return multiEncryptInfo;
  }

  public static QtecEncryptInfo createEncryptInfo(Object request) {
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    return encryptInfo;
  }

}
