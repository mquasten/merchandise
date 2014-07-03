package de.mq.merchandise.util.chouchdb.support;

import java.util.Collections;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.mq.merchandise.util.chouchdb.CouchViewResultRow;

class SimpleCouchViewRowImpl implements CouchViewResultRow {

	private Object key;

	private Object value;

	private String id;

	@JsonIgnore
	private MapCopyTemplate mapCopyTemplate = new MapCopyTemplate();

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
	public final String singleKey() {

		if (key instanceof Map) {
			throw new IllegalArgumentException("Key is a Composed Key");
		}
		return (String) key;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleValue()
	 */
	@Override
	public final String singleValue() {
		if (key instanceof Map) {
			throw new IllegalArgumentException("Key is a Composed Key");
		}

		return (String) value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedKey()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final Map<String, ? extends Object> composedKey() {
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
	public final Map<String, ? extends Object> composedValue() {
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
