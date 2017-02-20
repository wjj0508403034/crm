package com.huoyun.upgrade.core.builder.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.upgrade.core.Column;
import com.huoyun.upgrade.core.ColumnHashMap;
import com.huoyun.upgrade.core.ColumnType;
import com.huoyun.upgrade.core.KEYColumn;
import com.huoyun.upgrade.core.TableDefinition;
import com.huoyun.upgrade.core.UniqueColumn;
import com.huoyun.upgrade.core.UniqueDefinition;
import com.huoyun.upgrade.core.builder.ScriptBuilder;

public class MySqlScriptBuilder implements ScriptBuilder {
	public final String LINEBREAK = "\r\n";
	public final String COMMA = ",";

	@Override
	public String createTable(TableDefinition table) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(table.getName()).append(" (");

		ColumnHashMap<String, Column> columns = table.getColumns();

		String sp = StringUtils.EMPTY;
		for (Map.Entry<String, Column> entry : columns.entrySet()) {
			sb.append(sp);
			sb.append(this.LINEBREAK);
			sb.append('\t');
			Column c = entry.getValue();
			sb.append(c.getName()).append(" ")
					.append(c.getColumnType().getMSSQLValue());

			if (c.hasLength() && !ColumnType.isIgnoreType(c.getColumnType())) {
				sb.append("(").append(c.getLength()).append(")");
			}

			if (c.getColumnType() == ColumnType.DECIMAL && c.hasLength()
					&& c.hasScale()) {
				sb.append("(").append(c.getLength()).append(",")
						.append(c.getScale()).append(")");
			}

			if (c.isNullable()) {
				sb.append(" NULL");
			} else {
				sb.append(" NOT NULL");
			}

			if (StringUtils.isNotEmpty(c.getDefaultValue())) {
				String defaultvalue = c.getDefaultValue();
				if (ColumnType.isVarchar(c.getColumnType())) {
					defaultvalue = "\'" + defaultvalue.toUpperCase() + "\'";
				}
				sb.append(" DEFAULT ").append(defaultvalue);
			}

			sp = this.COMMA;
		}

		List<KEYColumn> pk = table.getPK();
		if (pk != null && pk.size() > 0) {
			String sp1 = StringUtils.EMPTY;
			sb.append(sp).append(this.LINEBREAK);
			sb.append('\t');
			sb.append("PRIMARY KEY (");
			for (KEYColumn keyColumn : pk) {
				sb.append(sp1);
				sb.append(keyColumn.getName());
				sp1 = this.COMMA;
			}
			sb.append(")");
		}

		sb.append(this.LINEBREAK)
				.append(") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;");

		List<UniqueDefinition> uniques = table.getUnique();
		for (UniqueDefinition ud : uniques) {
			String addUniqueStr = addUnique(ud);
			sb.append(addUniqueStr);
		}

		return sb.toString();
	}

	@Override
	public String addUnique(UniqueDefinition unique) {
		StringBuilder addUniqueStr = new StringBuilder();
		addUniqueStr.append("ALTER TABLE ").append(unique.getTableName());
		addUniqueStr.append(this.LINEBREAK);
		addUniqueStr.append(" ADD CONSTRAINT ");
		addUniqueStr.append(unique.getConstraintName());
		addUniqueStr.append(" UNIQUE");
		addUniqueStr.append(" (");
		UniqueColumn[] columnNames = unique.getColumnNames();
		String sp1 = StringUtils.EMPTY;
		for (UniqueColumn column : columnNames) {
			addUniqueStr.append(sp1);
			addUniqueStr.append(column.getColumnName());
			sp1 = this.COMMA;
		}

		addUniqueStr.append(" );");
		return addUniqueStr.toString();
	}

	@Override
	public String dropTable(TableDefinition table) {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE ");
		sb.append(table.getName());
		sb.append(";");
		sb.append(this.LINEBREAK);
		return sb.toString();
	}

	@Override
	public String tableExists(TableDefinition table) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) > 0 FROM information_schema.tables ");
		sb.append(this.LINEBREAK);
		sb.append("WHERE table_schema = '");
		sb.append(table.getSchema());
		sb.append("' AND table_name = '");
		sb.append(table.getName());
		sb.append("' LIMIT 1;");
		sb.append(this.LINEBREAK);
		return sb.toString();
	}

}
