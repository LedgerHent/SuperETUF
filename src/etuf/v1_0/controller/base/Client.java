package etuf.v1_0.controller.base;

import java.text.MessageFormat;

import etuf.v1_0.common.Common;
import etuf.v1_0.common.HttpHelper;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.Request_Base;
import etuf.v1_0.model.base.Response_Base;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;

public abstract class Client<T extends Request_Base, K extends Response_Base>
		extends ClientAbs<T, K> {

	/**
	 * 客户端请求唯一入口
	 * 
	 * @param serverUrl
	 * @param reqStruct
	 * @param clazz
	 * @return
	 */
	protected OutputResult<K, String> ClientRequest(String serverUrl,
			T reqStruct, Class<K> clazz) {
		OutputResult<K, String> or = new OutputResult<K, String>();
		OutputSimpleResult osr = DataValid(reqStruct);
		if (osr.IsSucceed()) {
			Config myConfig=GetConfig();
			osr = GetRequestString(myConfig,reqStruct);
			if (osr.IsSucceed()) {
				if (myConfig.isParamsEncrypt()) {
					osr = EncryptParams(myConfig,osr.result);
				}
				if (osr.IsSucceed()) {
					String requestString = MessageFormat.format("{0}={1}",
							myConfig.getHttpRequestParameterDefaultKey(),
							osr.result);
					osr = HttpHelper.HttpPost(serverUrl, requestString);
					if (osr.IsSucceed()) {
						String responseString = osr.result;
						if (!Common.IsNullOrEmpty(responseString)) {
							if (myConfig.isParamsEncrypt()) {
								osr = DecryptParams(myConfig,responseString);
								if (osr.IsSucceed()) {
									responseString = osr.result;
								} else {
									or.result = osr.result;
								}
							}
							if (osr.IsSucceed()) {
								or = GetResponseStruct(myConfig,clazz, responseString);
							}
						} else {
							or.result = "返回参数为空，请联系接口服务商。";
						}
					} else {
						or.result = osr.result;
						or.exception = osr.exception;
					}
				} else {
					or.result = osr.result;
					or.exception = osr.exception;
				}
			}
		} else {
			or.result = osr.result;
			or.exception = osr.exception;
		}
		return or;
	}

}
