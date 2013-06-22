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
public class CommercialSubjectRepositoryImpl implements CommercialSubjectRepository{
	
	
	static final String PARAMETER_SUBJECT_NAME = "name";
	static final String PARAMETER_CUSTOMER_ID = "customerId";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PagingUtil entityManagerUtil; 
	
	public CommercialSubjectRepositoryImpl() {
		
	}
	
	CommercialSubjectRepositoryImpl(final EntityManager entityManager, final PagingUtil entityManagerUtil){
		this.entityManager=entityManager;
		this.entityManagerUtil=entityManagerUtil;
	}
	
	
	public final Collection<CommercialSubject> forNamePattern(final Customer customer, final String namePattern, final Paging paging ) {
		
		return entityManagerUtil.countAndQuery(entityManager, CommercialSubject.class, paging, CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN , new ParameterImpl<String>(PARAMETER_SUBJECT_NAME, namePattern)  , new ParameterImpl<Long>(PARAMETER_CUSTOMER_ID, customer.id()));
		
	}
	
	
	

	@Override
	public final  CommercialSubject save(final CommercialSubject commercialSubject) {
		return entityManager.merge(commercialSubject);
	}

	@Override
	public void delete(final CommercialSubject commercialSubject) {
		idExistsGuard(commercialSubject);
		final CommercialSubject existing = entityManager.find(commercialSubject.getClass(), commercialSubject.id());
		if( existing==null){
			return;
		}
		entityManager.remove(existing);
		
	}

	private void idExistsGuard(CommercialSubject commercialSubject) {
		if( ! commercialSubject.hasId()){
			throw new IllegalArgumentException("Id not exists, given commercialSubject isn't persistent.");
		}
	}

	@Override
	public CommercialSubject forId(final Long id) {
		return entityManager.find(CommercialSubjectImpl.class, id);
	}
}
