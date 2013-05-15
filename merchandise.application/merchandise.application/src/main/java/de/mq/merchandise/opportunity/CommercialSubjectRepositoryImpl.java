package de.mq.merchandise.opportunity;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.jpa.repository.query.QueryUtils;

import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectRepository;
import de.mq.merchandise.util.Paging;

public class CommercialSubjectRepositoryImpl implements CommercialSubjectRepository{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public CommercialSubjectRepositoryImpl() {
		
	}
	
	CommercialSubjectRepositoryImpl(final EntityManager entityManager){
		this.entityManager=entityManager;
	}
	
	
	public final Collection<? extends CommercialSubject> forNamePattern(final String namePattern, final Paging paging ) {
		
		final TypedQuery<Number> typedCountQuery = entityManager.createQuery(QueryUtils.createCountQueryFor(queryString(CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN)), Number.class);
	
		typedCountQuery.setParameter("name" , namePattern);
		paging.assignRowCounter(typedCountQuery.getSingleResult().longValue());
		
		final TypedQuery<CommercialSubject> typedResultQuery = entityManager.createQuery(queryString(CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN) +" order by " +paging.sortHint(), CommercialSubject.class);
		
		typedResultQuery.setFirstResult(paging.firstRow());
		typedResultQuery.setMaxResults(paging.pageSize());
		typedResultQuery.setParameter("name", namePattern);
		return typedResultQuery.getResultList();
		
	}
	
	
	private String queryString(final String queryName) { 
	  return entityManager.createNamedQuery(queryName).unwrap(org.hibernate.Query.class).getQueryString().replaceFirst("[Oo][Rr][Dd][Ee][Rr].*[bB][Yy].*$", "");
	}
}
