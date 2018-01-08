package etuf.v1_0.controller;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import etuf.v1_0.common.Constant;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.MaxCountsInGivenTime;
import etuf.v1_0.model.base.output.OutputSimpleResult;

public class RequestControl {

	private static Hashtable<String, Vector<Date>> clientRequestRecord = new Hashtable<String, Vector<Date>>();

	/**
	 * 获取请求客户端的ip地址
	 * 
	 * @param request
	 * @return
	 */
	private static String getIpAddress(HttpServletRequest request) {
		// 通过请求头获取ip地址
		String ip = request.getHeader("x-forwarded-for");
		// 判断ip地址是否是代理地址
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		// 判断ip地址是否是代理地址
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		// 判断ip地址是否是代理地址
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static OutputSimpleResult RequestValid(
			MaxCountsInGivenTime requestRestrict,
			HttpServletRequest httpServletRequest) {
		OutputSimpleResult osr = new OutputSimpleResult();

		boolean isValid = false;
		int flag = 0;
		try {
			flag = 1;
			String address = getIpAddress(httpServletRequest);
			if (!new Config().getExceptRequestRestrictInterfaceIPs().contains(address)) {
				String key = MessageFormat.format("{0}→{1}", address,
						requestRestrict.getTarget());

				if (clientRequestRecord.containsKey(key)) {
					flag = 2;
					Vector<Date> timeList = clientRequestRecord.get(key);
					if (timeList.size() < requestRestrict.getCounts()) {
						flag = 3;
						// 请求数量小于限定的个数，允许
						timeList.add(new Date());
						isValid = true;
					} else {
						flag = 4;
						double remainSeconds = (timeList.get(0).getTime()
								+ requestRestrict.getSeconds() * 1000 - new Date()
								.getTime()) / 1000;
						if (remainSeconds > 0) {
							flag = 5;
							osr.result = MessageFormat.format(
									"客户端[{0}]请求过于频繁，请于[{1}]秒后重新请求！", key,
									String.valueOf(new DecimalFormat("#.00")
											.format(remainSeconds)));
						} else {
							flag = 6;
							timeList.remove(0);
							timeList.add(new Date());
							isValid = true;
						}
					}
				} else {
					flag = 7;
					Vector<Date> list = new Vector<Date>();
					list.add(new Date());
					if (!clientRequestRecord.containsKey(key)) {
						clientRequestRecord.put(key, list);
					} else {
						clientRequestRecord.get(key).add(new Date());
					}
					isValid = true;
				}
			} else isValid = true;
			
			if(isValid){
				osr.code= Constant.Code_Succeed;
				osr.result="正常访问";
			}
		} catch (Exception ex) {
			osr.exception=ex;
			osr.result="错误代码：201703261631。错误信息：" + ex.getStackTrace();
			osr.result += "<br>flag--" + String.valueOf(flag);
		}

		return osr;
	}
}
