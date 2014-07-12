package de.mq.merchandise.util.chouchdb;

import java.util.Map;

public interface MapBasedResultRow {

	String id();

	String singleKey();

	String singleValue();

	Map<String, Object> composedKey();

	Map<String, Object> composedValue();
	
	<T> T composedValue(final Class<? extends T> targetClass);
	
	<T> T composedKey(final Class<? extends T> targetClass) ;

}