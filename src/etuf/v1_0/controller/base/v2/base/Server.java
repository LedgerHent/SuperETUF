package etuf.v1_0.controller.base.v2.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


import etuf.v1_0.common.Common;
import etuf.v1_0.common.Constant;
import etuf.v1_0.common.EncryptHelper;
import etuf.v1_0.controller.DataFormatHelper;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;
import etuf.v1_0.model.v2.base.Request_Base;
import etuf.v1_0.model.v2.base.Response_Base;

@SuppressWarnings("serial")
public abstract class Server extends
		etuf.v1_0.controller.base.Server<Request_Base, Response_Base> {

	/**
	 * 实现父类方法，获取请求方法的名字
	 * @author lxd
	 * @param map
	 * @return 请求方法的名字
	 */
	@Override
	public String GetRequestMethodName(Map<String, String[]> map,Config myConfig) {
		String methodName = "";
		String requestString = GetRequestStringByKey(map,myConfig.getHttpRequestParameterDefaultKey());
		OutputResult<Request_Base, String> or = DataFormatHelper.Deserialize(
				Request_Base.class, requestString);
		if (or.IsSucceed()) {
			methodName = or.getResultObj().method;
		}
		return methodName;
	}

	@Override
	public boolean GetIsNeedEncrypt(Map<String, String[]> map,Config myConfig) {
		boolean isTest=false;
		
		String test=GetRequestStringByKey(map, myConfig.getHttpRequestParameterTestKey());
		if(!Common.IsNullOrEmpty(test)){
			isTest="true".equals(test.toLowerCase());
		}
		
		return !isTest;
	}
	
	/**
	 * 实现父类方法，获取请求参数结构
	 * @author lxd
	 * @param clazz
	 * @param map
	 * @return 
	 */
	@Override
	public <T extends Request_Base> OutputResult<T, String> GetRequestStruct(
			Class<T> clazz, Map<String, String[]> map,Config myConfig) {
		String requestString = GetRequestStringByKey(map,myConfig.getHttpRequestParameterDefaultKey());
		OutputResult<T, String> or = DataFormatHelper.Deserialize(clazz,
				requestString);
		return or;
	}


	private String GetRequestStringByKey(Map<String, String[]> map,String key) {
		String requestString = "";
		if(map!=null){
			if (map.containsKey(key)) {
				String[] ss = map.get(key);
				if (ss.length > 0) {
					requestString = ss[0];
				}
			}
		}
		return requestString;
	}


//	/**
//	 * 获取请求字符串
//	 * 
//	 * @param request
//	 * @return
//	 */
//	private OutputSimpleResult GetRequestString(HttpServletRequest request) {
//		OutputSimpleResult osr = new OutputSimpleResult();
//		String reqstMethod = request.getMethod().toUpperCase();
//		if (ConfigData.SupportHttpRequestMethods.contains(reqstMethod)) {
//			String data = request
//					.getParameter(ConfigData.HttpRequestParameterDefaultKey);
//			if (data != null && !"".equals(data)) {
//				osr.code = Constant.Code_Succeed;
//				osr.result = data;
//			} else {
//				osr.result = "请求参数为空，不予处理。";
//			}
//		} else {
//			osr.result = MessageFormat.format("不支持的请求类型：{0}。当前支持的请求类型为：{1}。",
//					reqstMethod, ConfigData.SupportHttpRequestMethods);
//		}
//		return osr;
//	}
	
	/**
	 * 参数解密
	 * @param map
	 * @return
	 */
	@Override
	public OutputResult<Map<String, String[]>,String> DecryptParams(Config myConfig,Map<String, String[]> map){
		OutputResult<Map<String, String[]>,String> or=new OutputResult<Map<String,String[]>, String>();
		String encryptStr=GetRequestStringByKey(map,myConfig.getHttpRequestParameterDefaultKey());
//		try {
//			encryptStr=URLDecoder.decode(encryptStr,myConfig.getCharset().displayName());
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		String decryptStr=EncryptHelper.DESDecrypt(encryptStr,
				myConfig.getDesKey(),myConfig.getDesIV(),myConfig.getCharset());
		if(!Common.IsNullOrEmpty(decryptStr)){
			String 	sign = MessageFormat.format("&{0}=",myConfig.getParameterKey4Sign());
			if(decryptStr.contains(sign)){
				int index=decryptStr.lastIndexOf(sign);
				String md5=decryptStr.substring(index + sign.length());
				decryptStr=decryptStr.substring(0,index);
				String myMd5=EncryptHelper.MD5Encrypt(decryptStr);
				if(md5.equals(myMd5)){
					//直接操作map会抛异常，原因是map被锁定了
					Map<String, String[]> newMap=new HashMap<String, String[]>();
					newMap.putAll(map);
					newMap.remove(myConfig.getHttpRequestParameterDefaultKey());
					newMap.put(myConfig.getHttpRequestParameterDefaultKey(),new String[]{decryptStr});
					or.setResultObj(newMap);
					or.code=Constant.Code_Succeed;
				}else{
					or.result="请求参数无效：无效的签名！";
				}
			}else{
				or.result="请求参数无效：必须包含参数签名！";
			}
		}else{
			or.result="请求参数无效，请提供正确的参数密文！";
		}
		
		return or;
	}
	
	
	
	
	/**
	 * 参数加密
	 * @param params
	 * @return
	 */
	@Override
	public OutputSimpleResult EncryptParams(Config myConfig,String params){
		OutputSimpleResult osr=new OutputSimpleResult();
		
		String md5=EncryptHelper.MD5Encrypt(params);
		if(!Common.IsNullOrEmpty(md5)){
			params=MessageFormat.format("{0}&{1}={2}", params,myConfig.getParameterKey4Sign(),md5);
			try {
				params=EncryptHelper.DESEncrypt(params,myConfig.getDesKey(),myConfig.getDesIV(),myConfig.getCharset());
				params=URLEncoder.encode(params,myConfig.getCharset().displayName());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			osr.result=params;
			osr.code=Constant.Code_Succeed;
		}else{
			osr.result="未知错误：MD5签名生成失败。";
		}
		return osr;
	}
	
}
