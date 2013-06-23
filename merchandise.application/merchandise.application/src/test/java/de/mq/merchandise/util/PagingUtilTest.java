package de.mq.merchandise.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.opportunity.support.Opportunity;

public class PagingUtilTest {
	
	
	private static final int COUNT = 42;

	private static final String NAMED_QUERY = "EscortByCategory";

	private static final String QUERY_STRING = "Select o  from Opportunity o where ...";

	private final PagingUtil  pagingUtil = new PagingUtilImpl(); 
	
	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	private final Paging paging = Mockito.mock(Paging.class);
	
	@SuppressWarnings("unchecked")
	private TypedQuery<Opportunity> query = Mockito.mock(TypedQuery.class);
	private org.hibernate.Query hibernateQuery = Mockito.mock( org.hibernate.Query.class);
	
	@SuppressWarnings("unchecked")
	private TypedQuery<Number> countQuery = Mockito.mock(TypedQuery.class);
	
	
	@SuppressWarnings("unchecked")
	private TypedQuery<Opportunity> resultQuery = Mockito.mock(TypedQuery.class);
	
	@SuppressWarnings("unchecked")
	private Parameter<String> parameter = Mockito.mock(Parameter.class);
	
	@Test
	public final void countAndQuery(){
		Mockito.when(parameter.name()).thenReturn("category");
		Mockito.when(parameter.value()).thenReturn("platin");
		
		Mockito.when(entityManager.createNamedQuery(NAMED_QUERY)).thenReturn(query);
		Mockito.when(query.unwrap(org.hibernate.Query.class)).thenReturn(hibernateQuery);
		Mockito.when(hibernateQuery.getQueryString()).thenReturn(QUERY_STRING);
		// dirrrty
		Mockito.when(entityManager.createQuery(QUERY_STRING.replaceFirst("o", "count(o)").replaceAll("Select", "select").replaceAll("[ ]+", " "), Number.class)).thenReturn(countQuery);
		Mockito.when(countQuery.getSingleResult()).thenReturn(COUNT);
		
		Mockito.when(entityManager.createQuery(QUERY_STRING + " order by id", Opportunity.class)).thenReturn(resultQuery);
		Mockito.when(paging.sortHint()).thenReturn("id");
		
		final List<Opportunity> opportunities = new ArrayList<>();
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		opportunities.add(opportunity);
		Mockito.when(resultQuery.getResultList()).thenReturn(opportunities);
		
	    final Collection<Opportunity> result = pagingUtil.countAndQuery(entityManager, Opportunity.class, paging, NAMED_QUERY,  parameter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(opportunity, result.iterator().next() );
		
		Mockito.verify(paging).assignRowCounter(COUNT);

	}
	
	
	

}
