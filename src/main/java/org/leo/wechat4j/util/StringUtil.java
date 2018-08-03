package org.leo.wechat4j.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * <pre>
 *  通用字符工具类
 * </pre>
 */
public class StringUtil {
	public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+|\\-?[0-9]+[.][0-9]+";
	
	public static String trim(Object obj) {
		if (null == obj)
			return "";
		return obj.toString().trim();
	}

	public static boolean isEmpty(String str) {
		return (null == str) || ("".equals(str.trim()));
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static boolean isEmpty(Object obj) {
		if(obj == null){
			return true;
		}
		if(obj instanceof String){
			return isEmpty(obj.toString());
		}
		if(obj instanceof Integer){
			return isEmpty(obj.toString());
		}
		if(obj instanceof Long){
			return isEmpty(obj.toString());
		}
		if(obj instanceof Map){
			return ((Map<?,?>)obj).isEmpty();
		}
		if(obj instanceof List){
			return ((List<?>)obj).isEmpty();
		}
		return false;
	}
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	public static boolean isTooLong(Object obj,int length) {
		if(obj instanceof String){
			if(((String)obj).length() > length){
				return true;
			}
		}
		if(obj instanceof Integer){
			if((obj+"").length() > length){
				return true;
			}
		}
		if(obj instanceof Long){
			if((obj+"").length() > length){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotNumeric(String str) {
		return !isNumeric(str);
	}
	
	public static boolean isMoney(String amount){
		if(isEmpty(amount)){
			return false;
		}
		if(!amount.toString().matches(CURRENCY_FEN_REGEX)) {
			return false;
        }
		return true;
	}
	
	public static String encode(String str) {
		if (null == str)
			return "";
		RuntimeException rex;
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException ex) {
			rex = new RuntimeException(ex.getMessage());
			rex.setStackTrace(ex.getStackTrace());
		}
		throw rex;
	}

	public static String decode(String str) {
		if (null == str)
			return "";
		RuntimeException rex;
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException ex) {
			rex = new RuntimeException(ex.getMessage());
			rex.setStackTrace(ex.getStackTrace());
		}
		throw rex;
	}

	public static String substringAfter(String str, String separator) {
		if (isEmpty(str))
			return str;

		if (separator == null)
			return "";

		int pos = str.indexOf(separator);
		if (pos == -1)
			return "";

		return str.substring(pos + separator.length());
	}

	public static Map<String, String> getCompartKeyAndVal(String str,String split) {
		Map<String, String> data = new HashMap<String, String>();
		if (isNotEmpty(str)) {
			String[] strs = str.split(split);
			if ((strs != null) && (strs.length > 0)) {
				for (int i = 0; i < strs.length; i++) {
					if (isNotEmpty(strs[i])) {
						String[] vals = strs[i].split("=");
						if ((vals != null) && (vals.length == 2)) {
							data.put(trim(vals[0]), trim(vals[1]));
						}
					}
				}
			}

		}
		return data;
	}
	
	public static String convertStr(Object obj){
		return obj == null?"":obj.toString();
	}
	
	public static BigDecimal convertBigDecimal(Object value){
		BigDecimal ret = null;
		if(value != null){
			if( value instanceof BigDecimal ) {
				ret = (BigDecimal) value;
			} else if( value instanceof String ) {
				ret = new BigDecimal( (String) value );  
			} else if( value instanceof BigInteger ) {
				ret = new BigDecimal( (BigInteger) value );  
			} else if( value instanceof Number ) {
				ret = new BigDecimal( ((Number)value).doubleValue() );  
			} else {
				throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");  
			}
		}
		return ret;
	}
	
	public static String convertMap2JSON(Map<String, Object> param){
		JSONObject object = JSONObject.fromObject(param);
		return object.toString();
	}
	
	/**
     * 前三后四手机号处理
     * 
     * @param mobileNum
     * @return
     */
    public static String hideMobileNum(String mobileNum){
    	String str = "";
    	try {
			String prefix = mobileNum.substring(0,3);
			String suffix = mobileNum.substring(7);
			str = prefix + "****" + suffix;
		} catch (Exception e) {
			str = mobileNum;
		}
    	return str;
    }
}