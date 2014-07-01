package de.mq.merchandise.util.chouchdb;

import java.util.Map;

interface CouchViewResultRow {

	String id();

	String singleKey();

	String singleValue();

	Map<String, ? extends Object> composedKey();

	Map<String, ? extends Object> composedValue();
	
	<T> T composedValue(final Class<? extends T> targetClass);
	
	<T> T composedKey(final Class<? extends T> targetClass) ;

}