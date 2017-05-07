package com.huoyun.core.bo.metadata.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.OneToMany;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.metadata.NodeMeta;

public class NodeMetaImpl implements NodeMeta {

	@SuppressWarnings("unchecked")
	public NodeMetaImpl(Field field) {
		OneToMany oneToManyAnno = field.getAnnotation(OneToMany.class);
		this.mappedBy = oneToManyAnno.mappedBy();

		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
			if (actualTypes.length > 0) {
				this.nodeClass = (Class<BusinessObject>) actualTypes[0];
			}
		}

	}

	private String mappedBy;
	private Class<BusinessObject> nodeClass;

	@Override
	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	@Override
	public Class<BusinessObject> getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(Class<BusinessObject> nodeClass) {
		this.nodeClass = nodeClass;
	}
}
