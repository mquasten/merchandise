package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.ParameterImpl;

@Repository
@Profile("db")
public class OpportunityRepositoryImpl implements OpportunityRepository {
	
	
	static final String PARAMETER_SUBJECT_NAME = "name";
	static final String PARAMETER_CUSTOMER_ID = "customerId";
	
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PagingUtil entityManagerUtil; 
	
	OpportunityRepositoryImpl(final EntityManager entityManager, final PagingUtil entityManagerUtil) {
		this.entityManager = entityManager;
		this.entityManagerUtil = entityManagerUtil;
	}
	
	public  OpportunityRepositoryImpl() {
		
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.OpportunityRepository#forNamePattern(de.mq.merchandise.customer.Customer, java.lang.String, de.mq.merchandise.util.Paging)
	 */
	@Override
	public Collection<Opportunity> forNamePattern(final Customer customer, final String namePattern, final Paging paging ) {
		return entityManagerUtil.countAndQuery(entityManager, Opportunity.class, paging, OPPORTUNITY_FOR_NAME_PATTERN, new ParameterImpl<String>(PARAMETER_SUBJECT_NAME, namePattern ), new ParameterImpl<Long>(PARAMETER_CUSTOMER_ID, customer.id() ));
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.OpportunityRepository#save(de.mq.merchandise.opportunity.support.Opportunity)
	 */
	@Override
	public void save(final Opportunity opportunity) {
		entityManager.merge(opportunity);
	}
	
	@Override
	public void delete(final Opportunity opportunity) {
         final Opportunity result = entityManager.find(OpportunityImpl.class, opportunity.id());
         if( result == null){
        	 return;
         }
         entityManager.remove(result);
	}
	
	@Override
	public Opportunity forId(final Long id) {
		return  entityManager.find(OpportunityImpl.class, id);
		
	}

}
