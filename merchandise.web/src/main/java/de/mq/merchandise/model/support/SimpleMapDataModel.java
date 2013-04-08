package de.mq.merchandise.model.support;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import javax.faces.model.DataModel;

import org.primefaces.model.SelectableDataModel;
import org.springframework.util.ReflectionUtils;



public class SimpleMapDataModel<T> extends DataModel<T> implements SelectableDataModel<T> , List<T> {

	private Map<String,T> map = new HashMap<>();; 
	private List<T> rows = new ArrayList<>();
	
	int index = -1;
	
	
	public SimpleMapDataModel() {
		
	}
	
	public SimpleMapDataModel(final Collection<T> rows) {
		setWrappedData(rows);
	}
	
	@Override
	public final String getRowKey(T object) {
		try {
			return id(object);
			
		} catch (final Exception  ex) {
			 throw new IllegalArgumentException("Unable to get id for Row.",  ex);
		}
		
	}

	private String id(T object) throws IllegalAccessException, InvocationTargetException {
		final Object id = ReflectionUtils.findMethod(object.getClass(), "getId" ).invoke(object);
		if ( id != null){
		   return UUID.nameUUIDFromBytes(id.toString().getBytes()).toString();
		}
		return UUID.nameUUIDFromBytes(object.toString().getBytes()).toString();
	}

	@Override
	public final T getRowData(String rowKey) {
		return map.get(rowKey);
	}

	@Override
	public final int size() {
		return map.size();
	}

	@Override
	public final boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return map.values().contains(o);
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
		String id =  getRowKey(row);
		keyExistsGuard(row, id);
		
		rowExistsGuard(row);
		rows.add(row);
		map.put(id, row);
		return true;
	}

	private void rowExistsGuard(T row) {
		if( rows.contains(row)){
			throw new IllegalArgumentException("Dupplicate row: " + row );
		}
	}

	private void keyExistsGuard(T row, String id) {
		if( map.containsKey(id)) {
			throw new IllegalArgumentException("Dupplicate row id: " + row );
		}
	}

	@Override
	// :fixMe SchadCode
	public boolean remove(Object o) {
		map.remove( getRowKey((T) o));
		
		return rows.remove(o);
		
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return rows.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> rows) {
		
		for(final T row : rows){
			this.add(row);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> rows) {
		final Map<String,T> newMap = new HashMap<>();
		for(final T row : rows){
			final String id = (String) getRowKey(row);
			keyExistsGuard(row, id);
			rowExistsGuard(row);
			newMap.put(id, row);
		}
		this.rows.addAll(index, rows);
		map.putAll(newMap);
		return true;
		
	}

	@Override
	// :fixMe SchadCode
	public boolean removeAll(Collection<?> rows) {
		
		for(Object row : rows){
			final String key = getRowKey((T) row).toString();
			map.remove(key);
		}
		
		return this.rows.removeAll(rows);
	}

	@Override
	public boolean retainAll(Collection<?> rows) {
		return this.rows.retainAll(rows);
	}

	@Override
	public void clear() {
		map.clear();
		rows.clear();
	}

	@Override
	public T get(int index) {
		return rows.get(index);
	}

	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, T row) {
		rowExistsGuard(row);
		final String id = getRowKey(row);
		keyExistsGuard(row, id);
		map.put(id, row);
		this.rows.add(index, row);
		
	}

	@Override
	public T remove(int index) {
	    T toBeRemoved = rows.remove(index);
	    if( toBeRemoved == null){
	    	return toBeRemoved; 
	    }
	    map.remove(getRowKey(toBeRemoved));
		return toBeRemoved;
	}

	@Override
	public int indexOf(Object o) {
		return rows.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return lastIndexOf(o);
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
		return map.size();
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
		
		return rows;
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
