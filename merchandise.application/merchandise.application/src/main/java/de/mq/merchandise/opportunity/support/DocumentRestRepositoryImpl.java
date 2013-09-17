package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import de.mq.merchandise.BasicEntity;

public class DocumentRestRepositoryImpl implements DocumentRepository {
	
	private static final String ID_PARAMETER = "id";
	private static final String ENTITY_PARAMETER = "entity";
	private static final String REVISION_KEY_JSON = "_rev";
	private final RestOperations restOperations;

	@Autowired
	public DocumentRestRepositoryImpl(final RestOperations restOperations) {
		this.restOperations = restOperations;
	}
	
	private final  String URL = "http://localhost:5984/{%s}/{%s}";
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public final String revisionFor(final BasicEntity basicEntity){
		final String url = String.format(URL, ENTITY_PARAMETER, ID_PARAMETER);
		final Map<String, Object> params = new HashMap<>();
		params.put(ID_PARAMETER, basicEntity.id());
		params.put(ENTITY_PARAMETER, entity(basicEntity));
		
		try {
			revisionFromMap(restOperations.getForObject(url,HashMap.class, params));
		} catch (final HttpClientErrorException ex){
			throwExceptionIfNot404(ex);
			restOperations.put(url, new HashMap<String,Object>()  , params);
		}
		return revisionFromMap( restOperations.getForObject(url,HashMap.class, params)) ;
	}


	private String revisionFromMap(final Map<String, String> results) {
		revisionExistsGuard(results);
		return results.get(REVISION_KEY_JSON);
	}


	private void revisionExistsGuard(final Map<String, String> results) {
		if( ! results.containsKey(REVISION_KEY_JSON)){
			throw new IllegalArgumentException(REVISION_KEY_JSON + " not found in result");
		}
	}


	String entity(final BasicEntity basicEntity) {
		if (basicEntity instanceof Opportunity) {
			return "opportunities";
		}
		
		if (basicEntity instanceof CommercialSubject) {
			return "subjects";
		}
		throw new IllegalArgumentException("Not supported entity type for documents " + basicEntity.getClass());
	}


	private void throwExceptionIfNot404(final HttpClientErrorException ex) {
		if( ex.getStatusCode() != HttpStatus.NOT_FOUND) {
			throw ex;
		}
	}

}
