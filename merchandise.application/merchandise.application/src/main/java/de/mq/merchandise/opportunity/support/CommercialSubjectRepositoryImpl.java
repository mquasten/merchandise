package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.AbstractRepository;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.ParameterImpl;
@Repository
@Profile("db")
public class CommercialSubjectRepositoryImpl extends AbstractRepository<CommercialSubject, Long> implements CommercialSubjectRepository  {
	
	
	
	
	@Autowired
	private PagingUtil pagingUtil;
	
	
	public CommercialSubjectRepositoryImpl() {
		
	}
	
	CommercialSubjectRepositoryImpl(final EntityManager entityManager, final PagingUtil entityManagerUtil){
		this.entityManager=entityManager;
		this.pagingUtil=entityManagerUtil;
	}
	
	
	public final Collection<CommercialSubject> forNamePattern(final Customer customer, final String namePattern, final Paging paging ) {
		return pagingUtil.countAndQuery(entityManager, CommercialSubject.class, paging, CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN , new ParameterImpl<String>(PARAMETER_SUBJECT_NAME, namePattern)  , new ParameterImpl<Long>(PARAMETER_CUSTOMER_ID, customer.id()));
	}

	

	@Override
	protected Class<? extends CommercialSubject> entityImplementationClass() {
		return CommercialSubjectImpl.class;
	}

	
}
