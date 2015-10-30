package de.mq.merchandise.util.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.vaadin.data.Item;

import de.mq.merchandise.support.Mapper;

public class ItemToDomainConverterImpl<T> implements Converter<Item, T>, Mapper<Item, T> {

	private final Class<? extends T> clazz;
	private final Enum<?>[] cols;

	private final Map<Enum<?>, Class<?>> childs = new HashMap<>();

	public ItemToDomainConverterImpl(Class<? extends T> clazz, Class<? extends Enum<?>> colClass) {
		this.clazz = clazz;
		final Method method = ReflectionUtils.findMethod(colClass, "values");

		method.setAccessible(true);

		cols = (Enum<?>[]) ReflectionUtils.invokeMethod(method, null);

	}

	public ItemToDomainConverterImpl(Class<? extends T> clazz, final Enum<?>[] cols) {
		this.clazz = clazz;
		this.cols = cols;
	}

	public final ItemToDomainConverterImpl<T> withChild(final Enum<?> col, final Class<?> clazz) {
		childs.put(col, clazz);
		return this;
	}

	@Override
	public T convert(final Item item) {
		if (item == null) {
			return null;
		}
		final T domain = BeanUtils.instantiateClass(clazz);
		return mapInto(item, domain);
	}

	@Override
	public final T mapInto(final Item item, final T domain) {
		Arrays.asList(cols).forEach(col -> {
			handleItem(item, domain, col);

		});
		return domain;
	}

	private void handleItem(final Item item, final T domain, Enum<?> col) {
		final Field field = ReflectionUtils.findField(AopUtils.getTargetClass(domain), StringUtils.uncapitalize(col.name()));
		Assert.notNull(field, "Field not found in Type: " + domain.getClass());
		field.setAccessible(true);
		
		if (item.getItemProperty(col) == null) {
			
			return;
		}



		if (childs.containsKey(col)) {
		
			final Object entity = BeanUtils.instantiateClass(childs.get(col));
			ReflectionUtils.doWithFields(entity.getClass(), f -> { f.setAccessible(true); f.set(entity,item.getItemProperty(col).getValue());}, f -> f.isAnnotationPresent(Id.class));
			ReflectionUtils.setField(field, domain, entity);
			return;
		}


		ReflectionUtils.setField(field, domain, item.getItemProperty(col).getValue());
	

	}

}
