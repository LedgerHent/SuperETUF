package etuf.v1_0.common;

public class Common {
	/**
	 * 返回字符数是否为null或者空串
	 * @param str
	 * @return
	 */
	public static boolean IsNullOrEmpty(String str){
		return str==null || "".equals(str);
	}
}
