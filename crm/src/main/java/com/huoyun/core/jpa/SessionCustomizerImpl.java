package com.huoyun.core.jpa;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicTypeBuilder;
import org.eclipse.persistence.sessions.Session;

import com.huoyun.core.bo.ext.CustomField;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.core.classloader.CachedClassLoader;
import com.huoyun.core.classloader.impl.CachedClassLoaderImpl;

public class SessionCustomizerImpl implements SessionCustomizer {

	private Session session = null;

	private CachedClassLoader cachedClassLoader = CachedClassLoaderImpl
			.instance();

	@Override
	public void customize(Session session) throws Exception {
		this.session = session;

		DynamicClassLoader dcl = DynamicClassLoader.lookup(session);
		buildUDE(dcl);
	}

	private void buildUDE(DynamicClassLoader dcl) {

		/*
		 * * cannot access UDO list view after EclipeseLink was upgraded to
		 * 2.6.2, the solution is to use static entity rather than dynamic
		 * entity JIRA:AWB-43780 Change-Id:
		 * I991638479fd4edd028648d264f9b04b88984a82c
		 */
		List<DynamicTypeBuilder> typeBuilders = new ArrayList<>();

		for (Class<?> extClazz : this.cachedClassLoader.getExtBoClassCache()
				.values()) {
			String tableName = BusinessObjectUtils.getExtTableName(extClazz);
			typeBuilders.add(createTypeBuilder(dcl, tableName));
		}

		for (DynamicTypeBuilder tb : typeBuilders) {
			addUDEFieldMapping(tb, CustomField.ID);
			addUDEFieldMapping(tb, CustomField.PID);
			addUDEFieldMapping(tb, CustomField.STR);
			addUDEFieldMapping(tb, CustomField.NUM);
			addUDEFieldMapping(tb, CustomField.TXT);
			tb.setPrimaryKeyFields(CustomField.ID.getPrefix());
			tb.configureSequencing(tb.getType().getClassName() + "_SEQ",
					CustomField.ID.getPrefix());
		}
		for (DynamicTypeBuilder builder : typeBuilders) {
			session.getProject().addDescriptor(
					builder.getType().getDescriptor());
		}
	}

	private DynamicTypeBuilder createTypeBuilder(DynamicClassLoader dcl,
			String tableName) {

		/* Convention: UDE class name equals to its table name */
		Class<?> clazz = dcl.createDynamicClass(tableName);

		return new CustomDynamicTypeBuilder(clazz, null, tableName);
	}

	private void addUDEFieldMapping(DynamicTypeBuilder tb, CustomField fieldEnum) {

		if (fieldEnum.getNum() > 1) {
			for (int ii = 1; ii <= fieldEnum.getNum(); ii++) {
				String columnName = fieldEnum.getPrefix() + ii;
				Class<?> fieldType = fieldEnum.getFieldType();
				tb.addDirectMapping(columnName, fieldType, columnName);
			}
		} else {
			String columnName = fieldEnum.getPrefix();
			Class<?> fieldType = fieldEnum.getFieldType();
			tb.addDirectMapping(columnName, fieldType, columnName);
		}

	}

}
