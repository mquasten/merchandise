package de.mq.merchandise.util.chouchdb.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.util.chouchdb.CouchViewResultRow;

class Mapping  {
	
	
	final Set<Mapping> childs = new HashSet<>();
	
	
	private final String key ;
	
	private final String field;
	
	final Class<?> clazz;
	
	private List<String> paths = new ArrayList<>();
	
	Mapping(final String key,final Class<?> clazz, final String field, final String ... paths) {
		this.key=key;
		this.field=field;
		this.clazz=clazz;
		for(String path : paths){
			this.paths.add(path);
		}
	}
	
	Mapping( final Class<?> clazz, final Mapping parent,  final String field, final String ... paths) {
		this(null,clazz, field, paths);
		parent.childs.add(this);
	}
	Mapping(final String key, final String ... paths) {
		this(key,null,null, paths);
	}
	
	private boolean matchesForParent(final String key) {
		if (this.key==null) {
			return false;
		}
		return this.key.equals(key);
	}
	
	private boolean matchesForRow(){
		return (key==null);
	}
	
	private boolean hasField() {
		return (field!=null);
	}
	
	private final Object valueFor(final Object value) {
		Object result = value;
		for(final String path: paths ){
			if (result instanceof Map) {
				result=(((Map<?,?>) result).get(path));
				continue;
			}
			if( result == null){
				break;
			}
			
			throw new IllegalArgumentException("Value should be a Map");
		}
		if( field != null ) {
			return result;
		}
		if (result instanceof Collection<?>) {
			return Collections.unmodifiableCollection((Collection<?>) result);
			
		}
		if (result instanceof Map<?,?>) {
			final Collection<Object> results = new ArrayList<>();
			results.add(result);
			return Collections.unmodifiableCollection(results);
			
		}
		throw new IllegalArgumentException("Value should be a Map");
		
	}



	private void assignField(final Object target, final Object value) {
		if( field == null ){
			throw new IllegalArgumentException("Field isn't defined");
		}
		if( clazz == null ){
			throw new IllegalArgumentException("Class isn't defined");
		}
		final Field targetField = ReflectionUtils.findField(clazz, field);
		if( field == null){
			throw new IllegalArgumentException("Field " + field + " not found for Class: " + clazz );
		}
		targetField.setAccessible(true);
		
		ReflectionUtils.setField(targetField, target, valueFor(value));
		
	}
	
	
	private void mapRow(final Map<String, Object> row, final Object result) {
		if (this.matchesForRow()) {
			this.assignField(result, row);	
		}
	}
	
	
	
	
	

	private void mapSubRow(final Map<String, Object> row, final CouchViewResultRow result) {
		for (final Mapping child : childs) {
			child.mapRow(row, result);
		}

		
	}


   private Collection<CouchViewResultRow> mapSubRows(final Collection<Map<String, Object>> rows ) {
		final Collection<CouchViewResultRow> results = new ArrayList<>();
		for (final Map<String, Object> row : rows) {
			final CouchViewResultRow result = new SimpleCouchViewRowImpl();
		    mapSubRow(row, result);
			results.add(result);
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	Collection<CouchViewResultRow> map(final Object parent, final String key, final Object value) {
	
		if (!matchesForParent(key)) {
		  return new ArrayList<>(); 
		} 
		
		if (! hasField()) {
			return mapSubRows((Collection<Map<String, Object>>) valueFor(value));
			
		}
        this.assignField(parent, value); 
        return new ArrayList<>();
	}
	
}
