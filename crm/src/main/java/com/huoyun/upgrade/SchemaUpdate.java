package com.huoyun.upgrade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.huoyun.core.bo.annotation.ColumnRule;
import com.huoyun.core.bo.annotation.ExtTable;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.core.classloader.CachedClassLoader;
import com.huoyun.upgrade.core.Column;
import com.huoyun.upgrade.core.ColumnHashMap;
import com.huoyun.upgrade.core.ColumnType;
import com.huoyun.upgrade.core.KEYColumn;
import com.huoyun.upgrade.core.KEYType;
import com.huoyun.upgrade.core.SequencesDefinition;
import com.huoyun.upgrade.core.TableDefinition;
import com.huoyun.upgrade.core.builder.ScriptBuilder;

public class SchemaUpdate {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SchemaUpdate.class);

	private static final String EXT_TABLE_COLUMN_ID = "ID";
	private static final String EXT_TABLE_SEQUENCE_SUFFIX = "_SEQ";
	private static final int STRING_DEFAULT_LEN = 255;
	private static final int DECIMAL_DEFAULT_LEN = 25;

	private JdbcTemplate jdbcTemplate;
	private CachedClassLoader classLoader;
	private ScriptBuilder scriptBuilder;
	private String schema = "crm";

	public SchemaUpdate(JdbcTemplate jdbcTemplate,
			CachedClassLoader classLoader, ScriptBuilder scriptBuilder)
			throws SQLException {
		this.jdbcTemplate = jdbcTemplate;
		this.classLoader = classLoader;
		this.scriptBuilder = scriptBuilder;
		this.initialize();
	}

	private void initialize() {
		List<TableDefinition> tableDefs = new ArrayList<>();
		for (String key : this.classLoader.getExtBoClassCache().keySet()) {
			tableDefs.add(this.getExtTableDefinition(this.classLoader
					.getExtBoClassCache().get(key)));
		}

		for (TableDefinition table : tableDefs) {
			String tableExistSql = this.scriptBuilder.tableExists(table);
			boolean exists = this.jdbcTemplate.queryForObject(tableExistSql,
					boolean.class);
			if (exists) {
				LOGGER.info(String.format("Table %s exists.", table.getName()));
				continue;
			}
			String sql = this.scriptBuilder.createTable(table);
			LOGGER.info(sql);
			this.jdbcTemplate.execute(sql);
		}
	}

	private TableDefinition getExtTableDefinition(Class<?> clz) {
		ExtTable extensionTable = AnnotationUtils.findAnnotation(clz,
				ExtTable.class);
		if (extensionTable == null) {
			return null;
		}
		TableDefinition table = new TableDefinition();
		table.setSchema(this.schema);
		table.setName(BusinessObjectUtils.getExtTableName(clz));
		table.setColumns(getExtTableColumns(table.getName(),
				extensionTable.columnRules()));
		table.setKeys(getExtKeyColumns());
		table.setSeq(getExtTableSequence(table.getName()));
		return table;
	}

	private List<KEYColumn> getExtKeyColumns() {
		List<KEYColumn> keyColumns = new ArrayList<>();
		KEYColumn column = new KEYColumn();
		column.setKeyType(KEYType.PK);
		column.setName(EXT_TABLE_COLUMN_ID);
		keyColumns.add(column);
		return keyColumns;
	}

	private SequencesDefinition getExtTableSequence(String tableName) {
		SequencesDefinition sequence = new SequencesDefinition();
		sequence.setSequenceName(tableName + EXT_TABLE_SEQUENCE_SUFFIX);
		sequence.setMinValue(1);
		sequence.setIncrementBy(1);
		sequence.setStartNumber(1);
		return sequence;
	}

	private ColumnHashMap<String, Column> getExtTableColumns(String tableName,
			ColumnRule[] columnRules) {
		ColumnHashMap<String, Column> columnMap = new ColumnHashMap<String, Column>();
		Column idColumn = new Column();
		idColumn.setName(EXT_TABLE_COLUMN_ID);
		idColumn.setColumnType(ColumnType.BIGINT);
		idColumn.setNullable(false);
		idColumn.setTableName(tableName);
		columnMap.put(idColumn.getName(), idColumn);

		for (ColumnRule columnRule : columnRules) {
			String valueType = columnRule.valueType();
			ColumnType columnType = null;
			int length = 0;

			if (ColumnType.isColumnType(valueType)) {
				columnType = ColumnType.valueOfString(valueType);
			} else if (StringUtils.containsAny(valueType,
					new char[] { '(', ')' })) {
				columnType = ColumnType.valueOfString(StringUtils
						.substringBefore(valueType, "(").trim());
				String lengthValue = StringUtils.substringBetween(valueType,
						"(", ")");
				if (lengthValue.equalsIgnoreCase("max")) {
					length = Integer.MAX_VALUE;
				} else {
					length = Integer.valueOf(lengthValue);
				}
			}
			if (columnType == ColumnType.NVARCHAR && length == 0) {
				length = STRING_DEFAULT_LEN;
			}
			Integer scale = null;
			if (columnType == ColumnType.DECIMAL) {
				scale = 6;
				length = length > 0 ? length : DECIMAL_DEFAULT_LEN;
			}
			String prefix = columnRule.prefix().toUpperCase();
			for (int i = 1; i <= columnRule.count(); i++) {
				Column column = new Column();
				column.setTableName(tableName);
				column.setName(prefix + i);
				column.setColumnType(columnType);
				column.setLength(length);
				column.setNullable(true);
				column.setScale(scale);
				columnMap.put(column.getName(), column);
			}
		}
		return columnMap;
	}

}
