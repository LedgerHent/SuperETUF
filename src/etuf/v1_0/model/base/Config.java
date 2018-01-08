package etuf.v1_0.model.base;

import java.nio.charset.Charset;

import etuf.v1_0.common.Common;
import etuf.v1_0.model.base.Enum.DataFormat;

/**
 * 服务配置信息
 * 
 * @author lxd
 * 
 */
public class Config {
	private MaxCountsInGivenTime maxCountsInGivenTime;
	private String modelNameSpace;
	private String controllerNameSpace;
	private boolean paramsEncrypt = true;
	private String desKey = "据阿斯蒂";
	private String desIV = "@viptrip";
	private Charset charset = Charset.forName("UTF-8");

	// java参数传递默认key值
	private String httpRequestParameterDefaultKey = "data";
	// java参数传递测试参数的key值
	private String httpRequestParameterTestKey = "isTest";
	// 参数签名key
	private String parameterKey4Sign = "sign";
	// 支持的http请求方式
	private String supportHttpRequestMethods = "GET|POST";
	// 数据序列化方式，默认为JSON
	private DataFormat dataSerializerFormat = DataFormat.JSON;
	// 不作访问限制的ip序列
	private String exceptRequestRestrictInterfaceIPs = "";

	public Config() {
		this("etuf.v1_0.model", "etuf.v1_0.controller");
	}

	/**
	 * 配置信息 构造函数
	 * 
	 * @param modelNameSpace
	 *            model所在包的全路径
	 * @param controllerNameSpace
	 *            controller所在包的全路径
	 */
	public Config(String modelNameSpace, String controllerNameSpace) {
		this(new MaxCountsInGivenTime(), modelNameSpace, controllerNameSpace,
				true, "", "", null);
	}

	/**
	 * 配置信息 构造函数
	 * 
	 * @param maxCountsInGivenTime
	 *            访问控制参数
	 * @param modelNameSpace
	 *            model所在包的全路径
	 * @param controllerNameSpace
	 *            controller所在包的全路径
	 * @param paramsEncrypt
	 *            是否加密参数，默认值为true
	 * @param desKey
	 *            DES加解密所用的密钥，长度应该为8位
	 * @param desIV
	 *            DES加解密所用的向量，长度应该为8位
	 * @param charset
	 *            字符编码，传null则为默认值UTF-8
	 */
	public Config(MaxCountsInGivenTime maxCountsInGivenTime,
			String modelNameSpace, String controllerNameSpace,
			boolean paramsEncrypt, String desKey, String desIV, Charset charset) {
		this(maxCountsInGivenTime, modelNameSpace, controllerNameSpace,
				paramsEncrypt, desKey, desIV, charset, "", "", "", "", null, "");
	}

	/**
	 * @param maxCountsInGivenTime
	 *            访问控制参数
	 * @param modelNameSpace
	 *            model所在包的全路径
	 * @param controllerNameSpace
	 *            controller所在包的全路径
	 * @param paramsEncrypt
	 *            是否加密参数，默认值为true
	 * @param desKey
	 *            DES加解密所用的密钥，长度应该为8位
	 * @param desIV
	 *            DES加解密所用的向量，长度应该为8位
	 * @param charset
	 *            字符编码，传null则为默认值UTF-8
	 * @param httpRequestParameterDefaultKey
	 *            java参数传递默认key值
	 * @param httpRequestParameterTestKey
	 *            java参数传递测试参数的key值
	 * @param parameterKey4Sign
	 *            参数签名key
	 * @param supportHttpRequestMethods
	 *            支持的http请求方式
	 * @param dataSerializerFormat
	 *            数据序列化方式，默认为JSON
	 * @param exceptRequestRestrictInterfaceIPs
	 *            不作访问限制的ip序列
	 */
	public Config(MaxCountsInGivenTime maxCountsInGivenTime,
			String modelNameSpace, 
			String controllerNameSpace,
			boolean paramsEncrypt, 
			String desKey, 
			String desIV,
			Charset charset,
			String httpRequestParameterDefaultKey,
			String httpRequestParameterTestKey, 
			String parameterKey4Sign,
			String supportHttpRequestMethods, 
			DataFormat dataSerializerFormat,
			String exceptRequestRestrictInterfaceIPs) {
		setMaxCountsInGivenTime(maxCountsInGivenTime);
		setModelNameSpace(modelNameSpace);
		setControllerNameSpace(controllerNameSpace);
		setParamsEncrypt(paramsEncrypt);
		setDesKey(desKey);
		setDesIV(desIV);
		setCharset(charset);
		setHttpRequestParameterDefaultKey(httpRequestParameterDefaultKey);
		setHttpRequestParameterTestKey(httpRequestParameterTestKey);
		setParameterKey4Sign(parameterKey4Sign);
		setSupportHttpRequestMethods(supportHttpRequestMethods);
		setDataSerializerFormat(dataSerializerFormat);
		setExceptRequestRestrictInterfaceIPs(exceptRequestRestrictInterfaceIPs);
	}

	/**
	 * 获取访问控制参数
	 * 
	 * @return
	 */
	public MaxCountsInGivenTime getMaxCountsInGivenTime() {
		return maxCountsInGivenTime;
	}

	/**
	 * 设置访问控制参数
	 * 
	 * @param maxCountsInGivenTime
	 */
	public void setMaxCountsInGivenTime(
			MaxCountsInGivenTime maxCountsInGivenTime) {
		if (maxCountsInGivenTime != null) {
			this.maxCountsInGivenTime = maxCountsInGivenTime;
		}
	}

	/**
	 * 获取model所在包的全路径
	 * 
	 * @return
	 */
	public String getModelNameSpace() {
		return modelNameSpace;
	}

	/**
	 * 设置model所在包的全路径
	 * 
	 * @param modelNameSpace
	 */
	public void setModelNameSpace(String modelNameSpace) {
		if (!Common.IsNullOrEmpty(modelNameSpace)) {
			this.modelNameSpace = modelNameSpace;
		}
	}

	/**
	 * 获取controller所在包的全路径
	 * 
	 * @return
	 */
	public String getControllerNameSpace() {
		return controllerNameSpace;
	}

	/**
	 * 设置controller所在包的全路径
	 * 
	 * @param controllerNameSpace
	 */
	public void setControllerNameSpace(String controllerNameSpace) {
		if (!Common.IsNullOrEmpty(controllerNameSpace)) {
			this.controllerNameSpace = controllerNameSpace;
		}
	}

	/**
	 * 获取DES加解密所用的密钥
	 * 
	 * @return
	 */
	public String getDesKey() {
		return desKey;
	}

	/**
	 * 设置DES加解密所用的密钥，长度应该为8位
	 * 
	 * @param desKey
	 */
	public void setDesKey(String desKey) {
		if (!Common.IsNullOrEmpty(desKey)) {
			this.desKey = desKey;
		}
	}

	/**
	 * 获取DES加解密所用的向量
	 * 
	 * @return
	 */
	public String getDesIV() {
		return desIV;
	}

	/**
	 * 设置DES加解密所用的向量，长度应该为8位
	 * 
	 * @param desIV
	 */
	public void setDesIV(String desIV) {
		if (!Common.IsNullOrEmpty(desIV)) {
			this.desIV = desIV;
		}
	}

	/**
	 * 获取设置的字符编码
	 * 
	 * @return
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置字符编码
	 * 
	 * @param charset
	 */
	public void setCharset(Charset charset) {
		if (charset != null) {
			this.charset = charset;
		}
	}

	/**
	 * 获取是否加密
	 * 
	 * @return
	 */
	public boolean isParamsEncrypt() {
		return paramsEncrypt;
	}

	/**
	 * 设置是否加密参数
	 * 
	 * @param paramsEncrypt
	 */
	public void setParamsEncrypt(boolean paramsEncrypt) {
		if (paramsEncrypt != this.paramsEncrypt) {
			this.paramsEncrypt = paramsEncrypt;
		}
	}

	/**
	 * 获取 java参数传递默认key值
	 * 
	 * @return
	 */
	public String getHttpRequestParameterDefaultKey() {
		return httpRequestParameterDefaultKey;
	}

	/**
	 * 设置 java参数传递默认key值
	 * 
	 * @param httpRequestParameterDefaultKey
	 */
	public void setHttpRequestParameterDefaultKey(
			String httpRequestParameterDefaultKey) {
		if (!Common.IsNullOrEmpty(httpRequestParameterDefaultKey)) {
			this.httpRequestParameterDefaultKey = httpRequestParameterDefaultKey;
		}
	}

	/**
	 * 获取 java参数传递测试参数的key值
	 * 
	 * @return
	 */
	public String getHttpRequestParameterTestKey() {
		return httpRequestParameterTestKey;
	}

	/**
	 * 设置 java参数传递测试参数的key值
	 * 
	 * @param httpRequestParameterTestKey
	 */
	public void setHttpRequestParameterTestKey(
			String httpRequestParameterTestKey) {
		if (!Common.IsNullOrEmpty(httpRequestParameterTestKey)) {
			this.httpRequestParameterTestKey = httpRequestParameterTestKey;
		}
	}

	/**
	 * 获取 参数签名key
	 * 
	 * @return
	 */
	public String getParameterKey4Sign() {
		return parameterKey4Sign;
	}

	/**
	 * 设置 参数签名key
	 * 
	 * @param parameterKey4Sign
	 */
	public void setParameterKey4Sign(String parameterKey4Sign) {
		if (!Common.IsNullOrEmpty(parameterKey4Sign)) {
			this.parameterKey4Sign = parameterKey4Sign;
		}
	}

	/**
	 * 获取 支持的http请求方式
	 * 
	 * @return
	 */
	public String getSupportHttpRequestMethods() {
		return supportHttpRequestMethods;
	}

	/**
	 * 设置 支持的http请求方式
	 * 
	 * @param supportHttpRequestMethods
	 */
	public void setSupportHttpRequestMethods(String supportHttpRequestMethods) {
		if (!Common.IsNullOrEmpty(supportHttpRequestMethods)) {
			this.supportHttpRequestMethods = supportHttpRequestMethods;
		}
	}

	/**
	 * 获取 数据序列化方式，默认为JSON
	 * 
	 * @return
	 */
	public DataFormat getDataSerializerFormat() {
		return dataSerializerFormat;
	}

	/**
	 * 设置 数据序列化方式，默认为JSON
	 * 
	 * @param dataSerializerFormat
	 */
	public void setDataSerializerFormat(DataFormat dataSerializerFormat) {
		if (dataSerializerFormat != null) {
			this.dataSerializerFormat = dataSerializerFormat;
		}
	}

	/**
	 * 获取 不作访问限制的ip序列
	 * 
	 * @return
	 */
	public String getExceptRequestRestrictInterfaceIPs() {
		return exceptRequestRestrictInterfaceIPs;
	}

	/**
	 * 设置 不作访问限制的ip序列
	 * 
	 * @param exceptRequestRestrictInterfaceIPs
	 */
	public void setExceptRequestRestrictInterfaceIPs(
			String exceptRequestRestrictInterfaceIPs) {
		if (!Common.IsNullOrEmpty(exceptRequestRestrictInterfaceIPs)) {
			this.exceptRequestRestrictInterfaceIPs = exceptRequestRestrictInterfaceIPs;
		}
	}

}
