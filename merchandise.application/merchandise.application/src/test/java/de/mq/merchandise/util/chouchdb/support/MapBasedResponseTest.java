package de.mq.merchandise.util.chouchdb.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.util.chouchdb.MapBasedResponse;
import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

public class MapBasedResponseTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public final void testMapping() {
		
		final MapBasedResponse mapBasedResponse = new SimpleCouchDBResultImpl();
		
		final Collection<Mapping<?>> mappings =  (Collection<Mapping<?>>) ReflectionTestUtils.getField(mapBasedResponse, "mappings");
	    Assert.assertEquals(1, mappings.size());
	    Assert.assertEquals("rows", ReflectionTestUtils.getField(mappings.iterator().next(), "key"));
	    Assert.assertEquals(2,  ((Collection<Mapping<?>>) ReflectionTestUtils.getField(mappings.iterator().next(), "childs")).size());
	    for(final Mapping<?> child : (Collection<Mapping<?>>) ReflectionTestUtils.getField(mappings.iterator().next(), "childs")) {
	    	
	    	Assert.assertEquals(1, ((Collection<?>)ReflectionTestUtils.getField(child, "paths")).size());
	    	final Object field = ReflectionTestUtils.getField(child, "field");
			
	        Assert.assertEquals(field, ((Collection<?>)ReflectionTestUtils.getField(child, "paths")).iterator().next());
	        Assert.assertTrue(field.equals("value")||(field.equals("key")));
	    	
	    	Assert.assertNull(ReflectionTestUtils.getField(child, "key"));
	    	Assert.assertTrue(((Collection<?>)ReflectionTestUtils.getField(child, "childs")).isEmpty());
	    	
	    }
		
		
	}
	@Test
	@SuppressWarnings("unchecked")
	public final void mapOtherParentField(){
		final MapBasedResponse mapBasedResponse = new AbstractMapBasedResult(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void configure() {
				super.assignParentFieldMapping("paging", "info", "start");
				
			}};
			
		
			final Collection<Mapping<?>> mappings =  (Collection<Mapping<?>>) ReflectionTestUtils.getField(mapBasedResponse, "mappings");
			Assert.assertEquals(1, mappings.size());
			Assert.assertEquals(0,  ((Collection<Mapping<?>>) ReflectionTestUtils.getField(mappings.iterator().next(), "childs")).size());
			
			Assert.assertEquals(1, ((Collection<?>)ReflectionTestUtils.getField(mappings.iterator().next(), "paths")).size());
	    	Assert.assertEquals("start", ((Collection<?>)ReflectionTestUtils.getField(mappings.iterator().next(), "paths")).iterator().next());
	    	
	    	Assert.assertEquals("info", (ReflectionTestUtils.getField(mappings.iterator().next(), "field")));
	    	Assert.assertEquals("paging", (ReflectionTestUtils.getField(mappings.iterator().next(), "key")));
		
	}
	@Test
	public final void rows() {
		final MapBasedResponse mapBasedResponse = new SimpleCouchDBResultImpl();
		final Collection<MapBasedResultRow> rows = new ArrayList<>();
		final MapBasedResultRow row = Mockito.mock(MapBasedResultRow.class);
		rows.add(row);
		ReflectionTestUtils.setField(mapBasedResponse, "results", rows);
		Assert.assertEquals(1,  mapBasedResponse.rows().size());
		Assert.assertEquals(rows, mapBasedResponse.rows());
	}

}
