package com.huoyun.core.bo.ext.impl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.ext.ColumnAllocFactory;
import com.huoyun.core.bo.ext.CustomField;
import com.huoyun.core.bo.ext.ExtErrorCode;
import com.huoyun.core.bo.ext.UDEAllocInfo;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.exception.BusinessException;

public class ColumnAllocFactoryImpl implements ColumnAllocFactory {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ColumnAllocFactoryImpl.class);

	private final Map<CustomField, Deque<String>> columns = new HashMap<>();
	private UDEAllocInfo allocInfo;

	public ColumnAllocFactoryImpl(UDEAllocInfo allocInfo)
			throws BusinessException {
		this.allocInfo = allocInfo;
		this.initilize();
		for (String strColumn : this.allocInfo.getStrColumnArray()) {
			this.reserve(strColumn);
		}

		for (String numColumn : this.allocInfo.getNumColumnArray()) {
			this.reserve(numColumn);
		}
		for (String txtColumn : this.allocInfo.getTxtColumnArray()) {
			this.reserve(txtColumn);
		}
	}

	@Override
	public String alloc(PropertyType type) throws BusinessException {

		Deque<String> freeColumns = this.columns.get(CustomField
				.fromBaseType(type));

		if (freeColumns.isEmpty()) {
			LOGGER.error("No enough columns");
			throw new BusinessException(ExtErrorCode.UDE_NO_ENOUGH_COLUMN);
		}

		String column = freeColumns.pollFirst();
		return column;
	}

	@Override
	public void append(PropertyType type, String columnName)
			throws BusinessException {
		CustomField field = CustomField.fromBaseType(type);

		switch (field) {
		case STR:
			if (StringUtils.isEmpty(this.allocInfo.getStrColumns())) {
				this.allocInfo.setStrColumns(columnName);
			} else {
				this.allocInfo.setStrColumns(this.allocInfo.getStrColumns()
						+ "," + columnName);
			}
			break;
		case NUM:
			if (StringUtils.isEmpty(this.allocInfo.getNumColumns())) {
				this.allocInfo.setNumColumns(columnName);
			} else {
				this.allocInfo.setNumColumns(this.allocInfo.getNumColumns()
						+ "," + columnName);
			}
			break;
		case TXT:

			if (StringUtils.isEmpty(this.allocInfo.getTxtColumns())) {
				this.allocInfo.setTxtColumns(columnName);
			} else {
				this.allocInfo.setTxtColumns(this.allocInfo.getTxtColumns()
						+ "," + columnName);
			}
			break;
		default:
			break;
		}

	}

	private void reserve(String columnName) throws BusinessException {
		Deque<String> freeColumns = this.columns.get(CustomField
				.parse(columnName));

		if (freeColumns == null) {
			LOGGER.error("No free columns for column {}", columnName);
			throw new BusinessException("No free columns for column "
					+ columnName);
		}

		if (freeColumns.isEmpty()) {
			LOGGER.error("No enough columns");
			throw new BusinessException(ExtErrorCode.UDE_NO_ENOUGH_COLUMN);
		}

		freeColumns.remove(columnName);
	}

	private void initilize() {
		Deque<String> stringColumns = new ArrayDeque<>();
		Deque<String> numberColumns = new ArrayDeque<>();
		Deque<String> textColumns = new ArrayDeque<>();

		for (int ii = 1; ii <= CustomField.STR.getNum(); ii++) {
			stringColumns.add(CustomField.STR.getPrefix() + ii);
		}

		for (int ii = 1; ii <= CustomField.NUM.getNum(); ii++) {
			numberColumns.add(CustomField.NUM.getPrefix() + ii);
		}

		for (int ii = 1; ii <= CustomField.TXT.getNum(); ii++) {
			textColumns.add(CustomField.TXT.getPrefix() + ii);
		}

		columns.put(CustomField.STR, stringColumns);
		columns.put(CustomField.NUM, numberColumns);
		columns.put(CustomField.TXT, textColumns);
	}

}
