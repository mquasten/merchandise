package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.Parameter;

public class EntityContextRepositoryTest {
	
	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private final PagingUtil pagingUtil = Mockito.mock(PagingUtil.class);
	
	private final EntityContextRepository entityContextRepository = new EntityContextRepositoryImpl(entityManager, pagingUtil);
	
	private final Paging paging = Mockito.mock(Paging.class);
	
	@SuppressWarnings("unchecked")
	@Test
	public final void fetch() {
		final Collection<EntityContext> results = new ArrayList<>();
		results.add(Mockito.mock(EntityContext.class));
		final ArgumentCaptor<EntityManager> entityManagerArgumentCaptor = ArgumentCaptor.forClass(EntityManager.class);
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> clazzArgumentCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<Paging> pagingArgumentCaptor = ArgumentCaptor.forClass(Paging.class);
		final ArgumentCaptor<String> queryArgumentCaptor = ArgumentCaptor.forClass(String.class);
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Parameter> parameterArgumentCaptor = ArgumentCaptor.forClass(Parameter.class);
		Mockito.when(pagingUtil.countAndQuery(entityManagerArgumentCaptor.capture(),clazzArgumentCaptor.capture(), pagingArgumentCaptor.capture(), queryArgumentCaptor.capture(), parameterArgumentCaptor.capture())).thenReturn(results);
		
		Assert.assertEquals(results, entityContextRepository.fetch(Resource.Opportunity, paging));
		
		Assert.assertEquals(entityManager, entityManagerArgumentCaptor.getValue());
		Assert.assertEquals(EntityContext.class, clazzArgumentCaptor.getValue());
		Assert.assertEquals(paging, pagingArgumentCaptor.getValue());
		Assert.assertEquals(EntityContextRepository.ENTITYCONTEXT_FOR_RESOURCE, queryArgumentCaptor.getValue());
		Assert.assertEquals(EntityContextRepository.PARAMETER_RESOURCE, parameterArgumentCaptor.getValue().name());
		
		Assert.assertEquals(Resource.Opportunity, parameterArgumentCaptor.getValue().value());
	}
	
	@Test
	public final void clazz() {
	     Assert.assertEquals(EntityContextImpl.class, ReflectionTestUtils.invokeMethod(entityContextRepository, "entityImplementationClass" ));
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new EntityContextRepositoryImpl());
	}

}
