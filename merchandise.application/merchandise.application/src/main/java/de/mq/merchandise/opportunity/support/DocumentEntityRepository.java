package de.mq.merchandise.opportunity.support;


/**
 * Store and read Entities with attached Documents
 * @author mquasten
 *
 */
public interface DocumentEntityRepository {

	/**
	 * Get the entity by its id. 
	 * @param id the id persistent key
	 * @param clazz the class of the entity
	 * @return the entity identified by the given id and the given implementation class
	 */
	DocumentsAware forId(final Long id, final Class<? extends DocumentsAware> clazz);

	

	/**
	 * save merge the given entity.
	 * @param entity the entity that should be stored
	 */
	void save(final DocumentsAware entity);

}