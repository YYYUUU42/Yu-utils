package com.yu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * 对象有效性校验工具类
 */
public class CheckUtil {

	/**
	 * 判断字符串是否是符合指定格式的时间
	 *
	 * @param date   时间字符串
	 * @param format 时间格式
	 * @return 是否符合
	 */
	public static boolean isDate(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.parse(date);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断字符串有效性
	 */
	public static boolean valid(String src) {
		return !(src == null || "".equals(src.trim()));
	}

	/**
	 * 判断一组字符串是否有效
	 */
	public static boolean valid(String[] src) {
		for (String s : src) {
			if (!valid(s)) {
				return false;
			}
		}
		return true;
	}


	/**
	 * 判断一个对象是否为空
	 */
	public static boolean valid(Object obj) {
		return !(null == obj);
	}

	/**
	 * 判断一组对象是否有效
	 */
	public static boolean valid(Object[] objs) {
		return objs != null && objs.length != 0;
	}

	/**
	 * 判断集合的有效性
	 */
	public static boolean valid(Collection collection) {
		return !(collection == null || collection.isEmpty());
	}

	/**
	 * 判断一组集合是否有效
	 */
	public static boolean valid(Collection... cols) {
		for (Collection c : cols) {
			if (!valid(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断map是否有效
	 */
	public static boolean valid(Map map) {
		return !(map == null || map.isEmpty());
	}

	/**
	 * 判断一组map是否有效
	 *
	 * @param maps 需要判断map
	 * @return 是否全部有效
	 */
	public static boolean valid(Map... maps) {
		for (Map m : maps) {
			if (!valid(m)) {
				return false;
			}
		}
		return true;
	}
}