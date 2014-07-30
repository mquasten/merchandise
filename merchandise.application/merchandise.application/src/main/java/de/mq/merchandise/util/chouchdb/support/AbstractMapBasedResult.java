package de.mq.merchandise.util.chouchdb.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.util.chouchdb.MapBasedResponse;
import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

abstract class AbstractMapBasedResult extends HashMap<String, Object> implements MapBasedResponse {

	/**
	 * Serializeable
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private final MapCopyTemplate mapCopyTemplate = new MapCopyTemplate();

	@JsonIgnore
	private final boolean messageFieldAware = false;

	@JsonIgnore
	private List<MapBasedResultRow> results = new ArrayList<>();

	@JsonIgnore(true)
	private Object status;

	@JsonIgnore(true)
	private Object info;

	@JsonIgnore(true)
	private Object message;

	@JsonIgnore(false)
	private Object description;

	@JsonIgnore(false)
	private Collection<Mapping<MapBasedResultRow>> mappings = new ArrayList<>();

	private Class<? extends MapBasedResultRow> rowClass;

	@JsonIgnore
	final ConfigurableConversionService conversionService = new DefaultConversionService();

	public AbstractMapBasedResult() {
		configure();
	}

	protected   abstract void configure() ;

	protected Mapping<MapBasedResultRow> assignParentResultMapping(final String node, String... paths) {
		final Mapping<MapBasedResultRow> result = new Mapping<MapBasedResultRow>(node, null, paths);
		mappings.add(result);
		return result;
	}

	protected void assignParentFieldMapping(final String node, final String field, String... paths) {
		mappings.add(new Mapping<MapBasedResultRow>(node, field, paths));
	}

	protected void assignChildRowMapping(Mapping<MapBasedResultRow> parent, final String field, String... paths) {
		new Mapping<MapBasedResultRow>(parent, field, paths);
	}

	protected void assignRowClass(final Class<? extends MapBasedResultRow> rowClass) {
		this.rowClass = rowClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#rows()
	 */
	@Override
	public final List<MapBasedResultRow> rows() {
		return Collections.unmodifiableList(results);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#single()
	 */
	@Override
	public final <T> List<T> single(final Class<? extends T> clazz) {
		return new CollectionTemplate<T>() {
			@Override
			protected T readRows(final MapBasedResultRow row, Class<? extends T> target) {
				return row.singleValue(clazz);
			}
		}.results(clazz);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#composed()
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final List<Map<String, Object>> composed() {

		return (List<Map<String, Object>>) (List<? extends Map>) new CollectionTemplate<Map>() {
			@Override
			protected Map readRows(final MapBasedResultRow row, final Class<? extends Map> target) {
				return row.composedValue();
			}
		}.results(Map.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.util.chouchdb.ChouchViewResponse#composed(java.lang
	 * .Class)
	 */
	@Override
	public final <T> List<T> composed(final Class<? extends T> targetClass) {
		return new CollectionTemplate<T>() {
			@Override
			protected T readRows(MapBasedResultRow row, Class<? extends T> target) {
				return row.composedValue(targetClass);
			}
		}.results(targetClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public final <T> List<T> result(final Class<? extends T> targetClass) {
		if (instanceOfMap(targetClass)) {
			return (List<T>) composed();
		}
		if (getClass().getClassLoader().equals(targetClass.getClassLoader())) {
			return composed(targetClass);
		}
		return single(targetClass);

	}

	private <T> boolean instanceOfMap(final Class<? extends T> targetClass) {
		try {
			targetClass.asSubclass(Map.class);
			return true;
		} catch (final ClassCastException ce) {
			return false;
		}
	}

	@Override
	public final Object put(final String key, final Object value) {
        
		Assert.notNull(rowClass);

		for (final Mapping<MapBasedResultRow> mapping : mappings) {
			results.addAll(mapping.map(this, rowClass, key, value));
		}

		return value;
	}

	abstract class CollectionTemplate<T> {
		final List<T> results(final Class<? extends T> targetClass) {
			final List<T> results = new ArrayList<T>();
			for (final MapBasedResultRow row : AbstractMapBasedResult.this.results) {
				results.add(readRows(row, targetClass));
			}
			return Collections.unmodifiableList(results);
		}

		protected abstract T readRows(final MapBasedResultRow row, final Class<? extends T> target);

	}

	@Override
	@SuppressWarnings("unchecked")
	public final <T> T field(final InfoField infoField, Class<? extends T> targetClass) {

		final Field field = ReflectionUtils.findField(getClass(), infoField.field());
		field.setAccessible(true);
		final Object result = valueFromField(field);
		if (getClass().getClassLoader().equals(targetClass.getClassLoader())) {

			return mapCopyTemplate.createShallowCopyFieldsFromMap(targetClass, (Map<String, Object>) result);
		}

		return conversionService.convert(result, targetClass);
	}

	private Object valueFromField(Field field) {
		try {
			return field.get(this);
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

}