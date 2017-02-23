package com.huoyun.core.jpa;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.changetracking.DeferredChangeDetectionPolicy;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.dynamic.DynamicTypeBuilder;
import org.eclipse.persistence.jpa.dynamic.DynamicIdentityPolicy;

public class CustomDynamicTypeBuilder extends DynamicTypeBuilder {

	public CustomDynamicTypeBuilder(Class<?> dynamicClass,
			DynamicType parentType, String... tableNames) {
		super(dynamicClass, parentType, tableNames);
	}

	public CustomDynamicTypeBuilder(DynamicClassLoader dcl,
			ClassDescriptor descriptor, DynamicType parentType) {
		super(dcl, descriptor, parentType);
	}

	@Override
	protected void configure(ClassDescriptor descriptor, String... tableNames) {
		super.configure(descriptor, tableNames);

		if (descriptor.getCMPPolicy() == null) {
			descriptor.setCMPPolicy(new DynamicIdentityPolicy());
		}

		descriptor.setObjectChangePolicy(new DeferredChangeDetectionPolicy());
	}

}
