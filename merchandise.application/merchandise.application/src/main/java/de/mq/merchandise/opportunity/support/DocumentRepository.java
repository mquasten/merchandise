package de.mq.merchandise.opportunity.support;

import de.mq.merchandise.BasicEntity;

public interface DocumentRepository {

	/**
	 * Gets the current revion for the given entity.
	 * Couch db needs this for every operation as an url parameter
	 * @param basicEntity the entity for which the revions is needed
	 * @return the revison for the given entity
	 */
	String revisionFor(final BasicEntity basicEntity);
	
	
      void assign(final BasicEntity entity, final String name, final  MediaTypeInputStream mediaTypeInputStream );

}