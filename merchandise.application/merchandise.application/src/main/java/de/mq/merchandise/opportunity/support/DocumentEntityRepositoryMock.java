package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
@Profile("mock")
public class DocumentEntityRepositoryMock  implements DocumentEntityRepository{
	
	
	private Map<UUID,DocumentsAware>  storedDocuments = new HashMap<>(); 

	@Override
	public DocumentsAware forId(final Long id, final Class<? extends DocumentsAware> clazz) {
		return  storedDocuments.get(entityExistsGuard(id, uuid(id, clazz)));
	}

	private UUID uuid(final Long id, final Class<? extends DocumentsAware> clazz) {
		return new UUID(id, clazz.hashCode());
	}

	private UUID entityExistsGuard(final Long id, final UUID uuid) {
		if ( ! storedDocuments.containsKey(uuid) ) {
			throw new InvalidDataAccessApiUsageException("Entity not found: " + id);
		}
		return uuid;
	}

	@Override
	public void save(final DocumentsAware entity) {
		
		storedDocuments.put(uuid(entity.id(), entity.getClass()), entity);
	}

}
