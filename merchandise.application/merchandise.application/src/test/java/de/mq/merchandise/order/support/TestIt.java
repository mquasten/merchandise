package de.mq.merchandise.order.support;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.sun.xml.bind.v2.model.core.PropertyKind;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/geocodingRepository.xml"})
public class TestIt {

	@Autowired
	private RestOperations restOperations;

	
	
	private final String URL = "http://localhost:5984/petstore/_design/qualityByArtist/_view/QualityByArtist?key=\"{key}\""; 
	
	private final String URL2 = "http://localhost:5984/petstore/_design/pricePerUnit/_list/quantityFilter/PricePerUnit?key={key}&quantity={quantity}";
	
	
	
	
	@Test
	public final void test() throws JsonGenerationException, JsonMappingException, IOException {
	
		RestTemplate x = (RestTemplate) restOperations;
		MappingJacksonHttpMessageConverter c  = (MappingJacksonHttpMessageConverter) x.getMessageConverters().iterator().next() ;
		final  ObjectMapper mapper= c.getObjectMapper() ;
		
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		final Map<String,String> pars = new HashMap<>();
		pars.put("key", "nicole" );
		@SuppressWarnings("unchecked")
		Map<String,Object> response = restOperations.getForObject(URL,Map.class,pars);
		System.out.println(response);
		
		pars.clear();
		
		
		
		
		pars.put("key", mapper.writeValueAsString(new PetPriceKey("platinium", "date")));
		pars.put("quantity", "3");
		System.out.println(mapper.writeValueAsString(new PetPriceKey("platinium", "date")));
		
		
		@SuppressWarnings("unchecked")
		Response<PetPriceKey,Map<String,Object>> prices = restOperations.getForObject(URL2,Response.class, pars);
		
		for(Object row : prices.rows) {
			 System.out.println(((Row)row).value);
			 
			 PetPriceKey p = (PetPriceKey) ((Row)row).key;
			 System.out.println(p.unit);
			 System.out.println(p.quality);
			 System.out.println(((Row)row).value);
		}
	}
	
	
	
	
	
	
}
