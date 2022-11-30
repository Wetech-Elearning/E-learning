package io.train.common.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.train.common.lang.DateUtils;

/**
 * 格式校验工具类
 * 
 * @author Tony
 * @version 2018-1-6
 */
public class ValidateUtils {
	private static final String[] pwdPattern = { "1qa", "qaz", "2ws", "wsx", "3ed", "edc", "4rf", "rfv", "5tg", "tgb",
			"6yh", "yhn", "7uj", "ujm", "8ik", "9ol", "!qa", "@ws", "#ed", "$rf", "%tg", "^yh", "&uj", "*ik", "(ol",
			"qwe", "wer", "ert", "rty", "tyu", "yui", "uio", "iop", "asd", "sdf", "dfg", "fgh", "ghj", "hjk", "jkl",
			"zxc", "xcv", "cvb", "vbn", "bnm" };

	/**
	 * 2013-11-22 校验日期是否合法. 此处只判断参数是否为 yyyy-MM-dd形式，具体是否合法日期不作判断
	 * 
	 * @param str  目标字符串
	 */
	public static boolean isValidDate(String sDate) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		boolean flag = false;
	     /*String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"  
	             + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"  
	             + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"  
	             + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("  
	             + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"  
	             + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";  */
		if (sDate != null) {
			try {
				Pattern pattern = Pattern.compile(datePattern1);
				Matcher match = pattern.matcher(sDate);
				if (match.matches()) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//
			}
		}
		return flag;
	}
	
	/**
	 * 校验str是否为整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidNumber(String str) {
		return StringUtils.isNumeric(str);
	}

	/**
	 * 校验str是否为指定格式日期格式
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static boolean isValidDate(String str, String pattern) {
		boolean result = false;
		try {
			Date date = DateUtils.parseDate(str, pattern);
			if (date != null && StringUtils.equals(str, DateUtils.formatDate(date, pattern))) {
				result = true;
			}
		} catch (Exception e) {
			//
		}

		return result;
	}

	/**
	 * 2013-12-10 校验用户名是否合法:用户名只能由8-25位字母、数字、下划线以及@、#、_、$、%、.和&特殊字符组成,且不能全为数字;
	 * 
	 * @param str 目标字符串
	 */
	public static boolean isValidUserName(String str) {
		boolean flag = false;
		if (StringUtils.isBlank(str)) {
			return false;
		}
		try {
			String regExp = "^[a-zA-Z0-9@#_$%.&_=]{8,25}$";
			Pattern pattern = Pattern.compile(regExp);
			Matcher match = pattern.matcher(str);
			if (match.matches()) {
				regExp = "^[0-9]*$";
				pattern = Pattern.compile(regExp);
				match = pattern.matcher(str);

				if (match.matches()) {
					flag = false;
				} else {
					flag = true;
				}
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			//
		}

		return flag;
	}

	/**
	 * 2013-12-10 校验密码是否合法:密码只能由6-25位字母、数字、下划线以及@、#、_、$、%、.和&特殊字符组成,;
	 * 
	 * @param str 目标字符串
	 */
	public static boolean isValidPwd(String str) {
		boolean flag = false;
		if (StringUtils.isBlank(str)) {
			return false;
		}
		try {
			String regExp = "^[a-zA-Z0-9@#_$%.&]{6,25}$";
			Pattern pattern = Pattern.compile(regExp);
			Matcher match = pattern.matcher(str);
			if (match.matches()) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			//
		}		
		return flag;
	}

	/**
	 * 2013-12-10 校验Email是否合法
	 * 
	 * @param str 目标字符串
	 */
	public static boolean isValidEmail(String str) {
		boolean flag = false;
		if (StringUtils.isBlank(str)) {
			return false;
		}
		String regExp = "^([a-zA-Z0-9]+[-|_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";

		try {
			Pattern pattern = Pattern.compile(regExp);
			Matcher match = pattern.matcher(str);
			if (match.matches()) {
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {

		}
		return flag;
	}

	/**
	 * 2013-12-10 校验Mobile手机号码是否合法
	 * 2017-03-11 更新正则表达式包含177号码段
	 * 
	 * @param str 目标字符串
	 */
	public static boolean isValidMobile(String str) {
		boolean flag = false;
		if (StringUtils.isBlank(str)) {
			return false;
		}
		String regExp = "^1[3|5|6|7|8|9][0-9]\\d{8}$";

		try {
			Pattern pattern = Pattern.compile(regExp);
			Matcher match = pattern.matcher(str);
			if (match.matches()) {
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {

		}
		return flag;

	}

	/**
	 * 2013-12-10 校验身份证号码是否合法,暂时只校验位数对不对 
	 * 2014-03-20 特别注意身份证号码末尾除数字外可能为'x'或'X'
	 * 
	 * @param str 目标字符串
	 */
	public static boolean isValidCredNo(String str) {
		boolean flag = false;
		if (StringUtils.isBlank(str)) {
			return false;
		}

		// String regExp = "^\\d{15}|\\d{18}$";
		// String regExp = "^[0-9]{17}+[0-9|x|X]$";
		// 20170916 更新身份证有效性正则表达式验证
		String regExp = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)";
		try {
			Pattern pattern = Pattern.compile(regExp);
			Matcher match = pattern.matcher(str);
			if (match.matches()) {
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {

		}
		return flag;
	}
	
	/**
	 * 校验用户密码强度
	 * 
	 * @param pwd 用户密码字符串
	 * @param refStr 相关联字符串
	 * @return
	 */
	public static int checkPwdStrength(final String pwd, final String refStr) {
		if (!checkPwd(pwd)) {
			return 1; // 密码不符合规则至少包含大写字母、小写字母、数字、特殊字符中的三种
		}
		// 校验
		for (int i = 0; i < pwdPattern.length; i++) {
			if (pwd.indexOf(pwdPattern[i]) > 0) {
				return 2; // 存在键盘次序字符序列
			}
		}

		if (pwd.indexOf(refStr) >= 0 || refStr.indexOf(pwd) >= 0) {
			return 3; // 密码字符与用户名存在关联
		}
		return 0;
	}

	/**
	 * 校验密码是否符合规则至少包含大写字母、小写字母、数字、特殊字符中的三种
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkPwd(final String str) {
		boolean result = false;
		int modes = 0;

		try {
			String regExp = "^[a-zA-Z0-9@#_$%.&_=]{8,25}$";
			Pattern pattern = Pattern.compile(regExp);
			Matcher match = pattern.matcher(str);
			if (match.matches()) {
				modes = Pattern.compile("\\d").matcher(str).find() ? (modes + 1) : modes;
				modes = Pattern.compile("[a-z]").matcher(str).find() ? (modes + 1) : modes;
				modes = Pattern.compile("[A-Z]").matcher(str).find() ? (modes + 1) : modes;
				modes = Pattern.compile("[-.!@#$%^&*()+?><]").matcher(str).find() ? (modes + 1) : modes;
				if (modes >= 3) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			//
		}

		return result;
	}
	
	/**
	 * 2013-11-22 判断字符串是否为数字字母横线组合方法
	 * 
	 * @param str 目标字符串
	 */
	public static boolean isValidParam(String str) {
		boolean flag = false;
		String checkStr = "[0-9a-zA-Z-]*";
		try {
			Pattern pr = Pattern.compile(checkStr);
			Matcher mat = pr.matcher(str);
			flag = mat.matches();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			//
		}

		return flag;
	}

	/**
	 * 2013-11-22 过滤字符串中所有特殊字符
	 * 
	 * @param str 目标字符串
	 */
	public static String strFilter(String str) {
		String result = "";
		if (str == null) {
			return "";
		}
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%&*()_——+|{}【】‘；：”“’。，、？]";
		try {
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			result = StringUtils.trimToEmpty(m.replaceAll(""));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
		}
		return result;
	}
	
	/**
	 * 过滤请求中的CRLF字符，避免HTTP分割响应攻击
	 * 
	 * @param str
	 * @return
	 */
	public static String filterCRLF(String str) {
		String result = "";
		if (StringUtils.isNotBlank(str)) {
			// String regex = "[\r\n\f\012\015\\x0A\\x0D]";
			String regex = "\\s*|\f|\t|\r|\n";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(str);
			result = m.replaceAll("");
		}
		return result;
	}
}
