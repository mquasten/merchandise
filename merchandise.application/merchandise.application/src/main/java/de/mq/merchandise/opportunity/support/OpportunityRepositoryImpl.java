package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.ParameterImpl;

@Repository
@Profile("db")
public class OpportunityRepositoryImpl extends AbstractRepository<Opportunity, Long> implements OpportunityRepository {
	
	
	
	
	
	
	@Autowired
	private PagingUtil pagingUtil; 
	
	OpportunityRepositoryImpl(final EntityManager entityManager, final PagingUtil pagingUtil) {
		this.entityManager = entityManager;
		this.pagingUtil = pagingUtil;
	}
	
	public  OpportunityRepositoryImpl() {
		
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.OpportunityRepository#forNamePattern(de.mq.merchandise.customer.Customer, java.lang.String, de.mq.merchandise.util.Paging)
	 */
	@Override
	public Collection<Opportunity> forNamePattern(final Customer customer, final String namePattern, final Paging paging ) {
		
		return pagingUtil.countAndQuery(entityManager, Opportunity.class, paging, OPPORTUNITY_FOR_NAME_PATTERN, new ParameterImpl<String>(PARAMETER_OPPORTUNITY_NAME, namePattern ), new ParameterImpl<Long>(PARAMETER_CUSTOMER_ID, customer.id() ));
	}

	@Override
	protected Class<? extends Opportunity> entityImplementationClass() {
		return OpportunityImpl.class;
	}

	
}
