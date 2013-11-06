package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.opportunity.support.EntityContext.State;
import de.mq.merchandise.util.Paging;

public class DocumentReplicationServiceTest {
	
	final EntityContextRepository entityContextRepository = Mockito.mock(EntityContextRepository.class);
	
	final AOProxyFactory proxyFactory = Mockito.mock(AOProxyFactory.class);
	
	final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	
	final DocumentIndexRepository documentIndexRepository = Mockito.mock(DocumentIndexRepository.class);
	final BasicRepository<Opportunity, Long> basicRepository = Mockito.mock(OpportunityRepository.class);
	
	final DocumentReplicationService documentReplicationService = new DocumentReplicationServiceImpl(entityContextRepository, proxyFactory, beanResolver, documentIndexRepository, basicRepository);
	
	
	final EntityContextAggregation entityContextAggregation = Mockito.mock(EntityContextAggregation.class);
	
	
	//final Paging paging = Mockito.mock(Paging.class);
	
	
	final Collection<EntityContext> entityContexts = new ArrayList<>();
	
	
	
	
	@Test
	@SuppressWarnings({ "rawtypes" , "unchecked" })
	public final void replicate() {
		
		Mockito.when(entityContextAggregation.counter()).thenReturn((long) (DocumentReplicationServiceImpl.minCount +10), 0L);
		Mockito.when(entityContextAggregation.minDate()).thenReturn(new Date());
		
		Mockito.when(entityContextRepository.aggregation()).thenReturn(entityContextAggregation);
		
		
		final EntityContext entityContext = Mockito.mock(EntityContext.class);
		Mockito.when(entityContext.id()).thenReturn(1L);
		Mockito.when(entityContext.hasId()).thenReturn(true);
		Mockito.when(entityContext.reourceId()).thenReturn(1L);
		Mockito.when(entityContext.resource()).thenReturn(Resource.Opportunity);
		
		
		
		final EntityContext entityContextDelete = Mockito.mock(EntityContext.class);
		Mockito.when(entityContextDelete.id()).thenReturn(2L);
		Mockito.when(entityContextDelete.hasId()).thenReturn(true);
		Mockito.when(entityContextDelete.reourceId()).thenReturn(2L);
		Mockito.when(entityContextDelete.resource()).thenReturn(Resource.Opportunity);
		Mockito.when(entityContextDelete.isForDeleteRow()).thenReturn(true);
		
		
		final EntityContext entityContextDuplicate = Mockito.mock(EntityContext.class);
		Mockito.when(entityContextDuplicate.id()).thenReturn(3L);
		Mockito.when(entityContextDuplicate.hasId()).thenReturn(true);
		Mockito.when(entityContextDuplicate.reourceId()).thenReturn(1L);
		Mockito.when(entityContextDuplicate.resource()).thenReturn(Resource.Opportunity);
		
	
		entityContexts.add(entityContext);
		entityContexts.add(entityContextDelete);
		entityContexts.add(entityContextDuplicate);
		
		
		final ArgumentCaptor<Class> clazzArgumentCaptor = ArgumentCaptor.forClass(Class.class) ;
		final ArgumentCaptor<ModelRepository> modelRepositoryArgumentCaptor = ArgumentCaptor.forClass(ModelRepository.class) ;
		final OpportunityIndexAO opportunityIndexAO = Mockito.mock(OpportunityIndexAO.class);
		final OpportunityIndexAO opportunityIndexAODelete = Mockito.mock(OpportunityIndexAO.class);
		
		Mockito.when(entityContext.reference(RevisionAware.class)).thenReturn(opportunityIndexAO);
		
		
		Mockito.when(entityContextDelete.reference(RevisionAware.class)).thenReturn(opportunityIndexAODelete);
		
		Mockito.when(proxyFactory.createProxy(clazzArgumentCaptor.capture(), modelRepositoryArgumentCaptor.capture())).thenReturn(opportunityIndexAO);
		
		
		
		Opportunity opportunity = Mockito.mock(Opportunity.class);
		
		Mockito.when(basicRepository.forId(1L)).thenReturn(opportunity);
		
		final boolean callOnMe[] = {false};
		Mockito.when(entityContextRepository.fetch(Mockito.any(Resource.class), Mockito.any(Paging.class))).thenAnswer(new Answer<Collection<EntityContext>>() {

			@Override
			public Collection<EntityContext> answer(InvocationOnMock invocation) throws Throwable {
		
				final Paging paging = (Paging) invocation.getArguments()[1];
				paging.assignRowCounter(1L);
			
				Assert.assertEquals(Resource.Opportunity, invocation.getArguments()[0]);
				if(!callOnMe[0]){
					callOnMe[0]=true;
					return entityContexts;
				}
				return new ArrayList<>();
			} });
		
		
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				
				Mockito.when(entityContext.finished()).thenReturn(true);
				for(final EntityContext entityContext  : (Collection<EntityContext>) invocation.getArguments()[0]) {
				
					if( entityContext.id()==1){
					   Assert.fail("Entity should be skipped, filtered id: " + entityContext.id());
					}
					if( entityContext.id()==2){
						entityContext.assign(State.Conflict);
						Mockito.when(entityContext.finished()).thenReturn(true);
						Mockito.when(entityContext.error()).thenReturn(true);
					}
					if( entityContext.id()==3){
						entityContext.assign(State.Ok);
						Mockito.when(entityContext.finished()).thenReturn(true);
					   
					}
					
					
				}
				return null;
			}
		}).when(documentIndexRepository).updateDocuments(Mockito.anyCollection());
		
		
		
		documentReplicationService.replicate();
		
		
	
		
		Assert.assertEquals(OpportunityIndexAO.class, clazzArgumentCaptor.getValue());
	//	final ModelRepository modelRepository = modelRepositoryArgumentCaptor.getValue();
		int i=0;
		for(ModelRepository modelRepository : modelRepositoryArgumentCaptor.getAllValues()){
	
		Map<?,Opportunity> entities  =  (Map<?, Opportunity>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		Assert.assertEquals(1, entities.size());
		final Opportunity entity = entities.values().iterator().next();
		if(i==0){
			Assert.assertEquals(opportunity, entities.values().iterator().next());
		}
		
		if( i==1) {
			Assert.assertEquals(2, entity.id());
		}
		
		
		i++;
		}
		Mockito.verify(documentIndexRepository).updateDocuments(new HashSet(entityContexts));
		Mockito.verify(entityContext).assign(State.Skipped);
		Mockito.verify(entityContextDelete).assign(State.Conflict);
		Mockito.verify(entityContextRepository).delete(3L);
		Mockito.verify(entityContextDuplicate).assign(State.Ok);
		Mockito.verify(entityContextRepository).save(entityContextDelete);
		Mockito.verify(entityContextRepository).delete(1L);
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void replicateCounterReached() {
		Mockito.when(entityContextAggregation.counter()).thenReturn((long) (DocumentReplicationServiceImpl.maxProcess *10), 0L);
		Mockito.when(entityContextAggregation.minDate()).thenReturn(new Date());
		
		Mockito.when(entityContextRepository.aggregation()).thenReturn(entityContextAggregation);
		for( long i=1; i < DocumentReplicationServiceImpl.maxProcess *10   ; i++){
		final EntityContext entityContext = Mockito.mock(EntityContext.class);
		Mockito.when(entityContext.id()).thenReturn(i);
		Mockito.when(entityContext.hasId()).thenReturn(true);
		Mockito.when(entityContext.reourceId()).thenReturn(i);
		Mockito.when(entityContext.resource()).thenReturn(Resource.Opportunity);
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		Mockito.when(basicRepository.forId(i)).thenReturn(opportunity);
		entityContexts.add(entityContext);
		}
		
		
		final boolean callOnMe[] = {false};
		Mockito.when(entityContextRepository.fetch(Mockito.any(Resource.class), Mockito.any(Paging.class))).thenAnswer(new Answer<Collection<EntityContext>>() {

			@Override
			public Collection<EntityContext> answer(InvocationOnMock invocation) throws Throwable {
		
				Assert.assertEquals(Resource.Opportunity, invocation.getArguments()[0]);
				if(!callOnMe[0]){
					callOnMe[0]=true;
					return entityContexts;
				}
				return new ArrayList<>();
			} });
		
		final OpportunityIndexAO opportunityIndexAO = Mockito.mock(OpportunityIndexAO.class);
		Mockito.when(proxyFactory.createProxy((Class<?>) Mockito.any(), (ModelRepository) Mockito.anyObject())).thenReturn(opportunityIndexAO);
		
		Mockito.doAnswer(new Answer<Void>() {

			
			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				
				
				for(final EntityContext entityContext  : (Collection<EntityContext>) invocation.getArguments()[0]) {
				
					
					if(entityContext.id() <  DocumentReplicationServiceImpl.maxProcess){
						entityContext.assign(State.Ok);
						Mockito.when(entityContext.finished()).thenReturn(true);
					}  
					
					
					
				}
				return null;
			}
		}).when(documentIndexRepository).updateDocuments(Mockito.anyCollection());
		
		
		documentReplicationService.replicate();
		
		
		
		final Collection<EntityContext> results = idsLessThan(entityContexts, DocumentReplicationServiceImpl.maxProcess);
		
		Mockito.verify(documentIndexRepository).updateDocuments(results);
	
		for(EntityContext result: entityContexts){
			if( result.id() < DocumentReplicationServiceImpl.maxProcess){
				Mockito.verify(entityContextRepository).delete(result.id());
				Mockito.verify(result).assign(State.Ok);
			}else {
				Mockito.verify(result, Mockito.never()).assign(Mockito.any(State.class));
				Mockito.verify(entityContextRepository, Mockito.never()).delete(result.id());
				Mockito.verify(entityContextRepository, Mockito.never()).save(result);
			}
			
		}
		
		
	}
	
	
	
	private Collection<EntityContext>idsLessThan(final Collection<EntityContext> entityContexts, final long maxId) {
		final Collection<EntityContext> results = new HashSet<>();
		for(final EntityContext entityContext : entityContexts){
			if( entityContext.id() > maxId){
				continue;
			}
			results.add(entityContext);
		}
	
		return results;
		
	}

}
