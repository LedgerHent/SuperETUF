package etuf.v1_0.model.base.output;

import etuf.v1_0.controller.DataFormatHelper;
import etuf.v1_0.model.base.Config;
import etuf.v1_0.model.base.Enum.DataFormat;


/**
 * 方法请求结果（返回对象）
 * @author lxd
 *
 * @param <T> 成功返回参数类型
 * @param <F> 失败返回参数类型
 */
public class OutputResult<T,F> extends OutputSimpleResult {
	/*
	 * 数据序列化和反序列化方式，虽然有总开关，但是也允许单独设置
	 */
	private DataFormat dataFormat=new Config().getDataSerializerFormat();
	
	/*
	 * 请求成功返回参数结构
	 */
	private T resultObj=null;
	
	/*
	 * 请求失败返回错误参数结构
	 */
	private F errorObj=null;

	/**
	 * @return 数据序列化和反序列化方式，虽然有总开关，但是也允许单独设置
	 */
	public DataFormat getDataFormat() {
		return dataFormat;
	}

	/**
	 * 设置数据序列化和反序列化方式，虽然有总开关，但是也允许单独设置
	 * @param dataFormat
	 */
	public void setDataFormat(DataFormat dataFormat) {
		this.dataFormat = dataFormat;
	}
	
	/**
	 * 请求成功返回参数结构
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getResultObj(){
		if(resultObj==null){
			OutputResult<T,String> or=(OutputResult<T, String>) DataFormatHelper.
					Deserialize(resultObj.getClass(), super.result);
			if(or.IsSucceed()){
				resultObj=or.getResultObj();
			}
		}
		return resultObj;
	}
	
	/**
	 * 设置结果对象
	 * @param resultObj
	 */
	public void setResultObj(T resultObj){
		this.resultObj=resultObj;
	}

	/**
	 * 请求失败返回错误信息结构
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public F getErrorObj() {
		if(errorObj==null){
			OutputResult<F,String> or=(OutputResult<F, String>) DataFormatHelper.
					Deserialize(errorObj.getClass(), super.result);
			if(or.IsSucceed()){
				errorObj=or.getResultObj();
			}
		}
		return errorObj;
	}


	
}
