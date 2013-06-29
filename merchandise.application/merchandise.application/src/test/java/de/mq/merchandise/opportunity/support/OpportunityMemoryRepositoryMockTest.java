package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;

import de.mq.merchandise.util.Parameter;

public class OpportunityMemoryRepositoryMockTest {
	
	private static final long OPPORTUNITY_ID = 4711L;

	private static final String PATTERN_SUB = ".*escort.*";
	
	private static final String PATTERN = "%escort%";

	private static final String OPPORTUNITY_NAME = "Best escort ever";

	private static final long CUSTOMER_ID = 19680528L;

	private final OpportunityMemoryRepositoryMock mockRepository = new OpportunityMemoryRepositoryMock();
	
	private final Opportunity opportunity = Mockito.mock(Opportunity.class);
	
	private final Customer customer = Mockito.mock(Customer.class);
	
	@SuppressWarnings("unchecked")
	final Parameter<String> nameParameter = Mockito.mock(Parameter.class);
	
	@SuppressWarnings("unchecked")
	final Parameter<Long> idParameter = Mockito.mock(Parameter.class);
	@Before
	public final void  setup() {
		Mockito.when(customer.id()).thenReturn(CUSTOMER_ID);
		Mockito.when(nameParameter.name()).thenReturn(OpportunityRepository.PARAMETER_OPPORTUNITY_NAME);
		Mockito.when(nameParameter.value()).thenReturn(PATTERN_SUB);
		Mockito.when(opportunity.customer()).thenReturn(customer);
		Mockito.when(idParameter.name()).thenReturn(OpportunityRepository.PARAMETER_CUSTOMER_ID);
		Mockito.when(idParameter.value()).thenReturn(CUSTOMER_ID);
		Mockito.when(opportunity.name()).thenReturn(OPPORTUNITY_NAME);
		Mockito.when(opportunity.id()).thenReturn(OPPORTUNITY_ID);
	}
	
	@Test
	public final void match() {
		
		
		Assert.assertTrue(mockRepository.match(opportunity, nameParameter, idParameter)); 
	}
	
	@Test
	public final void matchWrongName() {
		Mockito.when(opportunity.name()).thenReturn("Crime Banking Service");
		Assert.assertFalse(mockRepository.match(opportunity, nameParameter, idParameter)); 
	}
	
	@Test
	public final void matchWrongCustomer() {
		Mockito.when(customer.id()).thenReturn(CUSTOMER_ID*2);
		Assert.assertFalse(mockRepository.match(opportunity, nameParameter, idParameter)); 
	}
	
	@Test
	public final void compare() {
		final Opportunity otherOpportunity = Mockito.mock(Opportunity.class);
		Mockito.when(otherOpportunity.name()).thenReturn(OPPORTUNITY_NAME);
		Assert.assertEquals(0 , mockRepository.comparator().compare(opportunity, otherOpportunity));
	}
	
	
	
	@Test
	public final void forPattern() {
		final Paging paging = Mockito.mock(Paging.class);
		Mockito.when(paging.pageSize()).thenReturn(10);
	
		@SuppressWarnings("unchecked")
		final Map<Long,Opportunity> storedValues = (Map<Long, Opportunity>) ReflectionTestUtils.getField(mockRepository, "storedValues");
		storedValues.put(opportunity.id(), opportunity);
		
		final Opportunity otherOpportunity = Mockito.mock(Opportunity.class);
		Mockito.when(otherOpportunity.id()).thenReturn(2*OPPORTUNITY_ID);
		Mockito.when(otherOpportunity.name()).thenReturn("Crime Bank");
		Mockito.when(otherOpportunity.customer()).thenReturn(customer);
		storedValues.put(OPPORTUNITY_ID*2, otherOpportunity);
		
		final Collection<Opportunity> results = mockRepository.forNamePattern(customer, PATTERN, paging);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(opportunity, results.iterator().next());
	}

}
