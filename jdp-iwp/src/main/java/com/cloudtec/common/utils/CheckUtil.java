package com.cloudtec.common.utils;

public class CheckUtil {

	/**
	 * 防xss漏洞用
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceInvaidStr(String str) {
		if (str != null) {
			str = filterString(str);
			String reg = "[|#$%@'\"<>\\\\+]";
			str = str.replaceAll(reg, "");
			return str;
		} else {
			return null;
		}
	}

	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			if(Character.isISOControl(ch)){
				System.out.println("乱码:"+s.charAt(i));
			} else {
			String s4 = Integer.toHexString(ch);
			System.out.println("原始数据:"+s.charAt(i)+" 16进制:"+s4 );
			str = str + s4;
			}
		}
		return str;
	}

	/**
	 * 防xss漏洞用 不替换邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceForEmail(String str) {
		if (str != null) {
			str = filterString(str);
			String reg = "[|#$%'\"<>\\\\+]";
			str = str.replaceAll(reg, "");
			return str;
		} else {
			return null;
		}
	}

	public static String filterBankCodeStr(String str) {
		String regular = "(0123456789- )";
		if (str != null) {
			str = filterString(str);
			StringBuffer newStr = new StringBuffer();
			char[] array = str.toCharArray();
			for (int i = 0; i < array.length; i++) {
				if (regular.indexOf(array[i] + "") > -1) {
					newStr.append(array[i] + "");
				}
			}
			return newStr.toString();
		}
		return null;
	}

	/**
	 * 
	 * 过滤替换掉mysql不支持存储的无效字符
	 * 
	 * @param charSequence
	 * @return
	 */
	public static String filterString(CharSequence charSequence) {
		StringBuffer sb = new StringBuffer("");
		if (charSequence != null && charSequence.length() > 0) {
			sb = new StringBuffer();
			
			for (int i = 0; i < charSequence.length(); i++) {
				char ch = charSequence.charAt(i);
				//增加过滤控制符和非汉字的乱码
				if(Character.isISOControl(ch)){
					continue;
				}
				if (!Character.isHighSurrogate(ch)
						&& !Character.isLowSurrogate(ch)) {
					sb.append(ch);
				}
				else {
					sb.append("");
				}
			}
		}

		return sb.toString();
	}

}
