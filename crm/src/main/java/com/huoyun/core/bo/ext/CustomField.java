package com.huoyun.core.bo.ext;

import java.math.BigDecimal;

import com.huoyun.core.bo.utils.BusinessObjectUtils;

public enum CustomField {
	STR("STR", ExtUtils.STR_COUNT), NUM("NUM", ExtUtils.NUM_COUNT) {
		@Override
		public Class<?> getFieldType() {
			return BigDecimal.class;
		}
	},
	TXT("TXT", ExtUtils.TXT_COUNT), FLAG("FLAG", 9), ID(BusinessObjectUtils.EXT_TABLE_ID, 1) {
		@Override
		public Class<?> getFieldType() {
			return Long.class;
		}
	},
	PID(BusinessObjectUtils.EXT_TABLE_PID, 1) {
		@Override
		public Class<?> getFieldType() {
			return Long.class;
		}
	},
	TENANT_ID(BusinessObjectUtils.EXT_TABLE_TENANT_ID, 1) {
		@Override
		public Class<?> getFieldType() {
			return Long.class;
		}
	};

	CustomField(String prefix, int num) {
		this.prefix = prefix;
		this.num = num;

	}

	String prefix;
	int num;

	public String getPrefix() {
		return prefix;
	}

	public int getNum() {
		return num;
	}

	public Class<?> getFieldType() {
		return String.class;
	}

//	public static CustomField fromBaseType(BOBaseType baseType) {
//		switch (baseType) {
//		case DATE_TIME:
//			return CustomField.STR;
//		case STRING:
//			return CustomField.STR;
//		case TEXT:
//			return CustomField.TXT;
//		case INTEGER:
//		case LONG:
//		case DECIMAL:
//		case DOUBLE:
//		case BOOLEAN:
//		case TIME:
//			return CustomField.NUM;
//		default:
//			return CustomField.STR;
//		}
//	}

//	private static Pattern pattern = Pattern.compile("([A-Z]+)(\\d*)");
//
//	public static CustomField parse(String udeColumn) {
//		Matcher m = pattern.matcher(udeColumn);
//		if (m.find()) {
//			return Cache.get(m.group(1));
//		}
//		return null;
//	}
//
//	private static class Cache {
//		private static Map<String, CustomField> cache = new ConcurrentSkipListMap<>(
//				String.CASE_INSENSITIVE_ORDER);
//		static {
//			for (CustomField t : CustomField.values()) {
//				cache.put(t.getPrefix(), t);
//			}
//		}
//
//		public static CustomField get(String key) {
//			return cache.get(key);
//		}
//	}
}
