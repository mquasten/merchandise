package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectRepository;
import de.mq.merchandise.util.Paging;
@Repository
@Profile("db")
public class CommercialSubjectRepositoryImpl implements CommercialSubjectRepository{
	
	
	static final String PARAMETER_NAME = "name";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public CommercialSubjectRepositoryImpl() {
		
	}
	
	CommercialSubjectRepositoryImpl(final EntityManager entityManager){
		this.entityManager=entityManager;
	}
	
	
	public final Collection<CommercialSubject> forNamePattern(final String namePattern, final Paging paging ) {
		
		final TypedQuery<Number> typedCountQuery = entityManager.createQuery(QueryUtils.createCountQueryFor(queryString(CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN)), Number.class);
	
		typedCountQuery.setParameter(PARAMETER_NAME , namePattern);
		paging.assignRowCounter(typedCountQuery.getSingleResult().longValue());
		
		final TypedQuery<CommercialSubject> typedResultQuery = entityManager.createQuery(queryString(CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN) +" order by " +paging.sortHint(), CommercialSubject.class);
		
		typedResultQuery.setFirstResult(paging.firstRow());
		typedResultQuery.setMaxResults(paging.pageSize());
		typedResultQuery.setParameter(PARAMETER_NAME, namePattern);
		return typedResultQuery.getResultList();
		
		
	}
	
	
	private String queryString(final String queryName) { 
	  return entityManager.createNamedQuery(queryName).unwrap(org.hibernate.Query.class).getQueryString().replaceFirst("[Oo][Rr][Dd][Ee][Rr].*[bB][Yy].*$", "");
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
}
