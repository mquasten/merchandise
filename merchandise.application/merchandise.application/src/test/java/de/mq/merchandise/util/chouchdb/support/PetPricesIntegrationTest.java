package de.mq.merchandise.util.chouchdb.support;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import de.mq.merchandise.util.chouchdb.MapBasedResponse;
import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/geocodingRepository.xml"})

public class PetPricesIntegrationTest {

	@Autowired
	private RestOperations restOperations;

	
	
	private final String URL = "http://localhost:5984/petstore/_design/qualityByArtist/_view/qualityByArtist?key=\"{key}\""; 
	
	private final String URL2 = "http://localhost:5984/petstore/_design/pricePerUnit/_list/quantityFilter/pricePerUnit?key={key}&quantity={quantity}";
	
	
	
	
	@Test
	public final void test() throws JsonGenerationException, JsonMappingException, IOException {
		
		RestTemplate x = (RestTemplate) restOperations;
		MappingJacksonHttpMessageConverter c  = (MappingJacksonHttpMessageConverter) x.getMessageConverters().iterator().next() ;
		final  ObjectMapper mapper= c.getObjectMapper() ;
	
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		final Map<String,String> pars = new HashMap<>();
		pars.put("key", "nicole" );
	
		MapBasedResponse response = restOperations.getForObject(URL,SimpleCouchDBResultImpl.class,pars);
		
		System.out.println( response.rows().iterator().next().singleValue(String.class));
		
		
		pars.clear();
		
		System.out.println( mapper.writeValueAsString(new PetPriceKey("platinium", "date")));
		
		pars.put("key", mapper.writeValueAsString(new PetPriceKey("platinium", "date")));
		pars.put("quantity", "3");
	//	System.out.println(mapper.writeValueAsString(new PetPriceKey("platinium", "date")));
		
		System.out.println(URL2);
		System.out.println(pars);
	
		MapBasedResponse prices = restOperations.getForObject(URL2,SimpleCouchDBResultImpl.class, pars);
		
		for(MapBasedResultRow row : prices.rows()) {
			System.out.println(row.composedValue());
			System.out.println(row.composedKey());
			
			
			
			
		}
		
	}
	
	
	
	
	
	
}
