package etuf.v1_0.test;

import java.nio.charset.Charset;

import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.MaxCountsInGivenTime;
import etuf.v1_0.model.v2.base.Request_Base;
import etuf.v1_0.model.v2.base.Response_Base;

public abstract class Client<T extends Request_Base,K extends Response_Base> 
	extends etuf.v1_0.controller.base.v2.base.Client<T,K> {

	@Override
	protected void SetServerUrl() {
		setServerUrl("http://localhost:8080/etuf/etuf/v1_0/test/Servlet");
	}

	@Override
	protected Config GetConfig() {
		return new Config(new MaxCountsInGivenTime(5,2,"TestServlet"), 
				"etuf.v1_0.test.model", 
				"etuf.v1_0.test.controller", 
				true, "deskeyyy", "desivvvv", Charset.forName("UTF-8"));
	}
	
//	@Override
//	public OutputSimpleResult DataValid(T reqStruct) {
//		OutputSimpleResult osr=new OutputSimpleResult();
//		
//		osr.code=Constant.Code_Succeed;
//		osr.result="@all：这个方法必须做合法性判断哦，否则的话，在您的业务逻辑中直接用数据的时候肯能会抛异常的。";
//		
//		return osr;
//	}

}
