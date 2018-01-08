package etuf.v1_0.controller.base;

import etuf.v1_0.common.Common;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.Request_Base;
import etuf.v1_0.model.base.Response_Base;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;

/**
 * 客户端抽象累
 * 
 * @author lxd
 * 
 */
public abstract class ClientAbs<T extends Request_Base,K extends Response_Base> {
	/**
	 * 设置服务器的url【待实现的抽象方法】
	 */
	protected abstract void SetServerUrl();

	private String serverUrl;

	/**
	 * 返回当前服务器url
	 * @return
	 */
	protected String getServerUrl() {
		return serverUrl;
	}

	/**
	 * 设置当前服务器的url
	 * @param serverUrl
	 */
	protected void setServerUrl(String serverUrl) {
		if (!Common.IsNullOrEmpty(serverUrl)) {
			this.serverUrl = serverUrl;
		}
	}
	
	public ClientAbs(){
		SetServerUrl();
	}
	
	
	/**
	 * 获取配置参数，必须和服务端配置参数一样
	 * @return
	 */
	protected abstract Config GetConfig();

	/**
	 * 根据请求参数结果获取格式化的请求参数字符串【待实现的抽象方法】
	 * @param myConfig
	 * @param reqStruct
	 * @return
	 */
	protected abstract OutputSimpleResult GetRequestString(Config myConfig,T reqStruct); 

	/**
	 * 检查请求参数合法性【待实现的抽象方法】
	 * @param reqStruct
	 * @return
	 */
	protected abstract  OutputSimpleResult DataValid(T reqStruct);
	
	/**
	 * 业务处理【待实现的抽象方法】
	 * @param reqStruct
	 * @return
	 */
	protected abstract  OutputResult<K,String>  DoBusiness(T reqStruct);
	
	/**
	 * 根据响应参数字符串获取响应参数对象【待实现的抽象方法】
	 * @param myConfig
	 * @param clazz 响应参数类型
	 * @param responseString 经过解签解密处理后的字符串
	 * @return
	 */
	protected abstract OutputResult<K,String> GetResponseStruct(Config myConfig,Class<K> clazz,String responseString);
	
	/**
	 * 请求参数加密
	 * @param myConfig
	 * @param requestString
	 * @return
	 */
	protected abstract OutputSimpleResult EncryptParams(Config myConfig,String requestString);

	/**
	 * 请求参数解密
	 * @param myConfig
	 * @param responseString
	 * @return
	 */
	protected abstract OutputSimpleResult DecryptParams(Config myConfig,String responseString);
	
}
