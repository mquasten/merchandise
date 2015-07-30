package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

@Repository
class CommercialSubjectRepositoryImpl implements CommercialSubjectRepository {
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public final Collection<CommercialSubject> forCriteria(final Map<String,Object> criteria) {
		
		final TypedQuery<CommercialSubject> query = entityManager.createNamedQuery(COMMERCIAL_SUBJECT_BY_CRITERIA, CommercialSubject.class);
		return null;
		
	}

}
