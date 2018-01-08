package etuf.v1_0.test;

import java.nio.charset.Charset;

import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.MaxCountsInGivenTime;

@SuppressWarnings("serial")
public class Servlet extends etuf.v1_0.controller.base.v2.base.Server {

	@Override
	public Config GetConfig() {
		return new Config(new MaxCountsInGivenTime(5,2,"TestServlet"), 
				"etuf.v1_0.test.model", 
				"etuf.v1_0.test.controller", 
				true, "deskeyyy", "desivvvv", Charset.forName("UTF-8"));
	}



}
