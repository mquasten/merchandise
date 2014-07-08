package de.mq.merchandise.util.chouchdb.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class Mapping  {
	
	
	
	final private boolean isParent; 
	
	private final String key ;
	
	private final boolean mapToSubRow;
	
	private List<String> paths = new ArrayList<>();
	Mapping(final String key) {
		this.key=key;
		isParent=true;
		mapToSubRow=true;
	}
	
	Mapping(final String key,final  boolean isParent) {
		this.key=key;
		this.isParent=isParent;
		paths.add(key);
		mapToSubRow=false;
	}
	
	boolean matchesForParent(final String key) {
		return this.key.equals(key)&&isParent;
	}
	
	boolean matchesForRow(){
		return !isParent;
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
		if(! mapToSubRow) {
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
	
	final boolean  mapToSubRow() {
		return mapToSubRow;
	}
	
	final String key() {
		return key;
	}
	
}
