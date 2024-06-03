package com.yu.utils;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Properties 工具类
 */
public class PropertiesUtils {

    /**
     * 将 Properties 文件内容注入到对象中
     */
    public static void propertiesToObject(final Properties properties, final Object object, String prefix) {
        // 获取对象的所有公共方法
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();

            // 检查方法名是否以"set"开头
            if (methodName.startsWith("set")) {
                try {
                    // 获取属性名
                    String tmp = methodName.substring(4);
                    String first = methodName.substring(3, 4);
                    String key = prefix + first.toLowerCase() + tmp;

                    // 从 Properties 中获取属性值
                    String property = properties.getProperty(key);
                    if (property != null) {
                        // 获取方法的参数类型
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length > 0) {
                            // 获取第一个参数的类型名
                            String parameterType = parameterTypes[0].getSimpleName();
                            Object arg = null;

							// 根据参数类型解析属性值
							if (parameterType.equals("int") || parameterType.equals("Integer")) {
								arg = Integer.parseInt(property);
							} else if (parameterType.equals("long") || parameterType.equals("Long")) {
								arg = Long.parseLong(property);
							} else if (parameterType.equals("double") || parameterType.equals("Double")) {
								arg = Double.parseDouble(property);
							} else if (parameterType.equals("boolean") || parameterType.equals("Boolean")) {
								arg = Boolean.parseBoolean(property);
							} else if (parameterType.equals("float") || parameterType.equals("Float")) {
								arg = Float.parseFloat(property);
							} else if (parameterType.equals("String")) {
								arg = property;
							} else {
								continue;
							}

                            // 调用 setter 方法设置属性值
                            method.invoke(object, arg);
                        }
                    }
                } catch (Throwable ignored) {
                    // 忽略所有异常
                }
            }
        }
    }

    
    public static void propertiesToObject(final Properties p, final Object object) {
        propertiesToObject(p, object, "");
    }
 
}
