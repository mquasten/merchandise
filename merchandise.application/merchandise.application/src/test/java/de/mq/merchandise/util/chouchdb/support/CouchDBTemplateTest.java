package de.mq.merchandise.util.chouchdb.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;


import de.mq.mapping.util.json.MapBasedResponseClassFactory;

public class CouchDBTemplateTest {
	
	private static final String DATABASE = "petStore";
	final MapBasedResponseClassFactory mapBasedResponseClassFactory = Mockito.mock(MapBasedResponseClassFactory.class); 
	final RestOperations restOperations = Mockito.mock(RestOperations.class);
	
	private final  CouchDBTemplate couchDBTemplate  = new CouchDBTemplate(mapBasedResponseClassFactory, restOperations, DATABASE);
	
	@Test
	public final void di() {
		Assert.assertEquals(restOperations, ReflectionTestUtils.getField(couchDBTemplate, "restOperations"));
		Assert.assertEquals(mapBasedResponseClassFactory, ReflectionTestUtils.getField(couchDBTemplate, "mapBasedResponseClassFactory"));
		Assert.assertEquals(DATABASE, ReflectionTestUtils.getField(couchDBTemplate, "database"));
		Assert.assertEquals(5984, ReflectionTestUtils.getField(couchDBTemplate, "port"));
	}
}
