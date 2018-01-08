package etuf.v1_0.controller.base;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import etuf.v1_0.common.Common;
import etuf.v1_0.controller.HttpServletAbs;
import etuf.v1_0.controller.RequestControl;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.Enum.StatusType;
import etuf.v1_0.model.base.Request_Base;
import etuf.v1_0.model.base.Response_Base;
import etuf.v1_0.model.base.Response_BaseMsg;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;

@SuppressWarnings("serial")
public abstract class Server<RequestBase extends Request_Base, ResponseBase extends Response_Base>
		extends HttpServletAbs {

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		Config myConfig=GetConfig();
		try {
			request.setCharacterEncoding(myConfig.getCharset().displayName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset="+myConfig.getCharset().displayName());
		response.setCharacterEncoding(myConfig.getCharset().displayName());
		ProcessRequest(request, response);
	}

	/**
	 * 获取请求方法名称【待重写的抽象方法】
	 * 
	 * @param map
	 * @return
	 */
	public abstract String GetRequestMethodName(Map<String, String[]> map,Config myConfig);
	
	/**
	 * 获取是否需要对参数进行加密处理
	 * @param map
	 * @return
	 */
	public abstract boolean GetIsNeedEncrypt(Map<String, String[]> map,Config myConfig);

	
	/**
	 * 获取请求参数结构【待重写的抽象方法】
	 * 
	 * @param map
	 * @return
	 */
	public abstract <T extends RequestBase> OutputResult<T, String> GetRequestStruct(
			Class<T> clazz, Map<String, String[]> map,Config myConfig);


	/**
	 * 处理请求
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void ProcessRequest(HttpServletRequest request,
			HttpServletResponse response) {
		Config myConfig= GetConfig();
		
		// 请求参数MAP获取
		OutputResult<Map<String, String[]>, String> or = super
				.GetRequestMap(myConfig,request);
		if (!or.IsSucceed()) {
			ResponseErrorMessage(response,myConfig,null, or.result);
			return;
		}
		Map<String, String[]> paramsMap = or.getResultObj();

		// 请求次数控制
		OutputSimpleResult osr = RequestControl.RequestValid(
				myConfig.getMaxCountsInGivenTime(), request);
		if (!osr.IsSucceed()) {
			ResponseErrorMessage(response,myConfig, paramsMap,osr.result);
			return;
		}


		// 处理请求参数加密，设置加密也允许测试不加密
		if (myConfig.isParamsEncrypt() && GetIsNeedEncrypt(paramsMap,myConfig)) {
			or=DecryptParams(myConfig,paramsMap);
			if(!or.IsSucceed()){
				ResponseErrorMessage(response,myConfig, paramsMap,or.result);
				return;
			}else{
				paramsMap=or.getResultObj();
			}
			
		}

		// 获取请求方法名称
		String tmp_method = GetRequestMethodName(paramsMap,myConfig);
		if (Common.IsNullOrEmpty(tmp_method)) {
			ResponseErrorMessage(response,myConfig,paramsMap, "方法名为空！");
			return;
		}
		// 方法名称首字母小写转大写
		String formatMethodName = tmp_method;
		if (tmp_method.length() > 1) {
			formatMethodName = MessageFormat.format("{0}{1}", tmp_method
					.substring(0, 1).toUpperCase(), tmp_method.substring(1));
		} else {
			formatMethodName = tmp_method.toUpperCase();
		}
		// 反射找到请求、响应参数类型并进一步处理
		String packPath = myConfig.getModelNameSpace();
		Class<? extends RequestBase> clazzT = null;
		Class<? extends ResponseBase> clazzK = null;
		String controllerPath = myConfig.getControllerNameSpace();
		Class<? extends ClientAbs<RequestBase, ResponseBase>> clazzC = null;
		try {
			clazzT = (Class<? extends RequestBase>) Class.forName(MessageFormat
					.format("{0}.Request_{1}", packPath, formatMethodName));
			clazzK = (Class<? extends ResponseBase>) Class
					.forName(MessageFormat.format("{0}.Response_{1}", packPath,
							formatMethodName));
			clazzC = (Class<? extends ClientAbs<RequestBase, ResponseBase>>) Class
					.forName(MessageFormat.format("{0}.{1}", controllerPath,
							formatMethodName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (clazzT == null || clazzK == null || clazzC == null) {
			ResponseErrorMessage(response,myConfig,paramsMap, MessageFormat.format(
					"未找到[{0}]对应的请求参数、响应参数或者控制器的定义。", formatMethodName));
			return;
		}
		DealWithRequest(myConfig,response, paramsMap, formatMethodName, clazzC,clazzT);
	}

	void ResponseErrorMessage(HttpServletResponse response, Config myConfig,
			Map<String, String[]> paramsMap,String errorMessage) {
		Response_BaseMsg resMsg = new Response_BaseMsg();
		resMsg.status = StatusType.Error.getStatus();
		resMsg.msg = errorMessage;
		super.ResponseMessage(myConfig,response, resMsg,GetIsNeedEncrypt(paramsMap,myConfig));
	}

	/**
	 * 处理客户端请求
	 * 
	 * @param response
	 * @param paramsMap
	 */
	private <T extends RequestBase, K extends ResponseBase> void DealWithRequest(
			Config myConfig,
			HttpServletResponse response, Map<String, String[]> paramsMap,
			String methodName,
			Class<? extends ClientAbs<RequestBase, ResponseBase>> clazzC,
			Class<T> clazzT) {
		OutputResult<T, String> or = GetRequestStruct(clazzT, paramsMap,myConfig);
		if (!or.IsSucceed()) {
			ResponseErrorMessage(response,myConfig,paramsMap, or.result);
			return;
		}
		OutputResult<K, String> orResponse = ClientRequest(methodName,or.getResultObj(),
				clazzC, clazzT);
		DealWithResponse(myConfig,response, paramsMap,orResponse);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends RequestBase, K extends ResponseBase> OutputResult<K, String> ClientRequest(
			String controllerName,T reqStruct,
			Class<? extends ClientAbs<RequestBase, ResponseBase>> clazzC,
			Class<T> clazzT) {
		OutputResult<K, String> or = new OutputResult<K, String>();
		if (reqStruct != null) {
			try {
				String doBusinessMethodName = "DoBusiness";
				String datavaildMethodName = "DataValid";
				Class[] clzs = new Class[] { clazzT };
				Method d = clazzC.getDeclaredMethod(datavaildMethodName, clzs);
				if (d != null) {
					Object ni = clazzC.newInstance();
					d.setAccessible(true);
					Object[] args = new Object[] { reqStruct };
					OutputSimpleResult osr = (OutputSimpleResult) d.invoke(ni,
							args);
					if (osr != null) {
						if (osr.IsSucceed()) {
							Method m = clazzC.getDeclaredMethod(
									doBusinessMethodName, clzs);
							if (m != null) {
								m.setAccessible(true);
								or = (OutputResult<K, String>) m.invoke(ni,
										args);
								if (or == null) {
									or = new OutputResult<K, String>();
									or.result = MessageFormat.format(
											"控制器[{0}]业务处理[{1}]返回值为空，请检查业务逻辑实现方法。",
											controllerName,doBusinessMethodName);
								}
							} else {
								or.result = MessageFormat.format(
										"控制器[{0}]中未找到名为[{1}]的方法签名。",
										controllerName,doBusinessMethodName);
							}
						} else {
							or.result = osr.result;
						}
					} else {
						or.result = MessageFormat.format(
								"控制器[{0}]合法性检查[{1}]返回值为空，请检查合法性校验的实现方法。",
								controllerName,datavaildMethodName);
					}
				} else {
					or.result = MessageFormat.format("控制器[{0}]中未找到名为[{1}]的方法签名。",
							controllerName,datavaildMethodName);
				}
			} catch (Exception e) {
				e.printStackTrace();
				or.result = "错误代码：201703271159。错误信息：调用控制器["+controllerName+"]方法发生异常。";
			}
		}

		return or;
	}

	private <K extends ResponseBase> void DealWithResponse(
			Config myConfig,
			HttpServletResponse response, 
			Map<String, String[]> paramsMap,
			OutputResult<K, String> or) {
		if (or.IsSucceed()){
			ResponseMessage(myConfig,response, or.getResultObj(),GetIsNeedEncrypt(paramsMap,myConfig));
		}
		else
			ResponseErrorMessage(response,myConfig, paramsMap,or.result);
	}

}
