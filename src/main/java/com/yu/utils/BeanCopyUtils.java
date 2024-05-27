package com.yu.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BeanCopy 工具类
 */
public class BeanCopyUtils {

	/**
	 * 单个对象属性copy
	 *
	 * @param source 源对象
	 * @param clazz  目标对象类型
	 * @return 目标对象
	 */
	public static <V> V copyBean(Object source, Class<V> clazz) {
		V result = null;
		try {
			result = clazz.newInstance();
			BeanUtils.copyProperties(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * List 集合对象属性 copy
	 *
	 * @param list  源对象集合
	 * @param clazz 目标对象类型
	 * @param <O>   源对象类型
	 * @param <V>   目标对象类型
	 * @return 目标对象集合
	 */
	public static <O, V> List<V> copyBeanList(List<O> list, Class<V> clazz) {
		return list.stream()
				.map(o -> copyBean(o, clazz))
				.collect(Collectors.toList());
	}
}
