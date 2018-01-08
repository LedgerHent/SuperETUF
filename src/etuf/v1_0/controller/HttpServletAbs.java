package etuf.v1_0.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import etuf.v1_0.common.Constant;
import etuf.v1_0.model.base.Classs;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;

@SuppressWarnings("serial")
public abstract class HttpServletAbs extends HttpServlet {


	/**
	 * 获取配置信息【待重写的抽象方法】
	 * 
	 * @return
	 */
	public abstract Config GetConfig();
	
	/**
	 * 参数解密
	 * @param map
	 * @return
	 */
	public abstract OutputResult<Map<String, String[]>,String> 
		DecryptParams(Config myConfig,Map<String, String[]> map);
	
	
	/**
	 * 参数加密
	 * @param params
	 * @return
	 */
	public abstract OutputSimpleResult EncryptParams(Config myConfig,String params);
	
	/**
	 * 处理请求【抽象方法，待实现】
	 * 
	 * @param request
	 * @param response
	 */
	public abstract void ProcessRequest(HttpServletRequest request,
			HttpServletResponse response);


	/**
	 * 获取请求参数MAP集合
	 * @param request
	 * @return
	 */
	public OutputResult<Map<String, String[]>, String> GetRequestMap(Config myConfig,
			HttpServletRequest request) {
		OutputResult<Map<String, String[]>, String> os = new OutputResult<Map<String, String[]>, String>();
		String reqstMethod = request.getMethod().toUpperCase();
		if (myConfig.getSupportHttpRequestMethods().contains(reqstMethod)) {
			Map<String, String[]> map = request.getParameterMap();
			if (map.size() > 0) {
				os.code=Constant.Code_Succeed;
				os.setResultObj(map);
			} else {
				os.result="请求参数列表为空，不予处理。";
			}
		} else {
			os.result = MessageFormat.format("不支持的请求类型：{0}。当前支持的请求类型为：{1}。",
					reqstMethod, myConfig.getSupportHttpRequestMethods());
		}
		return os;

	}

	/**
	 * 向客户端输出结构化的信息
	 * 
	 * @param response
	 * @param resStruct
	 */
	public <T extends Classs> void ResponseMessage(Config myConfig,
			HttpServletResponse response, T resStruct,boolean isNeedEncrypt) {
		String message = "";
		OutputSimpleResult osr= DataFormatHelper.Serialize(resStruct);
		message=osr.result;
		try {
			// 处理请求参数加密
			if (myConfig.isParamsEncrypt() && isNeedEncrypt) {
				osr=EncryptParams(myConfig,message);
				message=osr.result;
			}
			response.getWriter().write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
