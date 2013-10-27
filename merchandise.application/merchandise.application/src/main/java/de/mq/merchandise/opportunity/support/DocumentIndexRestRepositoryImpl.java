package de.mq.merchandise.opportunity.support;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestOperations;

import de.mq.merchandise.opportunity.support.EntityContext.State;

public class DocumentIndexRestRepositoryImpl implements DocumentIndexRepository {

	static final String DOCS_ATTRIBUTE = "docs";
	static final String REV_ATTRIBUTE_NAME = "rev";
	static final String ID_ATTRIBUTE_NAME = "id";
	static final String VALUE_ATTRIBUTE_NAME = "value";
	static final String ROWS_ATTRIBUTE_NAME = "rows";
	static final String KEY_ATTRIBUTE_NAME = "keys";
	static final String ERROR_ATTRIBUTE_NAME = "error";
	final RestOperations restOperations;
	
	private Map<String,State> states = new HashMap<>(); 
	
	@Autowired
	DocumentIndexRestRepositoryImpl(final RestOperations restOperations) {
		this.restOperations=restOperations;
		for(final State state : State.values() ){
			states.put(state.name().toLowerCase(), state);
		}
	}
	
	static final String URL = "http://localhost:5984/{entity}/_all_docs";
	
	static final String URL_UPDATE = "http://localhost:5984/{entity}/_bulk_docs";
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.DocumentIndexRepository#revisionsforIds(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final  Map<Long,String> revisionsforIds(final Collection<EntityContext> entityContexts){
		final Map<Long,String> results = new HashMap<>();
		if( entityContexts.isEmpty()){
			return results;
		}
		
		final Collection<String> keys = new HashSet<>();
		Resource resource=null;
		for(final EntityContext entityContext : entityContexts ){
			resource = assignResource(resource, entityContext);
			keys.add(String.valueOf(entityContext.reourceId()));
		}
		
		
		
		final Map<String,Object> idMap = new HashMap<>();
		idMap.put(KEY_ATTRIBUTE_NAME, keys);
		
		for(final Map<String, ? > row : (List<Map<String,?>>) ((Map<String, ?>) restOperations.postForObject(URL, idMap, Map.class, resource.urlPart())).get(ROWS_ATTRIBUTE_NAME) ){
		
			if(! row.containsKey(VALUE_ATTRIBUTE_NAME)){
				continue;
			}
			
			results.put(Long.valueOf((String) row.get(ID_ATTRIBUTE_NAME)) ,(String)  ((Map<String, ?>)row.get(VALUE_ATTRIBUTE_NAME)).get(REV_ATTRIBUTE_NAME));
			
		}
		return results;
		
	}

	private Resource assignResource(final Resource resource, final EntityContext entityContext) {
		if( resource == null){
			return entityContext.resource();
		}
		sameResourceGuard(resource, entityContext);
		return resource;
	}

	private void sameResourceGuard(final Resource resource, final EntityContext entityContext) {
		if( resource!=entityContext.resource()){
			throw new IllegalArgumentException("EntityContexts should have the same resource");
		}
	}
	
	
	@Override
	public final void updateDocuments(final Collection<EntityContext> entityContexts)  {
		final  Map<Long,String> revisions = revisionsforIds(entityContexts);
		
		if( entityContexts.isEmpty()){
			return;
		}
		
		final Map<Long, Object> aoMap = new HashMap<>();
		final Map<Long, EntityContext> entityContextMap = new HashMap<>();
		Resource resource=null;
		for(final EntityContext entityContext: entityContexts){
			resource = assignResource(resource, entityContext);
			
			final RevisionAware reference = entityContext.reference(RevisionAware.class);
			if( revisions.containsKey(entityContext.reourceId())){
				reference.setRevision(revisions.get(entityContext.reourceId()));
			}
			if( entityContext.isForDeleteRow()){
				reference.setDeleted(true);
			}
			
			if(entityContextMap.containsKey(entityContext.reourceId())){
				entityContextMap.get(entityContext.reourceId()).assign(State.Skipped);
			}
			
			entityContextMap.put(entityContext.reourceId(), entityContext);
			aoMap.put(entityContext.reourceId(), reference);
			
		}
		
		final Map<String,Collection<Object>>  root = new HashMap<>();
	
		/*  new ArrayList<>(...) to be able to test it:  needed because implementations from sun/oracle will be crap nearly every time, 
		 * like Britney S. will be crap every time, without Everytime, there is hope that she die in bath, or she will be reincadinated there like sun in oracle ?  */
		root.put(DOCS_ATTRIBUTE, new HashSet<>(aoMap.values()));
		
		
	    for(final Map<String,?> result : processPostRequest(resource, root)){
			idExistsInEntityContextsGuard(entityContextMap, Long.valueOf((String) result.get(ID_ATTRIBUTE_NAME)));
			entityContextMap.get(Long.valueOf((String) result.get(ID_ATTRIBUTE_NAME))).assign(state((String) result.get(ERROR_ATTRIBUTE_NAME))); 
		}
		
	}

	private void idExistsInEntityContextsGuard(final Map<Long, EntityContext> entityContextMap, final Long id) {
		if(!entityContextMap.containsKey(id)){
			throw new IllegalStateException("Id in Result didn't match to entityContexts");
		}
	}

	@SuppressWarnings("unchecked")
	private List<Map<String,?>> processPostRequest(Resource resource, final Map<String, Collection<Object>> root) {
		
		return restOperations.postForObject(URL_UPDATE, root ,List.class, resource.urlPart());
	}
	
	
	State state(final String error){
		if(error==null){
			return State.Ok;
		}
		final State result = states.get(error.trim().toLowerCase());
		if( result==null){
			return State.Unkown;
		}
		return result;
		
	}
	

}
