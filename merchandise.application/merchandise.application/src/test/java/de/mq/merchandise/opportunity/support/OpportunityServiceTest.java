package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;



import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityRepository;
import de.mq.merchandise.opportunity.support.OpportunityService;
import de.mq.merchandise.opportunity.support.OpportunityServiceImpl;
import de.mq.merchandise.util.Paging;

public class OpportunityServiceTest {
	
	private final OpportunityRepository opportunityRepository = Mockito.mock(OpportunityRepository.class);
	
	private final OpportunityService opportunityService = new OpportunityServiceImpl(opportunityRepository);
	
	private final Paging paging = Mockito.mock(Paging.class);
	
	private final Customer customer = Mockito.mock(Customer.class);
	
	private final String pattern = "Pattern";
	
	@Test
	public final void opportunities() {
		
		final Collection<Opportunity>  results = new ArrayList<>();
		results.add(Mockito.mock(Opportunity.class));
		
		Mockito.when(opportunityRepository.forNamePattern(customer, pattern, paging)).thenReturn(results);
		
		Assert.assertEquals(results, opportunityService.opportunities(customer, pattern, paging));
	}

}
