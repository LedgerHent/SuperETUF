package weixin.mp.util.http;

import weixin.common.bean.result.WxError;
import weixin.common.exception.WxErrorException;
import weixin.common.util.http.RequestExecutor;
import weixin.common.util.http.Utf8ResponseHandler;
import weixin.common.util.json.WxGsonBuilder;
import weixin.mp.bean.material.WxMpMaterialVideoInfoResult;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MaterialVideoInfoRequestExecutor implements RequestExecutor<WxMpMaterialVideoInfoResult, String> {

  public MaterialVideoInfoRequestExecutor() {
    super();
  }

  @Override
  public WxMpMaterialVideoInfoResult execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String materialId) throws WxErrorException, IOException {
    HttpPost httpPost = new HttpPost(uri);
    if (httpProxy != null) {
      RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
      httpPost.setConfig(config);
    }

    Map<String, String> params = new HashMap<>();
    params.put("media_id", materialId);
    httpPost.setEntity(new StringEntity(WxGsonBuilder.create().toJson(params)));
    try(CloseableHttpResponse response = httpclient.execute(httpPost)){
      String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
      WxError error = WxError.fromJson(responseContent);
      if (error.getErrorCode() != 0) {
        throw new WxErrorException(error);
      } else {
        return WxMpMaterialVideoInfoResult.fromJson(responseContent);
      }
    }finally {
      httpPost.releaseConnection();
    }
  }

}
