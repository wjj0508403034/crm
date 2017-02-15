package com.huoyun.core.bo.utils;

import org.springframework.util.StringUtils;

import com.huoyun.core.bo.annotation.BoEntity;

public class BusinessObjectUtils {

	public final static String SYSTEM_BO_NAMESPACE = "com.huoyun.sbo";

	public static String getBoFullName(Class<?> clazz) {
		String name = getBoName(clazz);
		String namespace = getBoNamespace(clazz);
		return getFullName(namespace, name);
	}

	public static String getBoName(Class<?> clazz) {
		BoEntity annot = clazz.getAnnotation(BoEntity.class);
		String name = annot.name();
		return StringUtils.isEmpty(name) ? clazz.getSimpleName() : name;
	}

	public static String getBoNamespace(Class<?> clazz) {
		BoEntity annot = clazz.getAnnotation(BoEntity.class);
		if (annot == null) {
			return SYSTEM_BO_NAMESPACE;
		}
		return annot.namespace();
	}

	public static String getFullName(String namespace, String name) {
		if (!StringUtils.isEmpty(namespace)) {
			StringBuilder sb = new StringBuilder();
			sb.append(namespace.replace(".", "_"));
			sb.append("_");
			sb.append(name);
			return sb.toString();
		}
		return name;
	}
}
