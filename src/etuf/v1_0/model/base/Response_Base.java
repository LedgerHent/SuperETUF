package etuf.v1_0.model.base;

import java.io.Serializable;

/**
 * 返回结果基类
 * @author lxd
 * 
 */
@SuppressWarnings("serial")
public class Response_Base extends Classs  implements Serializable {
	/**
	 * 返回状态 0-成功|1-失败
	 */
	public int status ;

}
