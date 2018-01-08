package etuf.v1_0.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.output.OutputSimpleResult;

public class HttpHelper {

	/**
	 * 发送http get请求，默认用UTF-8编码发送
	 * @param url	服务器地址
	 * @param param	请求参数 key1=value1&key2=value2 形式
	 * @return
	 */
	public static OutputSimpleResult HttpGet(String url, String param) {
		return HttpGet(url,param,new Config().getCharset());
	}
	
	/**
	 * 发送http get请求
	 * 
	 * @param serverUrl
	 *            服务器地址
	 * @param param
	 *            参数信息
	 * @param charset
	 *            字符集[ASCII,ISO-8859-1,GB2312,GBK,GB18030,UTF-16,UTF-8]
	 * @return
	 */
	public static OutputSimpleResult HttpGet(String url, String param,
			Charset charset) {
		OutputSimpleResult osr = new OutputSimpleResult();

		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), charset));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			String resultStr = sb.toString();
			if (!Common.IsNullOrEmpty(resultStr)) {
				osr.code = Constant.Code_Succeed;
				osr.result = resultStr;
			} else {
				osr.result = "未接收到任何数据。";
			}
		} catch (Exception e) {
			osr.result = "错误代码：201704231458。错误信息：发送HTTP GET请求出现异常！";
			osr.exception = e;
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return osr;
	}

	/**
	 * 发送http post请求，默认用UTF-8编码发送
	 * 
	 * @param serverUrl
	 *            服务器地址
	 * @param requestString
	 *            请求字符串
	 * @return
	 */
	public static OutputSimpleResult HttpPost(String serverUrl,
			String requestString) {
		return HttpPost(serverUrl, requestString, new Config().getCharset());
	}

	/**
	 * 发送http post请求
	 * 
	 * @param serverUrl
	 *            服务器地址
	 * @param requestString
	 *            请求字符串
	 * @param charset
	 *            字符集[ASCII,ISO-8859-1,GB2312,GBK,GB18030,UTF-16,UTF-8]
	 * @return
	 */
	public static OutputSimpleResult HttpPost(String serverUrl,
			String requestString, Charset charset) {
		OutputSimpleResult osr = new OutputSimpleResult();

		OutputStreamWriter out = null;
		BufferedReader in = null;
		try {
			URL realUrl = new URL(serverUrl);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), charset);
			// 发送请求参数
			out.write(requestString);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), charset));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			String resultStr = sb.toString();
			if (!Common.IsNullOrEmpty(resultStr)) {
				osr.code = Constant.Code_Succeed;
				osr.result = resultStr;
			} else {
				osr.result = "未接收到任何数据。";
			}
		} catch (Exception e) {
			osr.result = "错误代码：201703271550。错误信息：发送HTTP POST请求出现异常！";
			osr.exception = e;
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return osr;
	}

}
