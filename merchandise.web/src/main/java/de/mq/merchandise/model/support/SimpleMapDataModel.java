package de.mq.merchandise.model.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.faces.model.DataModel;

import org.primefaces.model.SelectableDataModel;
import org.springframework.util.ReflectionUtils;



public class SimpleMapDataModel<T> extends DataModel<T> implements SelectableDataModel<T> , List<T> {

	private final Map<UUID,T> map = new HashMap<>();; 
	private final List<T> rows = new ArrayList<>();
	
	int index = -1;
	
	
	public SimpleMapDataModel() {
		
	}
	
	public SimpleMapDataModel(final Collection<? extends T> rows) {
		setWrappedData(rows);
	}
	
	@Override
	public final UUID getRowKey(T object) {
	 	try {
			return id(object);
			
		} catch (final Exception  ex) {
			 throw new IllegalArgumentException("Unable to get id for Row.",  ex);
		} 
		
	}

	private UUID id(T object) throws IllegalAccessException, InvocationTargetException {
		final Object id = invokeGetId(object);
		if ( id != null){
		   return UUID.nameUUIDFromBytes(id.toString().getBytes());
		}
		return UUID.nameUUIDFromBytes(Long.valueOf(System.identityHashCode(object)).toString().getBytes());
	}

	private Object invokeGetId(T object) throws IllegalAccessException, InvocationTargetException {
		final Method method =  ReflectionUtils.findMethod(object.getClass(), "getId" );
		if ( method == null){
			return null;
		}
		return method.invoke(object);
	}

	@Override
	public final T getRowData(String rowKey) {
		return map.get(UUID.fromString(rowKey));
	}

	@Override
	public final int size() {
		return rows.size();
	}

	@Override
	public final boolean isEmpty() {
		return rows.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(final Object o) {
		try {
	      return map.containsKey(getRowKey((T)o));
		} catch (final Exception ex){
			return false;
		}
		
	}

	@Override
	public Iterator<T> iterator() {
		return rows.iterator();
	}

	@Override
	public Object[] toArray() {
		return rows.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return rows.toArray(a);
	}

	@Override
	public boolean add(T row) {
		final UUID id =  getRowKey(row);
		idExistsGuard(id, map);
		
		if( this.rows.add(row)) {
			map.put(id, row);
            return true;
		}
		
		return false;
	}

	private void idExistsGuard(UUID id, Map<UUID,T> map) {
		if( map.containsKey(id)){
			throw new IllegalArgumentException("Key " + id + " already exists");
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		
		if ( ! contains(o)) {
			return false;
		}
		final UUID id = getRowKey((T) o);
		final boolean result = rows.remove(map.get(id));
		map.remove(id);
		return result;
		
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(final Object row : c) {
			if( ! contains(row)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> rows) {
		map.putAll(itemMap(rows));
		return this.rows.addAll(rows);
	}

	private Map<UUID, T> itemMap(Collection<? extends T> rows) {
		final Map<UUID,T> newItems = new HashMap<>();
		for(final T row : rows){
			final UUID id = getRowKey(row);
			idExistsGuard(id, newItems);
			idExistsGuard(id, map);
			newItems.put(id, row);
		}
		return newItems;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> rows) {
		map.putAll(itemMap(rows));
		return this.rows.addAll(index, rows);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> rows) {
		boolean touched =false;
		for(final Object row :rows ){
			if(! contains(row)){
				continue;
			}
			touched=true;
			final UUID id  = getRowKey((T) row);
			map.remove(id);
			this.rows.remove(row);
			
		}
		return touched;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> rows) {
		final Set<UUID> ids = new HashSet<>();
		for(final Object row : rows){
			if( ! contains(row)) {
				continue;
			}
			ids.add(getRowKey((T) row)); 
		}
		boolean touched =false;
		for(final Entry<UUID, T> entry :map.entrySet().toArray(new Entry[map.size()]) ){
			if(ids.contains(entry.getKey())){
				continue;
			}
			touched=true;
			map.remove(entry.getKey());
			this.rows.remove(entry.getValue());
		}
		return touched;
	}

	@Override
	public void clear() {
		map.clear();
		this.rows.clear();
	}

	@Override
	public T get(int index) {
		return rows.get(index);
	}

	@Override
	public T set(int index, T element) {
		
		if ( ! contains(element) ) {
			throw new IllegalArgumentException("Element not found in list");
		}
		map.put(getRowKey(element), element);
		return this.rows.set(index, element);
	}

	@Override
	public void add(int index, T row) {
		final UUID id = getRowKey(row);
		this.idExistsGuard(id, map);
		this.map.put(id, row);
		this.rows.add(index, row);
		
	}

	@Override
	public T remove(int index) {
	    final T row = this.rows.get(index);
	    if( ! contains(row) ) {
	    	throw new IllegalArgumentException("DataModel not consistent, id of the row is not in Map");
	    }
	    map.remove(getRowKey(row));
	    return rows.remove(index);
	   
	}

	@Override
	public int indexOf(Object o) {
		return rows.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return rows.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return rows.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return rows.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return rows.subList(fromIndex, toIndex);
	}

	@Override
	public boolean isRowAvailable() {
		if( index < 0 ){
			return false;
		}
		
		if( index >=  rows.size()){
			return false;
		}
		return true;
	}

	@Override
	public int getRowCount() {
		return size();
	}

	@Override
	public T getRowData() {
		return rows.get(index);
	}

	@Override
	public int getRowIndex() {
		return index;
	}

	@Override
	public void setRowIndex(int rowIndex) {
		this.index=rowIndex;
		
	}

	@Override
	public Object getWrappedData() {
		return Collections.unmodifiableList(rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setWrappedData(Object data) {
		// shit interface design primefaces !!!!
		if (!(data instanceof Collection)) {
			throw new UnsupportedOperationException("Given Data should be a collection");
		}
		setWrappedData((Collection<T>)data);
	}

	public void setWrappedData(Collection<T> data) {
		clear();
		this.addAll((Collection<? extends T>) data);
	}
	
	
	
	

	

}
