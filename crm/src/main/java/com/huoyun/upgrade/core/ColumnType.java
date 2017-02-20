package com.huoyun.upgrade.core;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

public enum ColumnType {
	NVARCHAR("nvarchar", java.lang.String.class),

	VARCHAR("varchar", java.lang.String.class),

	INTEGER("int", java.lang.Integer.class),

	TINYINT("bit", java.lang.Boolean.class),

	SMALLINT("smallint", java.lang.Integer.class),

	BIGINT("bigint", java.lang.Long.class),

	DOUBLE("numeric", java.lang.Double.class),

	REAL("real", java.lang.Float.class),

	FLOAT("float", java.lang.Double.class),

	DECIMAL("decimal", java.math.BigDecimal.class),

	DATE("datetime", DateTime.class),

	// ?

	TIME("time", java.sql.Time.class),

	TIMESTAMP("timestamp", java.sql.Timestamp.class),

	BINARY("binary", byte[].class),

	VARBINARY("varbinary", byte[].class),

	NCLOB("ntext", java.sql.NClob.class),

	BLOB("ntext", java.sql.Blob.class),

	CLOB("", java.sql.Clob.class),

	SHORTTEXT("", java.lang.String.class),

	// ?

	CHAR("char", java.lang.Character.class),

	NCHAR("nchar", java.lang.String.class),

	DATETIME("datetime", DateTime.class),

	TEXT("text", java.lang.String.class);

	private ColumnType(String mssqlType, Class<?> javaType) {
		this.MSSQL_TYPE = mssqlType;
		this.JAVA_TYPE = javaType;
	}

	private String MSSQL_TYPE;

	@SuppressWarnings("rawtypes")
	private Class JAVA_TYPE;

	public String getMSSQLValue() {
		return this.MSSQL_TYPE;
	}

	public String getHANAValue() {
		return this.toString();
	}

	public Class getJavaType() {
		return this.JAVA_TYPE;
	}

	public static boolean isIgnoreType(ColumnType type) {
		Set<ColumnType> ignore = new HashSet<ColumnType>();
		ignore.add(INTEGER);
		ignore.add(TINYINT);
		ignore.add(SMALLINT);
		ignore.add(BIGINT);
		ignore.add(DOUBLE);
		ignore.add(TIMESTAMP);
		ignore.add(TIME);
		ignore.add(DATE);
		ignore.add(DATETIME);
		ignore.add(NCLOB);
		ignore.add(CLOB);
		ignore.add(BLOB);
		ignore.add(TEXT);
		ignore.add(DECIMAL);

		return ignore.contains(type);
	}

	public static ColumnType valueOfString(String columnTypeStr) {
		ColumnType[] values = ColumnType.values();
		for (ColumnType columnType : values) {
			if (columnType.name().equalsIgnoreCase(columnTypeStr)) {
				return columnType;
			}
		}
		throw new RuntimeException("Cant match type " + columnTypeStr);
	}

	public static ColumnType valueOfClass(Class clz) {
		if (clz.isPrimitive()) {
			clz = toWrapsType(clz);
		}
		ColumnType[] values = ColumnType.values();
		for (ColumnType columnType : values) {
			if (columnType.JAVA_TYPE == clz) {
				return columnType;
			}
		}
		if (clz.isEnum()) {
			return INTEGER;
		}
		throw new RuntimeException("Cant match type " + clz.getName());
	}

	public static ColumnType getColumnTypeByMSSQL(String columnTypeStr) {
		for (ColumnType enumType : ColumnType.values()) {
			if (columnTypeStr.equals(enumType.MSSQL_TYPE)) {
				return enumType;
			}
		}
		throw new RuntimeException("Cant match type " + columnTypeStr);
	}

	private static Class toWrapsType(Class clz) {
		if (clz == byte.class) {
			return Byte.class;
		} else if (clz == boolean.class) {
			return Boolean.class;
		} else if (clz == char.class) {
			return Character.class;
		} else if (clz == short.class) {
			return Short.class;
		} else if (clz == int.class) {
			return Integer.class;
		} else if (clz == long.class) {
			return Long.class;
		} else if (clz == float.class) {
			return Float.class;
		} else if (clz == double.class) {
			return Double.class;
		}
		return String.class;
	}

	public static boolean isColumnType(String token) {
		ColumnType[] values = ColumnType.values();
		for (ColumnType columnType : values) {
			if (columnType.name().equalsIgnoreCase(token)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isLOB(ColumnType c) {
		Set<ColumnType> varchar = new HashSet<ColumnType>();
		varchar.add(NCLOB);
		varchar.add(CLOB);
		varchar.add(BLOB);
		return varchar.contains(c);
	}

	public static boolean isVarchar(ColumnType c) {
		Set<ColumnType> varchar = new HashSet<ColumnType>();
		varchar.add(NVARCHAR);
		varchar.add(VARCHAR);
		varchar.add(CHAR);
		varchar.add(NCHAR);
		varchar.add(SHORTTEXT);
		varchar.add(TEXT);
		return varchar.contains(c);
	}
}
