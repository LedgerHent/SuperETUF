package weixin.common.api;

import weixin.common.exception.WxErrorException;

/**
 * WxErrorException处理器
 */
public interface WxErrorExceptionHandler {

  void handle(WxErrorException e);

}
