package de.mq.merchandise.util.chouchdb.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

class Mapping  {
	
	
	
	
	
	private final String key ;
	
	private final String field;
	
	final Class<?> clazz;
	
	private List<String> paths = new ArrayList<>();
	public Mapping(final String key,final Class<?> clazz, final String field, final String ... paths) {
		this.key=key;
		this.field=field;
		this.clazz=clazz;
		for(String path : paths){
			this.paths.add(path);
		}
	}
	
	Mapping(final Class<?> clazz, final String field, final String ... paths) {
	
		this(null,clazz, field, paths);
	}
	Mapping(final String key, final String ... paths) {
		this(key,null,null, paths);
	}
	
	
	
	
	
	
	
	
	boolean matchesForParent(final String key) {
		if (this.key==null) {
			return false;
		}
		return this.key.equals(key);
	}
	
	boolean matchesForRow(){
		return (key==null);
	}
	
	boolean hasField() {
		return (field!=null);
	}
	
	final Object valueFor(final Object value) {
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



	public void assignField(final Object target, final Object value) {
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
	
	
	
	
	
}
