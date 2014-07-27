package de.mq.merchandise.util.chouchdb.support;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.web.client.RestOperations;

import de.mq.merchandise.util.chouchdb.MapBasedResponse;
import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/geocodingRepository.xml"})

public class PetPricesIntegrationTest {

	@Autowired
	private RestOperations restOperations;

	
	
	private final String URL = "http://localhost:5984/petstore/_design/qualityByArtist/_view/qualityByArtist?key=\"{key}\""; 
	
	private final String URL2 = "http://localhost:5984/petstore/_design/pricePerUnit/_list/quantityFilter/pricePerUnit?key={key}&quantity={quantity}";
	
	private final String URL3 =  "http://maps.googleapis.com/maps/api/geocode/json?address={address}&sensor=false";
	
	
	@Test
	public final void petstore() throws JsonGenerationException, JsonMappingException, IOException {
		
		
		final  ObjectMapper mapper=  new ObjectMapper() ;
	
		
		
		final Map<String,String> pars = new HashMap<>();
		pars.put("key", "nicole" );
	
		MapBasedResponse response = restOperations.getForObject(URL,SimpleCouchDBResultImpl.class,pars);
		
		Assert.assertEquals("platinium", response.rows().iterator().next().singleValue(String.class));
		
		
		pars.clear();
		
		
		
		pars.put("key", mapper.writeValueAsString(new PetPriceKey("platinium", "date")));
		pars.put("quantity", "3");
	//	System.out.println(mapper.writeValueAsString(new PetPriceKey("platinium", "date")));
		
		
		MapBasedResponse prices = restOperations.getForObject(URL2,SimpleCouchDBResultImpl.class, pars);
		
		for(MapBasedResultRow row : prices.rows()) {
			final Map<String, Object> values = row.composedValue();
			Assert.assertEquals(3, values.get("min"));
			Assert.assertEquals(4, values.get("max"));
			Assert.assertEquals(949.99, values.get("pricePerUnit"));
			
		    final Map<String,Object> keys = row.composedKey();
			Assert.assertEquals("platinium", keys.get("quality"));
			Assert.assertEquals("date", keys.get("unit"));
			
			
			
		}
		
		
	}
	
	
	@Test
	public final void maps() {
		
		final Map<String,String> pars = new HashMap<>();
		pars.put("address", "Wegberg, Am Telt 4");
		MapBasedResponse response = restOperations.getForObject(URL3, MapsCoordinatesResultImpl.class, pars);
		
		for(MapBasedResultRow row : response.rows()  ) {
		   final Map<String,Object> values = row.composedValue();
		 
		   Assert.assertEquals( 51.166913,  round((Double) values.get("lat")) );
		   Assert.assertEquals( 6.2829833,  round((Double) values.get("lng")) );
		   final Collection<String> types = row.collectionKey(String.class);
		   Assert.assertEquals(1, types.size());
		   Assert.assertEquals("street_address",types.iterator().next());
		}
		
		
		Assert.assertEquals("OK", response.field(MapBasedResponse.InfoField.Status, String.class));
	}


	private double round(final double value) {
		return Math.round( 1e12 *  value)/1e12;
	}
	
	
}
