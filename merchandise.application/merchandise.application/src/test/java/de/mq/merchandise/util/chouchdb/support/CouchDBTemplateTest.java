package de.mq.merchandise.util.chouchdb.support;

import java.util.Collection;

import junit.framework.Assert;

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
		Mockito.when(mapBasedResponseClassFactory.mappingBuilder()).thenReturn(mapBasedResultBuilder);
		Mockito.when(mapBasedResponseClassFactory.createClass(mappingCollectionCaptor.capture())).thenReturn(CLASS);
	}
	
	
	@Test
	public final void clazz(){
		
		Assert.assertEquals(CLASS, couchDBTemplate.clazz());
		final Collection<?> results = mappingCollectionCaptor.getValue();
		final Object  parent = results.iterator().next();
		Assert.assertEquals("rows", ReflectionTestUtils.getField(parent, "key"));
		Assert.assertNull(ReflectionTestUtils.getField(parent, "field"));
		Assert.assertTrue(((Collection<?>)ReflectionTestUtils.getField(parent, "paths")).isEmpty());
	
		final Collection<?> childs = (Collection<?>) ReflectionTestUtils.getField(parent, "childs");
		Assert.assertEquals(2, childs.size());
		for(final Object child : childs){
			Assert.assertNull(ReflectionTestUtils.getField(child, "key"));
			final Object field = ReflectionTestUtils.getField(child, "field");
			Assert.assertTrue(field.equals("key")||field.equals("value"));
			final Collection<?> paths = (Collection<?>) ReflectionTestUtils.getField(child, "paths");
			Assert.assertEquals(field, paths.iterator().next());
			
		}
	}
}
