package de.mq.merchandise.util.chouchdb.support;

import java.util.Collections;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

@SuppressWarnings("unused")
class SimpleMapBasedResultRowImpl implements MapBasedResultRow {

	private Object key;

	private Object value;

	private String id;
	
	@JsonIgnore
	final ConfigurableConversionService conversionService = new DefaultConversionService();

	@JsonIgnore
	private MapCopyOperations mapCopyTemplate = new MapCopyTemplate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#id()
	 */
	@Override
	public final String id() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleKey()
	 */
	
	@Override
	public final <T> T singleKey(Class<? extends T> clazz) {

		if (key instanceof Map) {
			throw new IllegalArgumentException("Key is a Composed Key");
		}

		
		return conversionService.convert(key, clazz);
		
		
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleValue()
	 */
	@Override
	public final <T> T singleValue(Class<? extends T> clazz) {
		if (value instanceof Map) {
			throw new IllegalArgumentException("Value is a Composed Key");
		}

		return conversionService.convert(value, clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedKey()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final Map<String, Object> composedKey() {
		composedKeyGuard();
		return Collections.unmodifiableMap((Map<String, ? extends Object>) key);
	}

	private void composedKeyGuard() {
		if (!(key instanceof Map<?, ?>)) {
			throw new IllegalArgumentException("Key is not composed");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedValue()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final Map<String, Object> composedValue() {
		composedValueGuard();
		return Collections.unmodifiableMap((Map<String, ? extends Object>) value);
	}

	private void composedValueGuard() {
		if (!(value instanceof Map<?, ?>)) {
			throw new IllegalArgumentException("Value is not  composed.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.order.support.CouchViewResultRow#composedValue(java
	 * .lang.Class)
	 */
	@Override
	public final <T> T composedValue(final Class<? extends T> targetClass) {
		composedValueGuard();
		return mapCopyTemplate.createShallowCopyFieldsFromMap(targetClass, composedValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.order.support.CouchViewResultRow#composedKey(java.lang
	 * .Class)
	 */
	@Override
	public final <T> T composedKey(final Class<? extends T> targetClass) {
		composedKeyGuard();
		return mapCopyTemplate.createShallowCopyFieldsFromMap(targetClass, composedKey());
	}

	
	
	
}
