package com.example.simpleapi.util;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil {

	private static GenericApplicationContext genericApplicationContext;

	public BeanUtil(GenericApplicationContext genericApplicationContext) {
		BeanUtil.genericApplicationContext = genericApplicationContext;
	}

	public static <T> T getBean(Class<T> clz) {
		return genericApplicationContext.getBean(clz);
	}

	public static <T> T getBean(String beanName, Class<T> clz) {
		return genericApplicationContext.getBean(beanName, clz);
	}
}