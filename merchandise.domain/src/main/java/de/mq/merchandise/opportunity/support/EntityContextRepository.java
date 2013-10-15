package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.util.Paging;

/**
 * DatabaseOperations for EntityContexts
 * @author mquasten
 *
 */
public interface EntityContextRepository   extends BasicRepository<EntityContext, Long>{
	
	static final String ENTITYCONTEXT_FOR_RESOURCE = "entityContextFprResource";
	
	static final String PARAMETER_RESOURCE= "resource";
	
	/**
	 * Fetch a collection with EntityContexts. paging is supported
	 * @param resource the resource that should be fetched
	 * @param paging the paging implementation 
	 * @return a collection with EntityContexts
	 */
	Collection<EntityContext> fetch(final Resource resource , final Paging paging); 
}
