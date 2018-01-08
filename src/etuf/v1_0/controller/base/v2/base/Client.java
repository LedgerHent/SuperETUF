package etuf.v1_0.controller.base.v2.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;

import etuf.v1_0.common.Common;
import etuf.v1_0.common.Constant;
import etuf.v1_0.common.EncryptHelper;
import etuf.v1_0.controller.DataFormatHelper;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;
import etuf.v1_0.model.v2.base.Request_Base;
import etuf.v1_0.model.v2.base.Response_Base;

public abstract class Client<T extends Request_Base, K extends Response_Base>
		extends etuf.v1_0.controller.base.Client<T, K> {

	/**
	 * 实现并覆盖父类的方法： 根据请求参数对象获取格式化的字符串
	 */
	@Override
	protected OutputSimpleResult GetRequestString(Config myConfig,T reqStruct) {
		// 给method字段赋值
		if (Common.IsNullOrEmpty(reqStruct.method)) {
			String classFullName = reqStruct.getClass().getName();
			String[] split = classFullName.split("\\.");
			if (split.length > 0) {
				split = split[split.length - 1].split("_");
				if (split.length > 0) {
					reqStruct.method = split[split.length - 1];
				}
			}

		}
		// 给versionId赋值
		if (reqStruct.versionId == 0.0) {
			reqStruct.versionId = 1.0;
		}
		return DataFormatHelper.Serialize(reqStruct,myConfig.getDataSerializerFormat());
	}

	/**
	 * 实现并覆盖父类的方法： 根据格式化的响应参数获取响应参数对象
	 */
	@Override
	protected OutputResult<K, String> GetResponseStruct(Config myConfig,Class<K> clazz,
			String responseString) {
		return DataFormatHelper.Deserialize(clazz, responseString);
	}

	/**
	 * 实现并覆盖父类的方法： 给请求参数加签名加密
	 */
	@Override
	protected OutputSimpleResult EncryptParams(Config myConfig,String requestString) {
		OutputSimpleResult osr = new OutputSimpleResult();
		String md5 = EncryptHelper.MD5Encrypt(requestString);
		if (!Common.IsNullOrEmpty(md5)) {
			requestString = MessageFormat.format("{0}&{1}={2}", requestString,
					myConfig.getParameterKey4Sign(), md5);
			String desStr = EncryptHelper.DESEncrypt(requestString,
					myConfig.getDesKey(),myConfig.getDesIV(),myConfig.getCharset());
			if (!Common.IsNullOrEmpty(desStr)) {
				try {
					osr.result = URLEncoder.encode(desStr,myConfig.getCharset().displayName());
					osr.code = Constant.Code_Succeed;
				} catch (UnsupportedEncodingException e) {
					osr.result="未知错误：URL编码发生错误。";
					e.printStackTrace();
				}
			} else {
				osr.result = "未知错误：生成加密密文失败。";
			}
		} else {
			osr.result = "未知错误：生成MD5签名失败。";
		}
		return osr;
	}

	/**
	 * 实现并覆盖父类的方法： 给返回参数加解签解密
	 */
	@Override
	protected OutputSimpleResult DecryptParams(Config myConfig,String responseString) {
		OutputSimpleResult osr = new OutputSimpleResult();
		try {
			responseString=URLDecoder.decode(responseString,myConfig.getCharset().displayName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String decryptStr = EncryptHelper.DESDecrypt(responseString,
				myConfig.getDesKey(),myConfig.getDesIV(),myConfig.getCharset());
		if (!Common.IsNullOrEmpty(decryptStr)) {
			String sign = MessageFormat.format("&{0}=",
					myConfig.getParameterKey4Sign());
			if (decryptStr.contains(sign)) {
				int index = decryptStr.lastIndexOf(sign);
				String md5 = decryptStr.substring(index + sign.length());
				decryptStr = decryptStr.substring(0, index);
				String myMd5 = EncryptHelper.MD5Encrypt(decryptStr);
				if (md5.equals(myMd5)) {
					osr.result = decryptStr;
					osr.code = Constant.Code_Succeed;
				} else {
					osr.result = "请求参数无效：无效的签名！";
				}
			} else {
				osr.result = "请求参数无效：必须包含参数签名！";
			}
		} else {
			osr.result = "请求参数无效，请提供正确的参数密文！";
		}
		return osr;
	}

	/**
	 * 覆盖父类的客户端请求方法
	 * 
	 * @param reqStruct
	 * @param clazz
	 * @return
	 */
	public OutputResult<K, String> ClientRequest(T reqStruct, Class<K> clazz) {
		return ClientRequest(getServerUrl(), reqStruct, clazz);
	}

}
