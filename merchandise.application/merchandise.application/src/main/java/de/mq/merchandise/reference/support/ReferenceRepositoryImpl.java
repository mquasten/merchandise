package de.mq.merchandise.reference.support;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceRepository;

@Repository
@Profile("db")
class ReferenceRepositoryImpl implements ReferenceRepository {

	@PersistenceContext
	private  EntityManager entityManager;
	
	
	public ReferenceRepositoryImpl(){
		
	}
	
	ReferenceRepositoryImpl(final EntityManager entityManager){
		this.entityManager=entityManager;
	}
	
	@Override
	public final List<Reference> forType(final Kind referenceType) {
		
		return entityManager.createNamedQuery(ReferenceRepository.QUERY_FOR_TYPE, Reference.class).setParameter("referenceType", referenceType).getResultList();
	}

}
