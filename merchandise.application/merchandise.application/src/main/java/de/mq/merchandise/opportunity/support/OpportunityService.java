package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;

@Transactional(readOnly=true)
public interface OpportunityService {

	Collection<Opportunity> opportunities(final Customer customer, final String patternForName, final Paging paging);

}
