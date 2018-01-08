package etuf.v1_0.model.base.output;

import etuf.v1_0.common.Constant;

/**
 * 方法请求结果（返回简单类型）
 * @author lxd
 *
 */
public class OutputSimpleResult extends OutputBase{
	/**
	 * @return 方法执行是否成功
	 */
	public boolean IsSucceed()
	{
		return super.code==Constant.Code_Succeed;
	}
}
