package de.mq.merchandise.util.chouchdb.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestOperations;

import de.mq.merchandise.util.chouchdb.MapBasedResponse;

public class CouchDBTemplate {
	
	private final RestOperations restOperations;
	
	private final String database;
	
	public CouchDBTemplate(final RestOperations restOperations, final String database) {
		this.restOperations = restOperations;
		this.database=database;
	}

	public final void forKey(final String view, final String key)  {
		final String url = new SimpleChouchDBUrlBuilder().withView(view).withDatabase(database).build() + "?key=\"{key}\"";
		final Map<String,String> keyMap = new HashMap<>(); 
		keyMap.put("key", key);
		System.out.println(url);
		System.out.println(restOperations);
		final MapBasedResponse response = this.restOperations.getForObject(url, SimpleCouchDBResultImpl.class, keyMap);
		
		
		
		System.out.println(PetPriceKey.class.getClassLoader());
		
	}

}
