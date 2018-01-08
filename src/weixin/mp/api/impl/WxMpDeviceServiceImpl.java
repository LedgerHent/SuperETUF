package weixin.mp.api.impl;

import weixin.common.exception.WxErrorException;
import weixin.mp.api.WxMpDeviceService;
import weixin.mp.api.WxMpService;
import weixin.mp.bean.device.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by keungtung on 10/12/2016.
 */
public class WxMpDeviceServiceImpl implements WxMpDeviceService {
  private static final String API_URL_PREFIX = "https://api.weixin.qq.com/device";
  @SuppressWarnings("unused")
private static Logger log = LoggerFactory.getLogger(WxMpMenuServiceImpl.class);

  private WxMpService wxMpService;

  WxMpDeviceServiceImpl(WxMpService wxMpService) {
    this.wxMpService = wxMpService;
  }

  @Override
  public TransMsgResp transMsg(WxDeviceMsg msg) throws WxErrorException {
    String url = API_URL_PREFIX + "/transmsg";
    String response = this.wxMpService.post(url,msg.toJson());
    return TransMsgResp.fromJson(response);
  }

  @Override
  public WxDeviceQrCodeResult getQrCode(String productId) throws WxErrorException {
    String url = API_URL_PREFIX + "/getqrcode";
    String response = this.wxMpService.get(url, "product_id=" + productId);
    return WxDeviceQrCodeResult.fromJson(response);
  }

  @Override
  public WxDeviceAuthorizeResult authorize(WxDeviceAuthorize wxDeviceAuthorize) throws WxErrorException {
    String url = API_URL_PREFIX + "/authorize_device";
    String response = this.wxMpService.post(url,wxDeviceAuthorize.toJson());
    return WxDeviceAuthorizeResult.fromJson(response);
  }

  @Override
  public WxDeviceBindResult bind(WxDeviceBind wxDeviceBind) throws WxErrorException {
    String url = API_URL_PREFIX + "/bind";
    String response = this.wxMpService.post(url,wxDeviceBind.toJson());
    return WxDeviceBindResult.fromJson(response);
  }

  @Override
  public WxDeviceBindResult compelBind(WxDeviceBind wxDeviceBind) throws WxErrorException {
    String url = API_URL_PREFIX + "/compel_bind";
    String response = this.wxMpService.post(url,wxDeviceBind.toJson());
    return WxDeviceBindResult.fromJson(response);
  }

  @Override
  public WxDeviceBindResult unbind(WxDeviceBind wxDeviceBind) throws WxErrorException {
    String url = API_URL_PREFIX + "/unbind?";
    String response = this.wxMpService.post(url, wxDeviceBind.toJson());
    return WxDeviceBindResult.fromJson(response);
  }

  @Override
  public WxDeviceBindResult compelUnbind(WxDeviceBind wxDeviceBind) throws WxErrorException {
    String url = API_URL_PREFIX + "/compel_unbind?";
    String response = this.wxMpService.post(url, wxDeviceBind.toJson());
    return WxDeviceBindResult.fromJson(response);
  }
}

