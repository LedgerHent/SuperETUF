package etuf.v1_0.model.base.output;

import etuf.v1_0.common.Constant;


/**
 * 方法请求结果基类
 * @author lxd
 *
 */
public class OutputBase {
	public OutputBase(){
		this.code=Constant.Code_Failed;
		this.result="默认错误";
		this.exception=null;
	}
	
	public OutputBase(int code,String result,Exception ex)
	{
		this.code=code;
		this.result=result;
		this.exception=ex;
	}
	
	/**
	 * 返回结果代码
	 */
	public int code;
	
	/**
	 * 返回结果字符串
	 */
	public String result;
	
	/**
	 * 捕获异常
	 */
	public Exception exception; 
}
