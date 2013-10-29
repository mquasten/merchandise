package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.util.AbstractRepository;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.ParameterImpl;

@Repository
public class EntityContextRepositoryImpl extends AbstractRepository<EntityContext, Long> implements  EntityContextRepository{

	
	
	@Autowired
	private PagingUtil pagingUtil; 
	
	EntityContextRepositoryImpl(final EntityManager entityManager, final PagingUtil pagingUtil) {
		this.entityManager = entityManager;
		this.pagingUtil = pagingUtil;
	}
	
	
	public EntityContextRepositoryImpl() {
		entityManager=null;
		pagingUtil=null;
	}
	

	@Override
	protected Class<? extends EntityContext> entityImplementationClass() {
		return EntityContextImpl.class;
	}


	@Override
	public Collection<EntityContext> fetch(final Resource resource, Paging paging) {
		return  pagingUtil.countAndQuery(entityManager, EntityContext.class, paging, ENTITYCONTEXT_FOR_RESOURCE, new ParameterImpl<Resource>(PARAMETER_RESOURCE, resource ));
	}
	
	@Override
	public final EntityContextAggregation aggregation() {
		final Query query = entityManager.createNamedQuery(ENTITYCONTEXT_AGGREGATION);
		return (EntityContextAggregation) query.getSingleResult();
		
	}

}
