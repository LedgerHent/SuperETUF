package etuf.v1_0.controller;

import java.text.MessageFormat;

import etuf.v1_0.common.Constant;
import etuf.v1_0.common.JSON;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.Enum.DataFormat;
import etuf.v1_0.model.base.output.OutputResult;
import etuf.v1_0.model.base.output.OutputSimpleResult;

public class DataFormatHelper {

	/**
	 * 数据序列化，以默认的序列化方式
	 * @param reqStruct 待序列化的对象
	 * @return
	 */
	public static <T> OutputSimpleResult Serialize(T reqStruct) {
		return Serialize(reqStruct, new Config().getDataSerializerFormat());
	}

	/**
	 * 数据序列化，以指定的序列化方式
	 * @param reqStruct 待序列化的对象
	 * @param format 序列化方式
	 * @return
	 */
	public static <T> OutputSimpleResult Serialize(T reqStruct,
			DataFormat format) {
		OutputSimpleResult osr = new OutputSimpleResult();
		if (format != null) {
			if (format == DataFormat.JSON) {
				String json = JSON.ObjectToJson(reqStruct);
				if (!etuf.v1_0.common.Common.IsNullOrEmpty(json)) {
					osr.code = Constant.Code_Succeed;
					osr.result = json;
				} else {
					osr.result = MessageFormat.format(
							"错误代码：201703271459。错误信息：[{0}]序列化失败。", format);
				}
			} else {
				osr.result = MessageFormat.format("未实现的序列化方式：{0}。", format);
			}
		} else {
			osr.result = "错误代码：201703271506。错误信息：未提供数据序列化方式。";
		}

		return osr;
	}

	/**
	 * 数据反序列化，以默认的反序列化方式
	 * @param clazz 泛型类型
	 * @param formatString 待反序列化的字符串
	 * @return
	 */
	public static <T> OutputResult<T,String> Deserialize(Class<T> clazz,String formatString){
		return Deserialize(clazz,formatString,new Config().getDataSerializerFormat());
	}
	
	/**
	 * 数据反序列化，以指定的反序列化方式
	 * @param clazz 泛型类型
	 * @param formatString 待反序列化的字符串
	 * @param format 序列化方式
	 * @return
	 */
	public static <T> OutputResult<T,String> Deserialize(Class<T> clazz,
			String formatString,DataFormat format){
		OutputResult<T,String> or=new OutputResult<T, String>();
		if (format != null) {
			if (format == DataFormat.JSON) {
				T reqStruct=JSON.JsonToObject(clazz, formatString);
				if (reqStruct!=null) {
					or.code = Constant.Code_Succeed;
					or.result = formatString;
					or.setResultObj(reqStruct);
				} else {
					or.result = MessageFormat.format(
							"错误代码：201703271511。错误信息：[{0}]反序列化失败。", format);
				}
			} else {
				or.result = MessageFormat.format("未实现的序列化方式：{0}。", format);
			}
		} else {
			or.result = "错误代码：201703271509。错误信息：未提供数据反序列化方式。";
		}
		return or;
	}
	
}
