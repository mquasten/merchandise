package de.mq.merchandise.opportunity.support;

import java.io.InputStream;

import org.springframework.http.MediaType;

import de.mq.merchandise.BasicEntity;

public interface DocumentRepository {

	/**
	 * Gets the current revision for the given entity. Couch db needs this for
	 * every operation as a query parameter. If the resource doesN#t exists , it will be created
	 * 
	 * @param basicEntity
	 *            the entity for which the revisions is needed
	 * @return the revision for the given entity
	 */
	String revisionFor(final BasicEntity basicEntity);

	/**
	 * Add image to the Resource that is defined by the given entity.
	 * @param entity the entity that defines the resource for example opportunities and subjects. The resource is identified by the id of the entity
	 * @param name then name of the attachment that should be added to the resource. 
	 * @param inputStream the stream with the attachment, the pdf document or the image
	 * @param mediaType the content-type of the attachment
	 */
	void assign(final BasicEntity entity, final String name, final InputStream inputStream, final MediaType mediaType);

	/**
	 * Deletes the attachment from the resource that is identified by the Entity 
	 * @param entity the resource from that the entity will be deleted
	 * @param name the name of the attachment that should be deleted
	 */
	void delete(final BasicEntity entity, final String name);

}