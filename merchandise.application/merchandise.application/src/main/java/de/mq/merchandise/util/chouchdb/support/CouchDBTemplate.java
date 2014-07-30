package de.mq.merchandise.util.chouchdb.support;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.RestOperations;

public class CouchDBTemplate {
	
	private final RestOperations restOperations;
	
	private final String database;
	private final String host;
	private final int port;
	         
	private final ObjectMapper mapper = new ObjectMapper();
	
	public CouchDBTemplate(final RestOperations restOperations, final String database, final String host, final int port) {
		this.restOperations = restOperations;
		this.database=database;
		this.host=host;
		this.port=port;
	}
	
	public CouchDBTemplate(final RestOperations restOperations, final String database) {
		this(restOperations, database, "localhost",5984 );
	}

	public final <T> List<T> forKey(final String view, final String key, final Class<? extends T> target)   {
		
		final Map<String, String> keyMap = mapFromKey(String.format("\"%s\"", key) );
		
		final String url = newUrlBuilder().withView(view).withParams(keyMap.keySet()).build() ;
		
		return Collections.unmodifiableList(this.restOperations.getForObject(url, SimpleCouchDBResultImpl.class, keyMap).result(target));
		
	}

	private SimpleChouchDBUrlBuilder newUrlBuilder() {
		return new SimpleChouchDBUrlBuilder().withHost(host).withDatabase(database).withPort(port);
	}

	private Map<String, String> mapFromKey(final String key) {
		final Map<String,String> keyMap = new HashMap<>(); 
	
		keyMap.put("key", key);
		return keyMap;
	}
	
	public final <T> List<T> forKey(final String view, final String list, final Map<String, String> keys,  final Map<String,String> queryParams, final Class<? extends T> target)   {
		
		final Map<String, String> keyMap = mapFromKey( key2Json(keys) );
		keyMap.putAll(queryParams);
		final String url = newUrlBuilder().withView(view).withListFunction(list).withParams(keyMap.keySet()).build() ;
		
		
		return Collections.unmodifiableList(this.restOperations.getForObject(url, SimpleCouchDBResultImpl.class, keyMap).result(target));
		
		
	}

	private String key2Json(final Object value)  {
		try {
			return mapper.writeValueAsString(value);
		} catch (final IOException ex) {
			 throw new IllegalArgumentException("Unable to gerate Json from key" , ex );
		} 
	
	}

	
}