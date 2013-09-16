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
	
	
	
	@Override
	public final String revisionFor(final BasicEntity basicEntity){
		final String url = String.format(URL, ENTITY_PARAMETER, ID_PARAMETER);
		final Map<String, Object> params = new HashMap<>();
		params.put(ID_PARAMETER, basicEntity.id());
		params.put(ENTITY_PARAMETER, entity(basicEntity));
		
		try {
		    restOperations.headForHeaders(url, params);
		} catch (final HttpClientErrorException ex){
			throwExceptionIfNot404(ex);
			restOperations.put(url, new HashMap<String,Object>()  , params);
		}
		@SuppressWarnings("unchecked")
		final Map<String,String> results = restOperations.getForObject(url,HashMap.class, params);
		
		return results.get(REVISION_KEY_JSON) ;
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
