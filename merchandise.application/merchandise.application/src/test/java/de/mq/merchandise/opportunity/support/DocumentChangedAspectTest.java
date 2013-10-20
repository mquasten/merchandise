package de.mq.merchandise.opportunity.support;

import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.BasicRepository;

import de.mq.merchandise.util.EntityUtil;

public class DocumentChangedAspectTest {
	
	private static final Long RESOURCE_ID = 19680528L;
	private final EntityContextRepository  entityContextRepository = Mockito.mock(EntityContextRepository.class);
	
	
	@Test
	public final void create() {
		final  DocumentChangedAspectImpl  documentChangedAspect  = new DocumentChangedAspectImpl(entityContextRepository);
		@SuppressWarnings("unchecked")
		final Map<Class<?>, Resource> resources = (Map<Class<?>, Resource>) ReflectionTestUtils.getField(documentChangedAspect, "resources");
	    Assert.assertEquals(Resource.values().length, resources.size());
	    
	    for(final Entry<Class<?>, Resource> entry : resources.entrySet()){
	    	Assert.assertEquals(entry.getValue().entityClass(), entry.getKey());
	    }
	}
	
	@Test
	public final void entityContextCreateOrUpdate() {
		final  DocumentChangedAspectImpl  documentChangedAspect  = new DocumentChangedAspectImpl(entityContextRepository);
		final BasicEntity entity = EntityUtil.create(OpportunityImpl.class);
		ReflectionTestUtils.setField(entity, "id", RESOURCE_ID);
		
		documentChangedAspect.entityContextCreateOrUpdate(entity);
		final ArgumentCaptor<EntityContextImpl> result = ArgumentCaptor.forClass(EntityContextImpl.class);
		
		Mockito.verify(entityContextRepository).save(result.capture());
		
		Assert.assertEquals(RESOURCE_ID, result.getValue().reourceId());
		Assert.assertEquals(Resource.Opportunity, result.getValue().resource());
		Assert.assertFalse(result.getValue().isForDeleteRow());
		
	}
	
	@Test
	public final void entityContextCreateOrUpdateWrongEntityNothingToDo(){
		final  DocumentChangedAspectImpl  documentChangedAspect  = new DocumentChangedAspectImpl(entityContextRepository);
		
		final BasicEntity entity = Mockito.mock(BasicEntity.class);
		Mockito.when(entity.hasId()).thenReturn(true);
		Mockito.when(entity.id()).thenReturn(RESOURCE_ID);
		documentChangedAspect.entityContextCreateOrUpdate(entity);
		
		Mockito.verifyZeroInteractions(entityContextRepository);
	}
	
	@Test
	public final void entityContextDelete() {
		final BasicRepository<CommercialSubject, Long> basicRepository = EntityUtil.create(CommercialSubjectRepositoryImpl.class);
		final  DocumentChangedAspectImpl  documentChangedAspect  = new DocumentChangedAspectImpl((EntityContextRepository) entityContextRepository);
		final BasicEntity entity = Mockito.mock(CommercialSubjectImpl.class);
		ReflectionTestUtils.setField(entity, "id", RESOURCE_ID);
		
		final ArgumentCaptor<EntityContextImpl> result = ArgumentCaptor.forClass(EntityContextImpl.class);
		documentChangedAspect.entityContextDelete(RESOURCE_ID, basicRepository);
		
		Mockito.verify(entityContextRepository).save(result.capture());
		Assert.assertEquals(RESOURCE_ID, result.getValue().reourceId());
		Assert.assertEquals(Resource.Subject, result.getValue().resource());
		Assert.assertTrue(result.getValue().isForDeleteRow());
		
	}
	
	@Test
	public final void entityContextDeleteWrongEntityNothingToDo() {
		final BasicRepository<CommercialSubject, Long> basicRepository = EntityUtil.create(CommercialSubjectRepositoryImpl.class);
		final  DocumentChangedAspectImpl  documentChangedAspect  = new DocumentChangedAspectImpl((EntityContextRepository) entityContextRepository);
		final BasicEntity entity = Mockito.mock(BasicEntity.class);
		Mockito.when(entity.hasId()).thenReturn(true);
		Mockito.when(entity.id()).thenReturn(RESOURCE_ID);
		
		@SuppressWarnings("unchecked")
		final Map<Class<?>, Resource> resources = (Map<Class<?>, Resource>) ReflectionTestUtils.getField(documentChangedAspect, "resources");
		resources.clear();
		
		documentChangedAspect.entityContextDelete(RESOURCE_ID, basicRepository);
		Mockito.verifyZeroInteractions(entityContextRepository);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void entityContextDeleteWrongTargetRepository() {
	
		final BasicRepository<?, ?> basicRepository = Mockito.mock(BasicRepository.class);
		final  DocumentChangedAspectImpl  documentChangedAspect  = new DocumentChangedAspectImpl((EntityContextRepository) entityContextRepository);
		final BasicEntity entity = Mockito.mock(BasicEntity.class);
		Mockito.when(entity.hasId()).thenReturn(true);
		Mockito.when(entity.id()).thenReturn(RESOURCE_ID);
		
		documentChangedAspect.entityContextDelete(RESOURCE_ID, basicRepository);
	}

}
