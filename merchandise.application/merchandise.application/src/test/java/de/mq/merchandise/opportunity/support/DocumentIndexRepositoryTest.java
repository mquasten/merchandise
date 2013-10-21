package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestOperations;

public class DocumentIndexRepositoryTest {
	
	private static final String REVISION = "revision-4711";

	private final RestOperations restOperations = Mockito.mock(RestOperations.class);
	
	private final  DocumentIndexRepository  documentIndexRepository = new DocumentIndexRestRepositoryImpl(restOperations);
	
	private final EntityContext entityContextCreateOrUpdate = new EntityContextImpl(19680528L, Resource.Opportunity, false);
	
	
	private final  Collection<EntityContext> entityContexts = new ArrayList<>();
	
	
	
	@Test
	public void revisionsforIds() {
		final Collection<String> keys = new HashSet<>();
		keys.add(String.valueOf(entityContextCreateOrUpdate.reourceId()));
	    final Map<String,Object> idMap = new HashMap<>();
		idMap.put(DocumentIndexRestRepositoryImpl.KEY_ATTRIBUTE_NAME, keys);
		entityContexts.add(entityContextCreateOrUpdate);
		Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL,idMap, Map.class,Resource.Opportunity.urlPart())).thenReturn(resultMap(String.valueOf(entityContextCreateOrUpdate.reourceId()), REVISION));
		
		final Map<Long,String> results = documentIndexRepository.revisionsforIds(entityContexts);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(entityContextCreateOrUpdate.reourceId(), results.keySet().iterator().next());
		Assert.assertEquals(REVISION, results.values().iterator().next());
		
	}
	
	@Test
	public void revisionsforIdsEmptyInputCollection() {
		
		Assert.assertTrue(documentIndexRepository.revisionsforIds(entityContexts).isEmpty());
		Mockito.verifyZeroInteractions(restOperations);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void revisionsforIdsMixedResources() {
		entityContexts.add(entityContextCreateOrUpdate);
		entityContexts.add(new EntityContextImpl(19680528L, Resource.Subject));
		documentIndexRepository.revisionsforIds(entityContexts);
		
	}
	
	
	@Test
	public void revisionsforIdWithoutRevision() {
		final Collection<String> keys = new HashSet<>();
		keys.add(String.valueOf(entityContextCreateOrUpdate.reourceId()));
	    final Map<String,Object> idMap = new HashMap<>();
		idMap.put(DocumentIndexRestRepositoryImpl.KEY_ATTRIBUTE_NAME, keys);
		entityContexts.add(entityContextCreateOrUpdate);
		Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL,idMap, Map.class,Resource.Opportunity.urlPart())).thenReturn(resultMap(String.valueOf(entityContextCreateOrUpdate.reourceId()), null));
		
		Assert.assertTrue(documentIndexRepository.revisionsforIds(entityContexts).isEmpty());
		
	}
	
	
	private Map<String,?> resultMap(final String key, final String revision) {
		
		final Map<String,Object> results = new HashMap<>();
		final List<Map<String,?>> rows = new ArrayList<>(); 
		final Map<String,Object> row = new HashMap<>();
		rows.add(row);
		
		if ( revision != null){
		final Map<String,Object> valueMap = new HashMap<>();
		
		valueMap.put(DocumentIndexRestRepositoryImpl.REV_ATTRIBUTE_NAME, revision);
		
		row.put(DocumentIndexRestRepositoryImpl.VALUE_ATTRIBUTE_NAME, valueMap);
		}
		
		row.put(DocumentIndexRestRepositoryImpl.ID_ATTRIBUTE_NAME, key);
		results.put(DocumentIndexRestRepositoryImpl.ROWS_ATTRIBUTE_NAME, rows);
		
		
		return results;
		
	}

}
