package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;



import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;



import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

@Repository
class SubjectRepositoryImpl implements SubjectRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectRepository#save(de.mq.merchandise.subject.Subject)
	 */
	@Override
	public final void save(final Subject subject) {
		final Optional<Long> id = entityManager.merge(subject).id();
		Assert.isTrue(id.isPresent(), " Id must be set, after save");
		ReflectionUtils.doWithFields(subject.getClass(), field -> {
			field.setAccessible(true);
			ReflectionUtils.setField(field, subject, id.get());
		} , field -> field.isAnnotationPresent(Id.class) );
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectRepository#subjectsForCustomer(de.mq.merchandise.customer.Customer)
	 */
	@Override
	public Collection<Subject> subjectsForCustomer(final Customer customer){
		Assert.notNull(customer, "Customer is mandatory");
		if(! customer.id().isPresent()) {
			return Collections.unmodifiableCollection(new ArrayList<>()); 
		}
		final TypedQuery<Subject> query = entityManager.createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class);
		query.setParameter(SubjectRepository.ID_PARAM_NAME, customer.id().get());
		return Collections.unmodifiableCollection(query.getResultList());
		
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectRepository#remove(de.mq.merchandise.subject.Subject)
	 */
	@Override
	public final void remove(final Subject subject) {
		Assert.notNull(subject, "Subject is mandatory");
		if( ! subject.id().isPresent() ) {
          return;			
		}
		
		final Subject toBeDeleted = entityManager.find(subject.getClass(), subject.id().get());
		if( toBeDeleted == null){
			return;
		}
		entityManager.remove(toBeDeleted);
	}
	
}
