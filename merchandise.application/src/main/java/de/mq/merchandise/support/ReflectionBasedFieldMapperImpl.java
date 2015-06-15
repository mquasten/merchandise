package de.mq.merchandise.support;

import java.lang.reflect.Field;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public  class ReflectionBasedFieldMapperImpl {

	

	protected final void assign(final String fieldName, final Object target, final Object value) {
		final Field field = ReflectionUtils.findField(target.getClass(), fieldName);
		Assert.notNull(field, String.format("Field %s not found ", fieldName));
		ReflectionUtils.setField(field, target, value);
	}

}