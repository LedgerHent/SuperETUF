package etuf.v1_0.common;

import java.nio.charset.Charset;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import etuf.v1_0.model.base.Config;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class EncryptHelper {
	
	/**
	 * DES加密【默认UTF-8编码、默认密钥和向量】
	 * @param encryptString
	 * @return base64编码的字符串
	 */
	public static String DESEncrypt(String encryptString) {
		Config c=new Config ();
		return DESEncrypt(encryptString,c.getDesKey(),c.getDesIV(),c.getCharset());
	}
	
	/**
	 * DES加密【指定编码、默认密钥和向量】
	 * @param encryptString
	 * @param desKey
	 * @param desIV
	 * @param charset
	 * @return base64编码的字符串
	 */
	public static String DESEncrypt(String encryptString,String desKey,String desIV,Charset charset) {
		String result=encryptString;			
		if(!Common.IsNullOrEmpty(encryptString)){
			result="";
			byte[] data= DESEncrypt(encryptString.getBytes(charset),
					desKey.getBytes(charset),desIV.getBytes(charset));
			if(data!=null){
				result=new BASE64Encoder().encode(data);
			}
		}
		return result;
	}

	/**
	 * DES解密【默认UTF-8编码、默认密钥和向量】
	 * @param decryptString
	 * @return base64编码的字符串
	 */
	public static String DESDecrypt(String decryptString) {
		Config c=new Config ();
		return DESDecrypt(decryptString,c.getDesKey(),c.getDesIV(),c.getCharset());
	}
	
	/**
	 * DES解密【指定编码、默认密钥和向量】
	 * @param decryptString
	 * @param charset
	 * @return base64编码的字符串
	 */
	public static String DESDecrypt(String decryptString,String desKey,String desIV,Charset charset) {
		String result=decryptString;			
		if(!Common.IsNullOrEmpty(decryptString)){
			result="";
			try {			
				 byte[] data= DESDecrypt(new BASE64Decoder().decodeBuffer(decryptString),
						 desKey.getBytes(charset),desIV.getBytes(charset));
				 if(data!=null){
					 result=new String(data,charset);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * DES加密
	 * @param datasource byte[]
	 * @param password byte[]
	 * @param iv byte[]
	 * @return byte[]
	 */
	public static byte[] DESEncrypt(byte[] datasource, byte[] desKey,byte[] desIV) {
		return DES(datasource,desKey,desIV,true);
	}
	
	/**
	 * DES解密
	 * @param datasource byte[]
	 * @param password byte[]
	 * @param iv byte[]
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] DESDecrypt(byte[] datasource, byte[] desKey,byte[] desIV) {
		return DES(datasource,desKey,desIV,false);
	}
	
	private static byte[] DES(byte[] datasource, byte[] password,byte[] desIV,boolean isEncrypt){
		try {
			// DES算法IV向量
			IvParameterSpec iv = new IvParameterSpec(GetByteArrayWithLength8(desIV));
			DESKeySpec desKey = new DESKeySpec(GetByteArrayWithLength8(password));
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 用密匙初始化Cipher对象
			cipher.init(isEncrypt ?Cipher.ENCRYPT_MODE:Cipher.DECRYPT_MODE, securekey, iv);
			// 现在，获取数据并进行加密、解密操作
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得8位长度的字节数字，长了截取，短了用0字节补齐
	 * @param source
	 * @return
	 */
	private static byte[] GetByteArrayWithLength8(byte[] source){
		byte[] target=new byte[8];
		if(source==null || source.length==0){
			source=new byte[]{0};
		}
		int len=source.length;
		for(int i=0;i<8;i++){
			if(i<len){
				target[i]=source[i];
			}else{
				target[i]=0;
			}
		}
		
		return target;
	}

	/**
	 * MD5加密【默认32位大写】
	 * @param plainText
	 * @return
	 */
    public static String MD5Encrypt(String encryptString) {  
        return MD5Encrypt(encryptString,true,true);
    }  
	
    /**
     * MD5加密
     * @param encryptString 待加密的字符串
     * @param is32Bit 是否32位结果
     * @param isUpperCase 是否要大写
     * @return
     */
    public static String MD5Encrypt(String encryptString,boolean is32Bit,boolean isUpperCase) {  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(encryptString.getBytes());  
            byte b[] = md.digest();  
            int i;  
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++) {  
                i = b[offset];  
                if (i < 0)  
                    i += 256;  
                if (i < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(i));  
            }  
            String result=is32Bit?buf.toString():buf.toString().substring(8, 24);
            if(isUpperCase){
            	result=result.toUpperCase();
            }else{
            	result=result.toLowerCase();
            }
            return result;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
  
    }  
	
}
