package de.mq.merchandise.util.chouchdb;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.StringUtils;



	class SimpleCouchViewRowImpl implements CouchViewResultRow {
	
		private Object key;
		
		private Object value;
		
		private String id;
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#id()
		 */
		@Override
		public final String id()  {
			return id;
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleKey()
		 */
		@Override
		public final String singleKey() {
			
			if (key instanceof Map) {
				throw new IllegalArgumentException("Key is a Composed Key");
			}
			return (String) key;
			
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleValue()
		 */
		@Override
		public final String singleValue() {
			if (key instanceof Map) {
				throw new IllegalArgumentException("Key is a Composed Key");
			}
			
			return (String) value;
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedKey()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public final Map<String,? extends Object> composedKey() {
			composedKeyGuard();
			return Collections.unmodifiableMap((Map<String, ? extends Object>) key);
		}

		private void composedKeyGuard() {
			if (!(key instanceof Map<?,?>)) {
				throw new IllegalArgumentException("Key is not composed");
			}
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedValue()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public final Map<String,? extends Object> composedValue() {
			composedValueGuard();
			return Collections.unmodifiableMap((Map<String, ? extends Object>) value);
		}

		private void composedValueGuard() {
			if (!(value instanceof Map<?,?>)) {
				throw new IllegalArgumentException("Value is not  composed.");
			}
		}
		
		public final <T> T composedValue(final Class<? extends T> targetClass) {
			composedValueGuard();
			return createBean(targetClass, composedValue());
		}
		
		public final <T> T composedKey(final Class<? extends T> targetClass) {
			composedKeyGuard();
			return createBean(targetClass, composedKey());
		}

		private <T> T createBean(final Class<? extends T> targetClass, final Map<String, ?> values) {
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
