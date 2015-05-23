package de.mq.merchandise.util.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.vaadin.data.Item;

import de.mq.merchandise.util.Mapper;

public class ItemToDomainMapperImpl<T> implements Mapper<Item, T> {
	
	private final Class<? extends T> clazz; 
	private final  Enum<?>[] cols;
	public ItemToDomainMapperImpl(Class<? extends T> clazz, Class<? extends Enum<?>> colClass) {
		this.clazz=clazz;
		final Method method = ReflectionUtils.findMethod(colClass, "values");
		
		method.setAccessible(true);
		
		cols = (Enum<?>[]) ReflectionUtils.invokeMethod(method, null);
		
	}

	@Override
	public T convert(final Item item) {
		if( item == null){
			return null;
		}
		return mapInto(item, BeanUtils.instantiateClass(clazz));
	}

	@Override
	public T mapInto(final Item item, final T domain) {
		Arrays.asList(cols).forEach(col -> {
			final Field field = ReflectionUtils.findField(domain.getClass(), StringUtils.uncapitalize(col.name()));
			Assert.notNull(field , "Field not found in Type: " + domain.getClass());
			field.setAccessible(true);
			if(item.getItemProperty(col) != null){
				ReflectionUtils.setField(field, domain, item.getItemProperty(col).getValue());
			}
			
		} );
		return domain;
	}

}
