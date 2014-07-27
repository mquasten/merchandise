package de.mq.merchandise.util.chouchdb.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/geocodingRepository.xml"})
public class CouchTemplateIntegrationTest {
	@Autowired
	private RestOperations restOperations;
	
	
	@Test
	public final void singleKey()  {
		final CouchDBTemplate couchDBTemplate = new CouchDBTemplate(restOperations, "petstore");
		final List<String> results = couchDBTemplate.forKey("qualityByArtist", "nicole", String.class);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals("platinium", results.get(0));
	}
	
	
	
	@Test
	public final void composedKey() {
		final CouchDBTemplate couchDBTemplate = new CouchDBTemplate(restOperations, "petstore");
		Map<String,String> keys = new LinkedHashMap<>();
		keys.put("quality", "platinium");
		keys.put("unit", "date");
		
		final Map<String,String> params = new HashMap<>();
		params.put("quantity", "3");
		@SuppressWarnings("unchecked")
		final List<Map<String,String>>  results = couchDBTemplate.forKey("pricePerUnit", "quantityFilter", keys, params, (Class<? extends Map<String,String>>) Map.class);
	    Assert.assertEquals(1, results.size()) ; 
	    Assert.assertEquals(949.99, results.get(0).get("pricePerUnit" ));
	    Assert.assertEquals(3, results.get(0).get("min" ));
	    Assert.assertEquals(4, results.get(0).get("max" ));
	}
	
	
	
}
