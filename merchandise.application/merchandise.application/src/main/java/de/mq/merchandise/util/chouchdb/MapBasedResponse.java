package de.mq.merchandise.util.chouchdb;

import java.util.List;
import java.util.Map;

public interface MapBasedResponse {

	List<MapBasedResultRow> rows();

	List<String> single();

	List<Map<String, Object>> composed();

	<T> List<T> composed(Class<? extends T> targetClass);

}