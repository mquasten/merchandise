package de.mq.merchandise.util.chouchdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonDeserialize;


class CouchViewResponse {
	
	
	
	private Long total_rows;
	private Long offset;
	
	
	@JsonDeserialize(contentAs=SimpleCouchViewRowImpl.class)
	private List<CouchViewResultRow>  rows;
	
	public final List<CouchViewResultRow> rows() {
		if( rows == null){
			rows=new ArrayList<>();
		}
		return rows; 
	}
	
	
	public final List<String> single() {
		final List<String> results = new ArrayList<>();
		for(final CouchViewResultRow row : rows()){
			results.add(row.singleValue());
		}
		return Collections.unmodifiableList(results);
		
	}
	
	public final List<Map<String,?>> composed() {
		final List<Map<String,?>> results = new ArrayList<>();
		for(final CouchViewResultRow row : rows()){
			results.add(Collections.unmodifiableMap(row.composedValue()));
		}
		return Collections.unmodifiableList(results);
	}
	
	
	public final<T> List<T> composed(Class<? extends T> targetClass) {
		final List<T> results = new ArrayList<>();
		for(final CouchViewResultRow row : rows) {
			results.add(row.composedValue(targetClass));
		}
		return results;
		
	}
	
}