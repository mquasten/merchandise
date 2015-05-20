package de.mq.merchandise.util.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;







import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.vaadin.data.Item;

public class ItemToDomainConverterImpl<T> implements Converter<Item, T> {
	
	private final Class<? extends T> clazz; 
	private final  Enum<?>[] cols;
	public ItemToDomainConverterImpl(Class<? extends T> clazz, Class<? extends Enum<?>> colClass) {
		this.clazz=clazz;
		final Method method = ReflectionUtils.findMethod(colClass, "values");
		
		method.setAccessible(true);
		
		cols = (Enum<?>[]) ReflectionUtils.invokeMethod(method, null);
		
	}

	@Override
	public T convert(final Item source) {
		if( source == null){
			return null;
		}
		final T subject = BeanUtils.instantiateClass(clazz);
		Arrays.asList(cols).forEach(col -> {
			final Field field = ReflectionUtils.findField(subject.getClass(), StringUtils.uncapitalize(col.name()));
			Assert.notNull(field , "Field not found in Type: " + subject.getClass());
			field.setAccessible(true);
			if(source.getItemProperty(col) != null){
				ReflectionUtils.setField(field, subject, source.getItemProperty(col).getValue());
			}
			
		} );
		
		
		return subject;
	}

}
