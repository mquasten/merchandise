package de.mq.merchandise.util.chouchdb.support;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

public class MapCopyTemplate {

	
      <T> T createShallowCopyFieldsFromMap(final Class<? extends T> targetClass, final Map<String, ? extends Object> values) {
		final T target = BeanUtils.instantiateClass(targetClass);
		ReflectionUtils.doWithFields(targetClass, new FieldCallback() {
			
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if(!field.isAnnotationPresent(de.mq.merchandise.util.chouchdb.Field.class)) {
					return;
				}
				
				String name = field.getName();
				if(StringUtils.hasText(field.getAnnotation(de.mq.merchandise.util.chouchdb.Field.class).value())){
					name=field.getAnnotation(de.mq.merchandise.util.chouchdb.Field.class).value();
				}
				
				if(!(values.containsKey(name))){
					return;
				}
				@SuppressWarnings("unchecked")
				final Converter<Object,Object>  converter = (Converter<Object, Object>) BeanUtils.instantiateClass(field.getAnnotation(de.mq.merchandise.util.chouchdb.Field.class).converter());
				field.setAccessible(true);
				
				field.set(target, converter.convert(values.get(name)));
				
			}
		});
		return target;
	}
}
