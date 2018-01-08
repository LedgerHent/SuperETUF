package etuf.v1_0.model.v2.base;

/**
 * v2版本 请求参数基类
 * @author lxd
 *
 */
@SuppressWarnings("serial")
public class Request_Base extends etuf.v1_0.model.base.Request_Base {

	/**
	 * 请求方法名称
	 */
	public String method; 

	/**
	 * 接口版本号，将来升级接口版本用
	 */
	public double versionId;
}
