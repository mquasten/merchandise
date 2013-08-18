package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;

public interface OpportunityRepository extends BasicRepository<Opportunity, Long> {
	
	static final String OPPORTUNITY_FOR_NAME_PATTERN = "opportunityForNamePattern";
	
	static final String PARAMETER_OPPORTUNITY_NAME = "name";
	static final String PARAMETER_CUSTOMER_ID = "customerId";
	
	Collection<Opportunity> forNamePattern(final Customer customer, final String namePattern, final Paging paging);

	

}