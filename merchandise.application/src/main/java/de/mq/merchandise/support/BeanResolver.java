package de.mq.merchandise.support;

import java.util.Map;

public interface BeanResolver {

	<T> T resolve(final Map<Class<?>, Object> beans, final Class<? extends T> clazz);

	<T> T resolve(final Class<? extends T> clazz);

	

}