package com.huoyun.upgrade.core;

import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("hiding")
public class ColumnHashMap<String, Column> extends
		LinkedHashMap<String, Column> {

	private static final long serialVersionUID = -3887020896508571167L;

	@Override
	public boolean containsKey(Object key) {
		Set<String> keySet = this.keySet();
		for (String k : keySet) {
			if (StringUtils.containsAny((java.lang.String) key,
					new char[] { '\"' })) {
				key = StringUtils
						.substringBetween((java.lang.String) key, "\"");
			}

			if (k.toString().equalsIgnoreCase((java.lang.String) key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Column get(Object key) {
		if (StringUtils
				.containsAny((java.lang.String) key, new char[] { '\"' })) {
			key = StringUtils.substringBetween((java.lang.String) key, "\"");
		}
		return super.get(key);
	}

	public Column getColumnNoSubstring(Object key) {
		return super.get(key);
	}
}
