package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.Parameter;
import de.mq.merchandise.util.ParameterImpl;

public class OpportunityRepositoryTest {
	
   private final EntityManager entityManager = Mockito.mock(EntityManager.class);	
   
   private PagingUtil pagingUtil = Mockito.mock(PagingUtil.class);
	
   @Test
   public final void create() {	
	   final OpportunityRepository opportunityRepository = new OpportunityRepositoryImpl(entityManager, pagingUtil);
	   Assert.assertEquals(entityManager, ReflectionTestUtils.getField(opportunityRepository, "entityManager"));
	   Assert.assertEquals(pagingUtil,  ReflectionTestUtils.getField(opportunityRepository, "pagingUtil"));
   }
   @Test
   public final void createDefault() {
	   Assert.assertNotNull(new OpportunityRepositoryImpl())  ; 
   }
   
   @Test
   public final void clazz(){
	   Assert.assertEquals(OpportunityImpl.class, new OpportunityRepositoryImpl().entityImplementationClass());
   }
   
   @Test
   public final void forPattern() {
	   final OpportunityRepository opportunityRepository = new OpportunityRepositoryImpl(entityManager, pagingUtil);
	   final Customer customer = Mockito.mock(Customer.class);
	   
	   final Paging paging = Mockito.mock(Paging.class);
	   final Parameter<String> nameParameter =new ParameterImpl<String>(OpportunityRepository.PARAMETER_OPPORTUNITY_NAME,null);
	   final List<Opportunity> results = new ArrayList<Opportunity>();
	   results.add(Mockito.mock(Opportunity.class));
	   final Parameter<Long> idParameter = new ParameterImpl<Long>(OpportunityRepository.PARAMETER_CUSTOMER_ID, null);
	 
	   
	   Mockito.when(pagingUtil.countAndQuery(entityManager, Opportunity.class, paging, OpportunityRepository.OPPORTUNITY_FOR_NAME_PATTERN, nameParameter, idParameter)).thenReturn(results);
	   
	   Assert.assertEquals(results, opportunityRepository.forNamePattern(customer, "Best Escort ever", paging));
   }

}
