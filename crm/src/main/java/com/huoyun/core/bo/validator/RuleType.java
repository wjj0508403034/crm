package com.huoyun.core.bo.validator;

import com.huoyun.core.bo.validator.constraints.EndsWith;
import com.huoyun.core.bo.validator.constraints.StartsWith;
import com.huoyun.core.bo.validator.constraints.StringLength;
import com.huoyun.core.bo.validator.constraints.StringMaxLength;
import com.huoyun.core.bo.validator.constraints.StringMinLength;
import com.huoyun.core.bo.validator.constraints.Equal;
import com.huoyun.core.bo.validator.constraints.Contains;
import com.huoyun.core.bo.validator.constraints.LargeThan;
import com.huoyun.core.bo.validator.constraints.LessThan;
import com.huoyun.core.bo.validator.constraints.Between;
import com.huoyun.core.bo.validator.constraints.NotBetween;
import com.huoyun.core.bo.validator.constraints.NotContains;

public enum RuleType {

	StartsWith(StartsWith.class), EndsWith(EndsWith.class), Length(StringLength.class), MinLength(
			StringMinLength.class), MaxLength(StringMaxLength.class), NotContains(NotContains.class), Contains(
					Contains.class), LargeThan(LargeThan.class), LessThan(
							LessThan.class), Equal(Equal.class), Between(Between.class), NotBetween(NotBetween.class);

	private final Class<?> validatorClass;

	<T extends Validator> RuleType(Class<T> klass) {
		this.validatorClass = klass;
	}

	@SuppressWarnings("unchecked")
	public <T extends Validator> Class<T> getValidatorClass() {
		return (Class<T>) validatorClass;
	}

}
