package de.mq.merchandise.util.support;

import java.util.Collection;
import java.util.Map;

public interface BeanResolver {

	<T> T resolve(final Map<Class<?>, Object> beans, final Class<? extends T> clazz);

	<T> T resolve(final Class<? extends T> clazz);

	<T> Collection<T> resolveAll(Class<? extends T> clazz, @SuppressWarnings("unchecked") T...  beans);

	

}