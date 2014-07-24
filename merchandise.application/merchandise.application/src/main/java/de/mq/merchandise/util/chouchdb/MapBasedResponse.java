package de.mq.merchandise.util.chouchdb;

import java.util.List;
import java.util.Map;

public interface MapBasedResponse {

	List<MapBasedResultRow> rows();

	<T> List<T> single(Class<? extends T> clazz);

	List<Map<String, Object>> composed();

	<T> List<T> composed(Class<? extends T> targetClass);
	
	<T> List<T> result(final Class<? extends T> targetClass);



}