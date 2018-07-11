package com.qtec.homestay.data.net;

import static com.qtec.homestay.data.net.ApiResult.ApiResultType.SUCCESS;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/07/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ApiResult<T> {

  private ApiResultType mResultType;
  private Exception mException;
  private T data;

  ApiResult() {
  }

  void setResultType(ApiResultType resultType) {
    mResultType = resultType;
  }

  public Exception getException() {
    return mException;
  }

  void setException(Exception exception) {
    mException = exception;
  }

  T getData() {
    return data;
  }

  void setData(T data) {
    this.data = data;
  }

  boolean success() {
    return mResultType == SUCCESS;
  }

  enum ApiResultType{
    SUCCESS,
    FAILED
  }
}
