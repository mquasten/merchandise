package de.mq.merchandise.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.BasicRepository;

public abstract  class AbstractPagingMemoryRepository<T>  implements BasicRepository<T, Long>{
	
	
	protected final Map<Long,T> storedValues = new HashMap<Long,T>();
	
	protected final Collection<T> forPattern(final Paging paging, final Parameter<?> ... params) {
	
		final List<T> allResults = new ArrayList<>();
		for(final T value : storedValues.values()){
			
			if( ! match( value, params )){
				continue;
			}
			allResults.add(value);
		}
		paging.assignRowCounter(allResults.size());
	    Collections.sort(allResults, comparator());
	    return Collections.unmodifiableList(allResults.subList(paging.firstRow(), Math.min(allResults.size(), paging.firstRow()+paging.pageSize())));
	   
	}

	
	protected abstract boolean match(final T value, final Parameter<?> ...  params );
	
	protected abstract Comparator<T> comparator();
	
	
	@SuppressWarnings("unchecked")
	protected final <Y> Y param(final Parameter<?>[]  params, final String name, Class<Y> clazz) {
		for(final Parameter<?> param : params){
			if(param.name().trim().equalsIgnoreCase(name)){
				return (Y) param.value();
			}
		}
		throw new IllegalArgumentException("Parameter with name " + name  + " not found");
		
	}


	@Override
	public  final T save(final T entity) {
		if ( ! ((BasicEntity) entity).hasId()){
			EntityUtil.setId(entity, randomId());
		}
		storedValues.put( ((BasicEntity) entity).id(), entity);
		return entity;
		
		
	}
	
	protected final long randomId() {
		return (long) (Math.random() * 1e12);
	}


	@Override
	public final void delete(final Long id) {
		if ( ! storedValues.containsKey(id)){
			return;
		}
		storedValues.remove(id);
	}


	@Override
	public final T forId(final Long id) {
		return storedValues.get(id);
	}
	
	
	public final Collection<T> entities() {
		return storedValues.values();
	}

	
	

}
