package de.mq.merchandise.opportunity.support;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;


import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;

public class DocumentRestRepositoryTest {
	
	private static final String ATTACHMENT_NAME = "kylie.jpg";

	private final RestOperations restOperations = Mockito.mock(RestOperations.class);
	
	private final DocumentRepository documentRepository = new DocumentRestRepositoryImpl(restOperations);
	
	private ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
	@SuppressWarnings("rawtypes")
	private ArgumentCaptor<Class> responseTypeCaptor = ArgumentCaptor.forClass(Class.class);
	
	
	@SuppressWarnings("rawtypes")
	private ArgumentCaptor<Map> parameterCaptor = ArgumentCaptor.forClass(Map.class);
	
	private static final Long id = 19680528L; 
	
	private static final String revision = new UUID(id, id).toString();
	
	private final static  Map<String,String> jsonResult = new HashMap<>();
	private BasicEntity basicEntity = Mockito.mock(OpportunityImpl.class);
	static {
	  
	   jsonResult.put("_rev", revision);
	   jsonResult.put("id", String.valueOf(id));
	}
	
	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		
		Mockito.when(basicEntity.id()).thenReturn(id);
		Mockito.when(restOperations.getForObject(urlCaptor.capture(), responseTypeCaptor.capture(), parameterCaptor.capture())).thenReturn(jsonResult);
	}
	
	@Test
	public final void revisionFor() {

       
		Assert.assertEquals(revision, documentRepository.revisionFor(basicEntity));
		Assert.assertEquals(DocumentRestRepositoryImpl.ENTITY_URL, urlCaptor.getValue());
		Assert.assertEquals(Map.class, responseTypeCaptor.getValue());
		Assert.assertEquals(2,  parameterCaptor.getValue().size());
		Assert.assertEquals(id, parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ID_PARAMETER));
		Assert.assertEquals(DocumentRestRepositoryImpl.OPPORTUNITIES_ENTITY, parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ENTITY_PARAMETER));
	}
	
	@Test
	public final void revisionForSupject() {
		basicEntity = Mockito.mock(CommercialSubjectImpl.class);
		Mockito.when(basicEntity.id()).thenReturn(id);
		
		Assert.assertEquals(revision, documentRepository.revisionFor(basicEntity));
		Assert.assertEquals(DocumentRestRepositoryImpl.ENTITY_URL, urlCaptor.getValue());
		Assert.assertEquals(Map.class, responseTypeCaptor.getValue());
		Assert.assertEquals(2,  parameterCaptor.getValue().size());
		Assert.assertEquals(id, parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ID_PARAMETER));
		Assert.assertEquals(DocumentRestRepositoryImpl.SUBJECTS_ENTITY, parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ENTITY_PARAMETER));
	}
	
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void revisionForNotDefinedResource() {
		basicEntity = Mockito.mock(Customer.class);
		Mockito.when(basicEntity.id()).thenReturn(id);
		documentRepository.revisionFor(basicEntity);
	}

	@SuppressWarnings("unchecked")
	@Test(expected=IllegalArgumentException.class)
	public final void revisionForBadJsonResult(){
		Mockito.when(restOperations.getForObject(urlCaptor.capture(), responseTypeCaptor.capture(), parameterCaptor.capture())).thenReturn(new HashMap<String, String>());
		documentRepository.revisionFor(basicEntity);
	}
	
	@SuppressWarnings("unchecked")
    @Test(expected=DataAccessResourceFailureException.class)
	public final void revisionForServerError(){
		Mockito.when(restOperations.getForObject(urlCaptor.capture(), responseTypeCaptor.capture(), parameterCaptor.capture())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
		documentRepository.revisionFor(basicEntity);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void revisionForEntityResourceNotExists() {
		final boolean [] likeAVirgin = {true}; // touched for the very fist time ...
		Mockito.when(restOperations.getForObject(urlCaptor.capture(), responseTypeCaptor.capture(), parameterCaptor.capture())).thenAnswer(new Answer<Map<String,String>>(){
            
			@Override
			public Map<String,String> answer(InvocationOnMock invocation) throws Throwable {
				Assert.assertEquals(DocumentRestRepositoryImpl.ENTITY_URL, invocation.getArguments()[0]);
				Assert.assertEquals(Map.class, invocation.getArguments()[1]);
				
				final Map<String,String> params = ((Map<String,String>)invocation.getArguments()[2]);
				Assert.assertEquals(2, params.size());
				Assert.assertEquals(id, params.get(DocumentRestRepositoryImpl.ID_PARAMETER));
				Assert.assertEquals(DocumentRestRepositoryImpl.OPPORTUNITIES_ENTITY, params.get(DocumentRestRepositoryImpl.ENTITY_PARAMETER));
				
				Assert.assertEquals(id, parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ID_PARAMETER));
				if ( likeAVirgin[0]){
					likeAVirgin[0]=false;
					throw new  HttpClientErrorException(HttpStatus.NOT_FOUND);
				}
				
				return jsonResult;
			} });
	
	  
		
		
		Assert.assertEquals(revision, documentRepository.revisionFor(basicEntity));
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Map> jsonMapCaptor = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(restOperations).put(urlCaptor.capture(), jsonMapCaptor.capture(), parameterCaptor.capture());
		Assert.assertEquals(DocumentRestRepositoryImpl.ENTITY_URL, urlCaptor.getValue());
		Assert.assertTrue(jsonMapCaptor.getValue().isEmpty());
		Assert.assertEquals(2,  parameterCaptor.getValue().size());
		Assert.assertEquals(id, parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ID_PARAMETER));
		Assert.assertEquals(DocumentRestRepositoryImpl.OPPORTUNITIES_ENTITY, parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ENTITY_PARAMETER));
		Assert.assertFalse(likeAVirgin[0]);
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void assign() {
		final InputStream is = Mockito.mock(InputStream.class);
		documentRepository.assign(basicEntity, ATTACHMENT_NAME, is, MediaType.IMAGE_JPEG);
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
		Mockito.verify(restOperations).put(urlCaptor.capture(), entityCaptor.capture(), parameterCaptor.capture());
		Assert.assertEquals(DocumentRestRepositoryImpl.ATTACHEMENT_URL, urlCaptor.getValue());
		Assert.assertEquals(is, entityCaptor.getValue().getBody());
		
		Assert.assertEquals(MediaType.IMAGE_JPEG , entityCaptor.getValue().getHeaders().getContentType());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=OptimisticLockingFailureException.class)
	public final void assignConflicted() {
		final InputStream is = Mockito.mock(InputStream.class);
		Mockito.doThrow(new HttpClientErrorException(HttpStatus.CONFLICT)).when(restOperations).put(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.anyMap());
		documentRepository.assign(basicEntity, ATTACHMENT_NAME, is, MediaType.IMAGE_JPEG);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=DataAccessResourceFailureException.class)
	public final void assignServererror() {
		final InputStream is = Mockito.mock(InputStream.class);
		Mockito.doThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(restOperations).put(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.anyMap());
		documentRepository.assign(basicEntity, ATTACHMENT_NAME, is, MediaType.IMAGE_JPEG);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void delete() {
		documentRepository.delete(basicEntity, ATTACHMENT_NAME);
		Mockito.verify(restOperations).delete(urlCaptor.capture(), parameterCaptor.capture());
		
		Assert.assertEquals(DocumentRestRepositoryImpl.ATTACHEMENT_URL,urlCaptor.getValue());
		
		Assert.assertEquals(4, parameterCaptor.getValue().size());
		Assert.assertEquals( id , parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ID_PARAMETER));
		Assert.assertEquals( revision , parameterCaptor.getValue().get(DocumentRestRepositoryImpl.REVISION_KEY));
		Assert.assertEquals( ATTACHMENT_NAME , parameterCaptor.getValue().get(DocumentRestRepositoryImpl.NAME_PARAMETER));
		Assert.assertEquals( DocumentRestRepositoryImpl.OPPORTUNITIES_ENTITY , parameterCaptor.getValue().get(DocumentRestRepositoryImpl.ENTITY_PARAMETER));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=DataAccessResourceFailureException.class)
	public final void deleteServerError() {
		
		Mockito.doThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(restOperations).delete(Mockito.anyString(), Mockito.anyMap());
		
		documentRepository.delete(basicEntity, ATTACHMENT_NAME);
		Mockito.verify(restOperations).delete(Mockito.anyString(), Mockito.anyMap() );
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void delete404() {
		
		Mockito.doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(restOperations).delete(Mockito.anyString(), Mockito.anyMap());
		
		documentRepository.delete(basicEntity, ATTACHMENT_NAME);
		Mockito.verify(restOperations).delete(Mockito.anyString(), Mockito.anyMap() );
	}

}
