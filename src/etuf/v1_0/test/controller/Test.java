package etuf.v1_0.test.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import etuf.v1_0.common.Common;
import etuf.v1_0.common.Constant;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;
import etuf.v1_0.test.Client;
import etuf.v1_0.test.model.Request_Test;
import etuf.v1_0.test.model.Response_Test;
import etuf.v1_0.model.base.Enum;

public class Test extends Client<Request_Test, Response_Test> {

	@Override
	protected OutputSimpleResult DataValid(Request_Test reqStruct) {
		OutputSimpleResult osr = new OutputSimpleResult();
		if (reqStruct != null) {
			if (!Common.IsNullOrEmpty(reqStruct.testStr)) {
				if (reqStruct.testInt >= 0) {
					if (reqStruct.testHashMap != null
							&& reqStruct.testHashMap.size() > 0) {
						osr.code = Constant.Code_Succeed;
						osr.result = "【测试接口】不容易啊，终于对了。";
					} else {
						osr.result = "【测试接口】请求的testHashMap必须有有效值内容的哈。";
					}
				} else {
					osr.result = "【测试接口】请求的testInt必须是自然数哦。";
				}
			} else {
				osr.result = "【测试接口】请求的testStr不能为空哦。";
			}
		} else {
			osr.result = "【测试接口】请求参数结构为空，请确认请求参数是否合法。";
		}
		return osr;
	}

	@Override
	protected OutputResult<Response_Test, String> DoBusiness(
			Request_Test reqStruct) {
		OutputResult<Response_Test, String> or = new OutputResult<Response_Test, String>();
		or.code = Constant.Code_Succeed;
		or.result = "执行成功了啊，小伙子。<br>下面的结构可直接赋值，也可以把格式的字符串给该字段赋值，效果一样哦。";
		//给返回结果赋值
		Response_Test resStruct = new Response_Test();
		resStruct.status=Enum.StatusType.Succeed.getStatus();
		resStruct.retStr = MessageFormat.format("接口到您的请求字符串：{0}", reqStruct.testStr);
		resStruct.retInt=0-reqStruct.testInt;
		resStruct.retHashMap=new HashMap<String, Integer>();
		for(Entry<String,String[]> entry : reqStruct.testHashMap.entrySet()){
			resStruct.retHashMap.put(entry.getKey(), entry.getValue().length);
		}
		or.setResultObj(resStruct);

		return or;
	}

	

	

}
