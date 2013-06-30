package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.BasicServiceImpl;
import de.mq.merchandise.util.Paging;

@Service
@Transactional(readOnly=true )
public class OpportunityServiceImpl extends BasicServiceImpl<Opportunity> implements  OpportunityService {

	@Autowired
	public OpportunityServiceImpl(final OpportunityRepository opportunityRepository) {
		super(opportunityRepository);
	}
	
	@Override
	public final Collection<Opportunity> opportunities(final Customer customer, final String patternForName, final Paging paging) {
		return ((OpportunityRepository) repository).forNamePattern(customer, patternForName, paging);
		
	}


}
