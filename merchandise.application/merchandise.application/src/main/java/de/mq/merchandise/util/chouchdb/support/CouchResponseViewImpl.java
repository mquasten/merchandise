package de.mq.merchandise.util.chouchdb.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.mq.merchandise.util.chouchdb.ChouchViewResponse;
import de.mq.merchandise.util.chouchdb.CouchViewResultRow;

class CouchResponseViewImpl extends HashMap<String, Object> implements ChouchViewResponse {

	public CouchResponseViewImpl() {
		final Mapping parent = new Mapping("rows");
		mappings.add(parent);
		mappings.add(new Mapping("total_rows", CouchResponseViewImpl.class, "info" ));
		mappings.add(new Mapping("offset", CouchResponseViewImpl.class, "description" ));
		new Mapping(SimpleCouchViewRowImpl.class, parent, "value",  "value");
		new Mapping(SimpleCouchViewRowImpl.class,parent, "key", "key");
		new Mapping(SimpleCouchViewRowImpl.class, parent, "id", "id");
	}

	/**
	 * Serializeable
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private final MapCopyTemplate mapCopyTemplate = new MapCopyTemplate();

	@JsonIgnore
	private final boolean messageFieldAware = false;

	@JsonIgnore
	private List<CouchViewResultRow> results = new ArrayList<>();

	@JsonIgnore(true)
	private Object status;

	
	@JsonIgnore(true)
	private Object info;

	@JsonIgnore(true)
	private Object message;

	@JsonIgnore(false)
	private Object description;

	@JsonIgnore(false)
	private Collection<Mapping> mappings = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#rows()
	 */
	@Override
	public final List<CouchViewResultRow> rows() {
		if (results == null) {
			results = new ArrayList<>();
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#single()
	 */
	@Override
	public final List<String> single() {
		return new CollectionTemplate<String>() {
			@Override
			protected String readRows(final CouchViewResultRow row, Class<? extends String> target) {
				return row.singleValue();
			}
		}.results(String.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#composed()
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final List<Map<String, Object>> composed() {

		return (List<Map<String, Object>>) (List<? extends Map<String, Object>>) new CollectionTemplate<Map>() {
			@Override
			protected Map readRows(final CouchViewResultRow row, final Class<? extends Map> target) {
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
			protected T readRows(CouchViewResultRow row, Class<? extends T> target) {
				return row.composedValue(targetClass);
			}
		}.results(targetClass);
	}

	abstract class CollectionTemplate<T> {
		final List<T> results(final Class<? extends T> targetClass) {
			final List<T> results = new ArrayList<T>();
			for (final CouchViewResultRow row : CouchResponseViewImpl.this.results) {
				results.add(readRows(row, targetClass));
			}
			return Collections.unmodifiableList(results);
		}

		protected abstract T readRows(final CouchViewResultRow row, final Class<? extends T> target);
		
	}
	
	

	
	@Override
	public Object put(String key, Object value) {

		for (final Mapping mapping : mappings) {
			results.addAll(mapping.map(this, key, value));
		}

		return null;
	}

	
	
	


	
	

}