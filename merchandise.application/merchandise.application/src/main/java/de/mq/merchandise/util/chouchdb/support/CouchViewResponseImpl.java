package de.mq.merchandise.util.chouchdb.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import de.mq.merchandise.util.chouchdb.ChouchViewResponse;
import de.mq.merchandise.util.chouchdb.CouchViewResultRow;

class CouchViewResponseImpl implements ChouchViewResponse {

	@SuppressWarnings("unused")
	private Long total_rows;
	@SuppressWarnings("unused")
	private Long offset;

	@JsonDeserialize(contentAs = SimpleCouchViewRowImpl.class)
	private List<CouchViewResultRow> rows;

	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#rows()
	 */
	@Override
	public final List<CouchViewResultRow> rows() {
		if (rows == null) {
			rows = new ArrayList<>();
		}
		return rows;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#single()
	 */
	@Override
	public final List<String> single() {
		return new  CollectionTemplate<String>() {
			@Override
			protected String readRows(final CouchViewResultRow row, Class<? extends String> target) {
				return row.singleValue();
			}}.results(String.class);
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#composed()
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public final List<Map<String, Object>> composed() {
		
		   return (List<Map<String, Object>>)  (List<? extends Map<String, Object>>)  new CollectionTemplate<Map<?,?>> () {
			@Override
			protected Map<?,?> readRows(final CouchViewResultRow row, final Class<? extends Map<?,?>> target) {
				return row.composedValue();
			}}.results( (Class<? extends Map<?, ?>>) Map.class);
			
			
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.chouchdb.ChouchViewResponse#composed(java.lang.Class)
	 */
	@Override
	public final <T> List<T> composed(final Class<? extends T> targetClass) {
		return new CollectionTemplate<T>() {
			@Override
			protected T readRows(CouchViewResultRow row, Class<? extends T> target) {
				return row.composedValue(targetClass);
			}
		}.results(  targetClass);
	}
	

	abstract class CollectionTemplate<T> {
		final List<T> results(final Class<? extends T> targetClass) {
			final List<T> results = new ArrayList<T>();
			for (final CouchViewResultRow row : rows) {
				results.add(readRows(row, targetClass));
			}
			return Collections.unmodifiableList(results);
		}

		protected abstract T readRows(final CouchViewResultRow row, final Class<? extends T> target);
	}

}