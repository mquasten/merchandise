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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;

import de.mq.merchandise.opportunity.support.EntityContext.State;
import de.mq.merchandise.util.EntityUtil;

public class DocumentIndexRepositoryTest {
	
	private static final String REVISION = "revision-4711";
	private static final String REVISION_DELETE = "revision-4712";

	private final RestOperations restOperations = Mockito.mock(RestOperations.class);
	
	private final  OpportunityIndexRepository  documentIndexRepository = new DocumentIndexRestRepositoryImpl(restOperations);
	
	private final EntityContext entityContextUpdate = new EntityContextImpl(19680528L, Resource.Opportunity, false);
	
	private final EntityContext entityContextNew = new EntityContextImpl(4711L, Resource.Opportunity, false);
	
	private final EntityContext entityContextDelete = new EntityContextImpl(4712L, Resource.Opportunity, true);
	
	private final  Collection<EntityContext> entityContexts = new ArrayList<>();
	
	
	
	@Test
	public void revisionsforIds() {
		entityContexts.add(entityContextUpdate);
		Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL,idMap(), Map.class,Resource.Opportunity.urlPart())).thenReturn(resultMap(new String[] {String.valueOf(entityContextUpdate.reourceId())}, new String[] {REVISION}));
		
		final Map<Long,String> results = documentIndexRepository.revisionsforIds(entityContexts);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(entityContextUpdate.reourceId(), results.keySet().iterator().next());
		Assert.assertEquals(REVISION, results.values().iterator().next());
		
	}

	private Map<String, Object> idMap(Long ... ids ) {
		final Collection<String> keys = new HashSet<>();
		keys.add(String.valueOf(entityContextUpdate.reourceId()));
		for(Long id : ids ){
			keys.add(String.valueOf(id));
		}
	    final Map<String,Object> idMap = new HashMap<>();
		idMap.put(DocumentIndexRestRepositoryImpl.KEY_ATTRIBUTE_NAME, keys);
		return idMap;
	}
	
	@Test
	public void revisionsforIdsEmptyInputCollection() {
		
		Assert.assertTrue(documentIndexRepository.revisionsforIds(entityContexts).isEmpty());
		Mockito.verifyZeroInteractions(restOperations);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void revisionsforIdsMixedResources() {
		entityContexts.add(entityContextUpdate);
		entityContexts.add(new EntityContextImpl(19680528L, Resource.Subject));
		documentIndexRepository.revisionsforIds(entityContexts);
		
	}
	
	
	@Test
	public void revisionsforIdWithoutRevision() {
		final Map<String, Object> idMap = idMap();
		entityContexts.add(entityContextUpdate);
		Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL,idMap, Map.class,Resource.Opportunity.urlPart())).thenReturn(resultMap(new String[]{String.valueOf(entityContextUpdate.reourceId())}, null));
		
		Assert.assertTrue(documentIndexRepository.revisionsforIds(entityContexts).isEmpty());
		
	}
	
	
	private Map<String,?> resultMap(final String[] keys, final String[] revisions) {
		
		final Map<String,Object> results = new HashMap<>();
		final List<Map<String,?>> rows = new ArrayList<>(); 
		
		
		if (revisions != null){
		for(int i=0; i < keys.length; i++){
			addRow(keys[i], revisions[i], rows);
		}
		
		}
		
		results.put(DocumentIndexRestRepositoryImpl.ROWS_ATTRIBUTE_NAME, rows);
		
		return results;
		
	}

	private void addRow(final String key, final String revision, final List<Map<String, ?>> rows) {
		final Map<String,Object> row = new HashMap<>();
		rows.add(row);
		
		if ( revision != null){
		final Map<String,Object> valueMap = new HashMap<>();
		
		valueMap.put(DocumentIndexRestRepositoryImpl.REV_ATTRIBUTE_NAME, revision);
		
		row.put(DocumentIndexRestRepositoryImpl.VALUE_ATTRIBUTE_NAME, valueMap);
		}
		
		row.put(DocumentIndexRestRepositoryImpl.ID_ATTRIBUTE_NAME, key);
	}
	
	@Test
	public final void updateDocuments() {
		final Collection<EntityContext> entityContexts = new ArrayList<>(); 
		final RevisionAware revisionAware=Mockito.mock(RevisionAware.class);
		final RevisionAware revisionAwareForCopy=Mockito.mock(RevisionAware.class);
		final RevisionAware revisionAwareForNew=Mockito.mock(RevisionAware.class);
		final RevisionAware revisionAwareForDelete=Mockito.mock(RevisionAware.class);
		
	
		
		final EntityContext entityContextCopy = EntityUtil.copy(entityContextUpdate);
		entityContextCopy.assign(RevisionAware.class, revisionAwareForCopy);
		
		entityContextUpdate.assign(RevisionAware.class, revisionAware);
		ReflectionTestUtils.setField(entityContextUpdate, "state", State.New);
		
		entityContextNew.assign(RevisionAware.class, revisionAwareForNew);
		
		entityContextDelete.assign(RevisionAware.class, revisionAwareForDelete);
		
		entityContexts.add(entityContextCopy);
		entityContexts.add(entityContextUpdate);
		entityContexts.add(entityContextNew);
		entityContexts.add(entityContextDelete);
		
		
		final Collection<Object> aos = new HashSet<>();
		
		
		aos.add(revisionAware);
		aos.add(revisionAwareForNew);
		aos.add(revisionAwareForDelete);
		
		final Map<String,Collection<?>> rows = new HashMap<>();
		rows.put(DocumentIndexRestRepositoryImpl.DOCS_ATTRIBUTE, aos);
		final List<Object> results = new ArrayList<>();
		
		final Map<String,Object> result = new HashMap<>();
		result.put(DocumentIndexRestRepositoryImpl.ID_ATTRIBUTE_NAME, ""+ entityContextUpdate.reourceId());
		
		final Map<String,Object> resultNew = new HashMap<>();
		resultNew.put(DocumentIndexRestRepositoryImpl.ID_ATTRIBUTE_NAME, ""+ entityContextNew.reourceId());
		resultNew.put(DocumentIndexRestRepositoryImpl.ERROR_ATTRIBUTE_NAME, "DontLetMeGetMe");
		
		
		final Map<String,Object> resultDelete = new HashMap<>();
		resultDelete.put(DocumentIndexRestRepositoryImpl.ID_ATTRIBUTE_NAME, ""+ entityContextDelete.reourceId());
		
		resultDelete.put(DocumentIndexRestRepositoryImpl.ERROR_ATTRIBUTE_NAME,  State.Conflict.name());
		
		results.add(result);
		results.add(resultNew);
		
		results.add(resultDelete);
		
		Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL_UPDATE, rows, List.class, Resource.Opportunity.urlPart())).thenReturn(results);
		
		
		Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL,idMap(entityContextNew.reourceId(), entityContextDelete.reourceId()), Map.class,Resource.Opportunity.urlPart())).thenReturn(resultMap(new String[] { String.valueOf(entityContextUpdate.reourceId()),  String.valueOf(entityContextDelete.reourceId())},new String[] { REVISION, REVISION_DELETE}));
		
		
	
		
		documentIndexRepository.updateDocuments(entityContexts);
		
		Assert.assertEquals(State.Ok, ReflectionTestUtils.getField(entityContextUpdate, "state"));
		
		Assert.assertEquals(State.Skipped, ReflectionTestUtils.getField(entityContextCopy, "state"));
		
		Assert.assertEquals(State.Unkown,  ReflectionTestUtils.getField(entityContextNew, "state"));
		
		Assert.assertEquals(State.Conflict, ReflectionTestUtils.getField(entityContextDelete, "state"));
		
		
		Mockito.verify(revisionAware).setRevision(REVISION);
		Mockito.verify(revisionAware, Mockito.never()).setDeleted(Mockito.anyBoolean());
		
		
		Mockito.verify(revisionAwareForCopy).setRevision(REVISION);
		Mockito.verify(revisionAwareForCopy, Mockito.never()).setDeleted(Mockito.anyBoolean());
		
		Mockito.verify(revisionAwareForDelete).setRevision(REVISION_DELETE);
		Mockito.verify(revisionAwareForDelete).setDeleted(true);
		
		Mockito.verifyZeroInteractions(revisionAwareForNew);
		
		
		
	}
	
	@Test
	public final void updateDocumentsEmptyInputList() {
		documentIndexRepository.updateDocuments(new ArrayList<EntityContext>());
		Mockito.verifyZeroInteractions(restOperations);
	}
	
	
	@Test(expected=IllegalStateException.class)
	public final void  updateDocumentIdNotExists() {
		
			final Collection<EntityContext> entityContexts = new ArrayList<>(); 
			final RevisionAware revisionAware=Mockito.mock(RevisionAware.class);
			entityContextUpdate.assign(RevisionAware.class, revisionAware);
			entityContexts.add(entityContextUpdate);
			Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL,idMap(), Map.class,Resource.Opportunity.urlPart())).thenReturn(resultMap(new String[] {""+entityContextUpdate.reourceId()}, new String[] {REVISION}));
		
			final List<Object> results = new ArrayList<>();
			final Collection<Object> aos = new HashSet<>();
			aos.add(revisionAware);
			
			
			final Map<String,Object> result = new HashMap<>();
			result.put(DocumentIndexRestRepositoryImpl.ID_ATTRIBUTE_NAME, "1234");
			results.add(result);
			
			
			final Map<String,Collection<?>> rows = new HashMap<>();
			rows.put(DocumentIndexRestRepositoryImpl.DOCS_ATTRIBUTE, aos);
			
			Mockito.when(restOperations.postForObject(DocumentIndexRestRepositoryImpl.URL_UPDATE, rows, List.class, Resource.Opportunity.urlPart())).thenReturn(results);
			
			documentIndexRepository.updateDocuments(entityContexts);
			
	}

}
