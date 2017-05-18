package com.huoyun.core.bo.utils;

import javax.persistence.Table;

import org.springframework.util.StringUtils;

import com.huoyun.core.bo.annotation.BoEntity;

public class BusinessObjectUtils {

	public final static String SYSTEM_BO_NAMESPACE = "com.huoyun.sbo";
	public final static String EXTENSION_BO_NAMESPACE = "ext.default";
	public static final String EXT_TABLE_NAME_SUFFIX = "_EXT";
    public final static String EXT_TABLE_ID = "ID";
    public final static String EXT_TABLE_PID = "PARENT_ID";
    public final static String EXT_TABLE_TENANT_CODE = "TENANT_CODE";

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

	public static String getExtTableName(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		String tableName = null;
		if (clazz.getAnnotation(Table.class) != null) {
			tableName = clazz.getAnnotation(Table.class).name();
		}
		if (StringUtils.isEmpty(tableName)) {
			tableName = clazz.getSimpleName();
		}
		if (tableName != null) {
			return tableName.toUpperCase() + EXT_TABLE_NAME_SUFFIX;
		} else {
			return null;
		}
	}
}
