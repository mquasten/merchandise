package de.mq.merchandise.opportunity.support;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.opportunity.support.EntityContext.State;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.SimplePagingImpl;

public class DocumentReplicationServiceImpl {
	
	private int minCount=50;
	
	private int minAgeSec=300;
	
	private EntityContextRepository entityContextRepository;
	
	
	private BasicRepository<BasicEntity, Long> basicRepository;
	
	private AOProxyFactory proxyFactory; 
	
	private BeanResolver beanResolver;
	
	
	private DocumentIndexRepository documentIndexRepository;
	
	public final void replicate() {
		
		
		final EntityContextAggregation aggregation = entityContextRepository.aggregation();
		
		
		if((aggregation.counter() >= minCount )) {
			doReplication();
		} 
		
		if( new Date().getTime() - aggregation.minDate().getTime() >= minAgeSec ){
			doReplication();
		}
		
		
	}
	
	
	private void doReplication() {
		final Map<Long,EntityContext> entityContexts = new HashMap<Long,EntityContext>();
		final Set<EntityContext> inDenStaub = new HashSet<EntityContext>();
		filter(entityContexts, inDenStaub);
		for(final EntityContext entityContext : entityContexts.values()){
			final BasicEntity entity = basicRepository.forId(entityContext.reourceId());
			final RevisionAware reference = proxyFactory.createProxy(OpportunityIndexAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(entity).build());
			entityContext.assign(RevisionAware.class, reference);
		}
		final Set<EntityContext> pocessed = new HashSet<>();
		documentIndexRepository.updateDocuments(pocessed);
		pocessed.addAll(inDenStaub);
		for(final EntityContext entityContext : pocessed){
			if (! entityContext.finished() ) {
				continue;
			}
			if( entityContext.error() ) {
				entityContextRepository.save(entityContext);
				continue;
			}
			entityContextRepository.delete(entityContext.id());
		}
		
		replicate();
	}


	private void filter(final Map<Long, EntityContext> entityContexts, final Set<EntityContext> inDenStaub) {
		final Paging paging = new SimplePagingImpl(100, "id");
		for(int page =0;  page< paging.pageSize(); page++){
			if( filterRow(paging,entityContexts, inDenStaub) ) {
				break;
			}
			paging.next();
		}
	}


	private boolean  filterRow(final Paging paging, final Map<Long,EntityContext> distinctResources, final Set<EntityContext> dupplicateIds) {
		
		
		for(final EntityContext entityContext: entityContextRepository.fetch(Resource.Opportunity, paging)){
		    if( distinctResources.containsKey(entityContext.reourceId())){
		    	final EntityContext skippedEntityContext = distinctResources.get(entityContext.reourceId());
		    	skippedEntityContext.assign(State.Skipped);
				dupplicateIds.add(skippedEntityContext);
		    }
			
		    distinctResources.put(entityContext.reourceId(), entityContext);
		    if(distinctResources.size()>=100){
		    	return true;
		    }
		}
		return false;
	}


	
	

}
