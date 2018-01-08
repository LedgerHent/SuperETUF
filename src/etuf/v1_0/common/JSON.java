package etuf.v1_0.common;

import com.google.gson.Gson;


/**
 * JSON工具
 * 
 * @author lxd
 * 
 */
public class JSON {
	/**
	 * JSON字符串反序列化为对象
	 * 
	 * @param clazz
	 * @param JSON字符串
	 * @return
	 */
	public static <T> T JsonToObject(Class<T> clazz, String json) {
		try {
			Gson gson = new Gson();
			T t = gson.fromJson(json, clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对象序列化为JSON字符串
	 * @param t 待序列化对象
	 * @return JSON字符串
	 */
	public static <T> String ObjectToJson(T t) {
		String json = "";
		if (t != null) {
			try {
				Gson gson = new Gson();
				json = gson.toJson(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json;
	}

}
