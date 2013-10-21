package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestOperations;

public class DocumentIndexRestRepositoryImpl implements DocumentIndexRepository {

	static final String REV_ATTRIBUTE_NAME = "rev";
	static final String ID_ATTRIBUTE_NAME = "id";
	static final String VALUE_ATTRIBUTE_NAME = "value";
	static final String ROWS_ATTRIBUTE_NAME = "rows";
	static final String KEY_ATTRIBUTE_NAME = "keys";
	final RestOperations restOperations;
	
	@Autowired
	DocumentIndexRestRepositoryImpl(final RestOperations restOperations) {
		this.restOperations=restOperations;
	}
	
	static final String URL = "http://localhost:5984/{entity}/_all_docs";
	
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
			if( resource == null){
				resource=entityContext.resource();
			}
			
			sameResourceGuard(resource, entityContext);
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

	private void sameResourceGuard(final Resource resource, final EntityContext entityContext) {
		if( resource!=entityContext.resource()){
			throw new IllegalArgumentException("EntityContexts should have the same resource");
		}
	}

}
