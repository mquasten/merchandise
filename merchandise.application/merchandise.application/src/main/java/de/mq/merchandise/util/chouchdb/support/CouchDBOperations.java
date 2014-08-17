package de.mq.merchandise.util.chouchdb.support;

import java.util.List;
import java.util.Map;

public interface CouchDBOperations {

	<T> List<T> forKey(String view, String key, Class<? extends T> target);

	<T> List<T> forKey(String view, String list, Map<String, String> keys, Map<String, String> queryParams, Class<? extends T> target);

}