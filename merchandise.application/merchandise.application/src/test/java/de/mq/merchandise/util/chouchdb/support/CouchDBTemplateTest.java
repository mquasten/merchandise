package de.mq.merchandise.util.chouchdb.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;

import de.mq.mapping.util.json.MapBasedResponseClassFactory;
import de.mq.mapping.util.json.MapBasedResultBuilder;
import de.mq.mapping.util.json.support.MapBasedResponse;
import de.mq.mapping.util.json.support.MappingTestConstants;

public class CouchDBTemplateTest {
	
	private static final String UNIT_PARAM = "unit";
	private static final String QUALITY_PARAM = "quality";
	private static final String QUANTITY = "3";
	private static final String VIEW2 = "pricePerUnit";
	private static final String LIST = "quantityFilter";
	private static final String QUANTITY_PARAM = "quantity";
	private static final String UNIT = "privateDate";
	private static final String QUALITITY = "platinium";
	private static final String KEY_PARAM = "key";
	private static final String VIEW = "qualityByArtist";
	private static final String KEY = "nicole";
	static final Class<MapBasedResponse> CLASS = MapBasedResponse.class;
	private static final String DATABASE = "petStore";
	final MapBasedResultBuilder mapBasedResultBuilder = MappingTestConstants.newMappingBuilder();
	final MapBasedResponseClassFactory mapBasedResponseClassFactory = Mockito.mock(MapBasedResponseClassFactory.class); 
	final RestOperations restOperations = Mockito.mock(RestOperations.class);
	
	private final  CouchDBTemplate couchDBTemplate  = new CouchDBTemplate(mapBasedResponseClassFactory, restOperations, DATABASE);
	
	@SuppressWarnings({ "rawtypes" })
	final ArgumentCaptor<Collection>mappingCollectionCaptor = ArgumentCaptor.forClass(Collection.class);
	
	@Test
	public final void di() {
		Assert.assertEquals(restOperations, ReflectionTestUtils.getField(couchDBTemplate, "restOperations"));
		Assert.assertEquals(mapBasedResponseClassFactory, ReflectionTestUtils.getField(couchDBTemplate, "mapBasedResponseClassFactory"));
		Assert.assertEquals(DATABASE, ReflectionTestUtils.getField(couchDBTemplate, "database"));
		Assert.assertEquals(5984, ReflectionTestUtils.getField(couchDBTemplate, "port"));
	}
	
	@Before
	public void setup() {
		Mockito.when(mapBasedResponseClassFactory.mappingBuilder()).thenReturn(mapBasedResultBuilder, MappingTestConstants.newMappingBuilder());
		Mockito.when(mapBasedResponseClassFactory.createClass(mappingCollectionCaptor.capture())).thenReturn(CLASS);
	}
	
	
	@Test
	public final void clazz(){
		
		Assert.assertEquals(CLASS, couchDBTemplate.clazz());
		final Collection<?> results = mappingCollectionCaptor.getValue();
		final Object  parent = results.iterator().next();
		Assert.assertEquals("rows", ReflectionTestUtils.getField(parent, KEY_PARAM));
		Assert.assertNull(ReflectionTestUtils.getField(parent, "field"));
		Assert.assertTrue(((Collection<?>)ReflectionTestUtils.getField(parent, "paths")).isEmpty());
	
		final Collection<?> childs = (Collection<?>) ReflectionTestUtils.getField(parent, "childs");
		Assert.assertEquals(2, childs.size());
		for(final Object child : childs){
			Assert.assertNull(ReflectionTestUtils.getField(child, KEY_PARAM));
			final Object field = ReflectionTestUtils.getField(child, "field");
			Assert.assertTrue(field.equals(KEY_PARAM)||field.equals("value"));
			final Collection<?> paths = (Collection<?>) ReflectionTestUtils.getField(child, "paths");
			Assert.assertEquals(field, paths.iterator().next());
			
		}
		
	}
	
	@Test
	public final void forKey() {
		final String url = new SimpleChouchDBUrlBuilder().withDatabase(DATABASE).withView(VIEW).withParams(KEY_PARAM).build();
		final Map<String,String> map = new HashMap<>();
		map.put(KEY_PARAM, String.format( "\"%s\"" , KEY));
		final MapBasedResponse mapBasedResponse = Mockito.mock(MapBasedResponse.class);
		Mockito.when(restOperations.getForObject(url, CLASS, map)).thenReturn(mapBasedResponse);
		final List<String> results = new ArrayList<>();
		results.add(QUALITITY);
		Mockito.when(mapBasedResponse.result(String.class)).thenReturn(results);
		Assert.assertEquals(results, couchDBTemplate.forKey(VIEW, KEY, String.class));
	
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void forKeys() {
		final String url = new SimpleChouchDBUrlBuilder().withDatabase(DATABASE).withView(VIEW2).withListFunction(LIST).withParams(QUANTITY_PARAM ,KEY_PARAM).build();
		
		final Map<String,String> queryParams = new  HashMap<>();
		queryParams.put(QUANTITY_PARAM, QUANTITY);
		
		
		final Map<String,String> keys = new LinkedHashMap<>();
		keys.put(QUALITY_PARAM, QUALITITY);
		keys.put(UNIT_PARAM, UNIT);
		
		
		final ArgumentCaptor<Class> classCaptor =  ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<String> urlCaptor =  ArgumentCaptor.forClass(String.class);
		
		final ArgumentCaptor<Map> mapCaptor =  ArgumentCaptor.forClass(Map.class);
		final MapBasedResponse mapBasedResponse = Mockito.mock(MapBasedResponse.class);
		Mockito.when(restOperations.getForObject(urlCaptor.capture(), classCaptor.capture(), mapCaptor.capture())).thenReturn(mapBasedResponse);
		final Map<String,String> result = new HashMap<>();
		final List<Map<String,String>> results = new ArrayList<>();
		result.put(UNIT_PARAM, UNIT);
		result.put("min", "3");
		result.put("max", "5");
		results.add(result);
		Mockito.when(mapBasedResponse.result(Map.class)).thenReturn( (List) results);
		
		Assert.assertEquals(results, couchDBTemplate.forKey(VIEW2, LIST, keys, queryParams, Map.class));
		
		Assert.assertEquals(url, urlCaptor.getValue());
		Assert.assertEquals(CLASS, classCaptor.getValue());
	
		final Map<String,String> params = mapCaptor.getValue();
		Assert.assertEquals(2,params.size());
		
		Assert.assertEquals(QUANTITY, params.get(QUANTITY_PARAM));
		Assert.assertEquals(String.format("{\"%s\":\"%s\",\"%s\":\"%s\"}", QUALITY_PARAM, QUALITITY, UNIT_PARAM , UNIT), params.get(KEY_PARAM));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void forKeysMapperException() throws IOException {
		ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
		ReflectionTestUtils.setField(couchDBTemplate, "mapper", mapper);
		Mockito.when(mapper.writeValueAsString(Mockito.any())).thenThrow(new IOException("Don't worry only for Test"));
		couchDBTemplate.forKey(VIEW2, LIST, new HashMap<String,String>(), new HashMap<String,String>(), Map.class);
	}
}
