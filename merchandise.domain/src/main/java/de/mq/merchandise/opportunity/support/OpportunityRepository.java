package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;

public interface OpportunityRepository {
	
	static final String OPPORTUNITY_FOR_NAME_PATTERN = "subjectForNamePattern";
	
	Collection<Opportunity> forNamePattern(final Customer customer, final String namePattern, final Paging paging);

	void save(final Opportunity opportunity);

	Opportunity forId(final Long id);

	void delete(Opportunity opportunity);

}