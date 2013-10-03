package de.mq.merchandise.opportunity.support;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import de.mq.merchandise.BasicEntity;

@Repository
@Profile("db")
public class DocumentRestRepositoryImpl implements DocumentRepository {

	static final String SUBJECTS_ENTITY = "subjects";
	static final String OPPORTUNITIES_ENTITY = "opportunities";
	static final String ID_PARAMETER = "id";
	static final String ENTITY_PARAMETER = "entity";
	static final String NAME_PARAMETER = "name";
	private static final String REVISION_KEY_JSON = "_rev";
	static final String REVISION_KEY = "rev";
	private final RestOperations restOperations;

	@Autowired
	public DocumentRestRepositoryImpl(final RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	static final String URL = "http://localhost:5984/{%s}/{%s}";

	static final String ATTACHEMENT_URL = String.format(URL + "/{%s}?rev={%s}", ENTITY_PARAMETER, ID_PARAMETER, NAME_PARAMETER, REVISION_KEY);

	static final String ENTITY_URL = String.format(URL, ENTITY_PARAMETER, ID_PARAMETER);

	@SuppressWarnings("unchecked")

	final String revisionFor(final BasicEntity basicEntity) {

		final Map<String, Object> params = new HashMap<>();
		params.put(ID_PARAMETER, basicEntity.id());
		params.put(ENTITY_PARAMETER, entity(basicEntity));

		try {

			revisionFromMap(restOperations.getForObject(ENTITY_URL, Map.class, params));
		} catch (final HttpClientErrorException ex) {
			throwExceptionIfNot404(ex);
			restOperations.put(ENTITY_URL, new HashMap<String, Object>(), params);
		}
		return revisionFromMap(restOperations.getForObject(ENTITY_URL, Map.class, params));
	}

	private String revisionFromMap(final Map<String, String> results) {
		revisionExistsGuard(results);
		return results.get(REVISION_KEY_JSON);
	}

	private void revisionExistsGuard(final Map<String, String> results) {
		if (!results.containsKey(REVISION_KEY_JSON)) {
			throw new IllegalArgumentException(REVISION_KEY_JSON + " not found in result");
		}
	}

	String entity(final BasicEntity basicEntity) {
		if (basicEntity instanceof Opportunity) {
			return OPPORTUNITIES_ENTITY;
		}

		if (basicEntity instanceof CommercialSubject) {
			return SUBJECTS_ENTITY;
		}
		throw new InvalidDataAccessApiUsageException("Not supported entity type for documents " + basicEntity.getClass());
	}

	private void throwExceptionIfNot404(final HttpClientErrorException ex) {
		if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
			throw new DataAccessResourceFailureException(ex.getStatusText(), ex);
		}
	}

	public final void assign(final BasicEntity entity, final String name, final InputStream inputStream, final MediaType mediaType) {
		try {
			restOperations.put(ATTACHEMENT_URL, new HttpEntity<InputStream>(inputStream, headerForMediaType(mediaType)), attachementParameters(entity, name));
		} catch (final HttpClientErrorException ex) {
			throwExceptionIfNotConflicted(ex);
			throw new OptimisticLockingFailureException(String.valueOf(entity.id()), ex);
		}

	}

	private HttpHeaders headerForMediaType(final MediaType mediaType) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(mediaType);
		return httpHeaders;
	}

	private Map<String, Object> attachementParameters(final BasicEntity entity, final String name) {
		final Map<String, Object> params = new HashMap<>();
		params.put(ID_PARAMETER, entity.id());
		params.put(ENTITY_PARAMETER, entity(entity));
		params.put(NAME_PARAMETER, name);
		params.put(REVISION_KEY, revisionFor(entity));
		return params;
	}

	private void throwExceptionIfNotConflicted(final HttpClientErrorException ex) {
		if (ex.getStatusCode() != HttpStatus.CONFLICT) {
			throw new DataAccessResourceFailureException(ex.getStatusText(), ex);
		}
	}

	public final void delete(final BasicEntity entity, final String name) {
		try {
			restOperations.delete(ATTACHEMENT_URL, attachementParameters(entity, name));
		} catch (final HttpClientErrorException ex) {
			throwExceptionIfNot404(ex);
		}
	}

}
