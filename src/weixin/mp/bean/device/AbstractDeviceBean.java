package weixin.mp.bean.device;

import weixin.common.util.json.WxGsonBuilder;

/**
 * Created by keungtung on 14/12/2016.
 */
public abstract class AbstractDeviceBean {
  public String toJson() {
    return WxGsonBuilder.create().toJson(this);
  }
}
