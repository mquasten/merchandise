package de.mq.merchandise.util.chouchdb.support;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
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
	
	@Test
	public final void cglib() throws InstantiationException, IllegalAccessException {
		
		
		final Enhancer enhancer = new Enhancer();
	//	enhancer.setUseFactory(true);
		enhancer.setSuperclass(AbstractMapBasedResult.class);
		enhancer.setCallbackFilter(new CallbackFilter() {

			@Override
			public int accept(Method method) {
				
				if( method.getName().equals("configure")) {
					System.out.println("**" );
					return 1;
				}
				return 0;
			}} ); 

		
		enhancer.setCallbackTypes( new Class[] { NoOp.INSTANCE.getClass(), MethodInterceptor.class});
		Class<?> clazz = enhancer.createClass();
		Enhancer.registerCallbacks(clazz, (new Callback[]  { NoOp.INSTANCE , new MethodInterceptor() {

			
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				
				
				 ReflectionTestUtils.setField(obj, "rowClass", SimpleMapBasedResultRowImpl.class);
			
					Mapping<MapBasedResultRow> parent = new Mapping<>("results", null);
							
					new Mapping<>(parent, "value", "geometry",  "location");
					new Mapping<>(parent, "key", "types");
					Mapping<MapBasedResultRow> field = new Mapping<>("status", "status");
					
					@SuppressWarnings("unchecked")
					final Collection<Mapping<MapBasedResultRow>> mappings = (Collection<Mapping<MapBasedResultRow>>) ReflectionTestUtils.getField(obj, "mappings");
				    mappings.add(parent) ;
				    mappings.add(field);
				
				return null;
			}}})); 
		
		
	// AbstractMapBasedResult x =  (AbstractMapBasedResult) clazz.newInstance(); 
		
	
	 final Map<String,String> pars = new HashMap<>();
		pars.put("address", "Wegberg, Am Telt 4");
		MapBasedResponse response = (MapBasedResponse) restOperations.getForObject(URL3, clazz , pars);
		for(MapBasedResultRow row : response.rows()  ) {
			   final Map<String,Object> values = row.composedValue();
			   System.out.println(values);
			   final  Collection<String> keys = row.collectionKey(String.class);
			   System.out.println(keys);
		}
	  
	}
	
	
	
}


