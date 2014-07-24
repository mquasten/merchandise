package de.mq.merchandise.util.chouchdb.support;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.RestOperations;

import de.mq.merchandise.util.chouchdb.MapBasedResponse;

public class CouchDBTemplate {
	
	private final RestOperations restOperations;
	
	private final String database;
	private final ObjectMapper mapper = new ObjectMapper();
	
	public CouchDBTemplate(final RestOperations restOperations, final String database) {
		this.restOperations = restOperations;
		this.database=database;
	}

	public final <T> List<T> forKey(final String view, final String key, final Class<? extends T> target) throws JsonGenerationException, JsonMappingException, IOException  {
		final String url = new SimpleChouchDBUrlBuilder().withView(view).withDatabase(database).build() + "?key={key}";
		final Map<String,String> keyMap = new HashMap<>(); 
		
		
		keyMap.put("key", mapper.writeValueAsString(key) );
		
		System.out.println(keyMap);
		final MapBasedResponse response = this.restOperations.getForObject(url, SimpleCouchDBResultImpl.class, keyMap);
		return Collections.unmodifiableList(response.result(target));
		
	}

}
