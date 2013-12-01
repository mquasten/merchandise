package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.opportunity.support.EntityContext.State;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.SimplePagingImpl;

public class DocumentReplicationServiceImpl implements DocumentReplicationService {
	
	static final int minCount=50;
	
	static final int minAgeSec=300;
	
	static final int maxProcess = 100;
	
	static final  int maxDelete = 200;
	
	private final EntityContextRepository entityContextRepository;
	
	
	private  final BasicRepository<? extends BasicEntity, Long> basicRepository;
	
	private final AOProxyFactory proxyFactory; 
	
	private final BeanResolver beanResolver;
	
	public DocumentReplicationServiceImpl(final EntityContextRepository entityContextRepository,  final AOProxyFactory proxyFactory, final BeanResolver beanResolver, final OpportunityIndexRepository documentIndexRepository, final BasicRepository<? extends BasicEntity, Long> basicRepository) {
		this.entityContextRepository = entityContextRepository;
		this.proxyFactory = proxyFactory;
		this.beanResolver = beanResolver;
		this.documentIndexRepository = documentIndexRepository;
		this.basicRepository=basicRepository;
	}


	private OpportunityIndexRepository documentIndexRepository;
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.DocumentReplicationService#replicate()
	 */
	@Override
	public final void replicate() {
		
		
		final EntityContextAggregation aggregation = entityContextRepository.aggregation();
		
	
		if((aggregation.counter() >= minCount )) {
			
			doReplication();
		} 
		
		
		if( new Date().getTime() - aggregation.minDate().getTime() >= minAgeSec*1000 ){
			
			doReplication();
		} 
		
		
	}
	
	
	private void doReplication() {
		final Map<Long,EntityContext> entityContextsForReplication = new HashMap<Long,EntityContext>();
		final Set<EntityContext> skippedEntityContexts = new HashSet<EntityContext>();
		filter(entityContextsForReplication, skippedEntityContexts);
		enhance(entityContextsForReplication);
		final Collection<EntityContext> entityContexts = sortById(entityContextsForReplication.values());
	
		documentIndexRepository.updateDocuments(entityContexts);
		
	
		//entityContexts.addAll(new HashSet<>(skippedEntityContexts));
		
		saveOrDeleteEntityContext(entityContexts);
		saveOrDeleteEntityContext(sortById(skippedEntityContexts));
		
		replicate();
		
	}


	private void enhance(final Map<Long, EntityContext> entityContextsForReplication) {
		for(final EntityContext entityContext : entityContextsForReplication.values()){
			if( entityContext.isForDeleteRow()){
				final BasicEntity entity = EntityUtil.create(Resource.Opportunity.entityClass());
				EntityUtil.setId(entity, entityContext.reourceId());
				entityContext.assign(RevisionAware.class , proxyFactory.createProxy(OpportunityIndexCouchDBAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(entity).build()));
			} else {
				
				entityContext.assign(RevisionAware.class, proxyFactory.createProxy(OpportunityIndexCouchDBAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(basicRepository.forId(entityContext.reourceId())).build()));
			}
			
		}
		
	}


	private void saveOrDeleteEntityContext(final Collection<EntityContext> pocessed) {
		
		
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
	}


	private void filter(final Map<Long, EntityContext> entityContexts, final Set<EntityContext> inDenStaub) {
		final Paging paging = new SimplePagingImpl(100, "id");
		for(int page =0;  page< paging.maxPages() ; page++){
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
		    
		    if(distinctResources.size()>= maxProcess){
		    	return true;
		    }
		    
		    if(dupplicateIds.size() >= maxDelete){
		    	return true;
		    }
		}
		
		return false;
	}


  Collection<EntityContext>  sortById(Collection<EntityContext> entityContexts) {
		final List<EntityContext> toBeSorted = new ArrayList<>(entityContexts);
		
		Collections.sort(toBeSorted, new Comparator<EntityContext>(){

			@Override
			public int compare(final EntityContext o1, final EntityContext o2) {
				
				if(( o1.hasId()) && (o2.hasId())) {
						return (int) Math.signum(o1.id() - o2.id());
				} else {
					return  (int) Math.signum(o1.created().getTime() - o2.created().getTime()); 
				}
				
			}});
		
		return Collections.unmodifiableList(toBeSorted);
		
	}
	
	

}
