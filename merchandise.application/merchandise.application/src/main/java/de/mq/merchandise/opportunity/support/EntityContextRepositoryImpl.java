package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import de.mq.merchandise.util.AbstractRepository;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.ParameterImpl;

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
	public Collection<EntityContext> fetch(Resource resource, Paging paging) {
		return  pagingUtil.countAndQuery(entityManager, EntityContext.class, paging, ENTITYCONTEXT_FOR_RESOURCE, new ParameterImpl<Resource>(PARAMETER_RESOURCE, resource ));
	}

}
