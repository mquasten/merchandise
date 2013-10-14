package de.mq.merchandise.opportunity.support;

import java.util.Date;

import de.mq.merchandise.BasicEntity;

/**
 * Stores changes insert/updates and deletes for an entity.
 * It is used to replicate this changes to an other database. for example a NoSqlDatabase
 * @author mquasten
 *
 */
public interface EntityContext extends BasicEntity{


	/**
	 * The identifier of the restResource. It is the id of the entity
	 * @return the identifier of the rest resource for the changed entity
	 */
	Long reourceId();

	/**
	 * The Resource. An Enum that describes the rest based resource
	 * @return the resource, the specific part of the rest url.
	 */
	Resource resource();

	/**
	 * Ist the row is deleted or not
	 * @return true if the row is deleted otherwise false
	 */
	boolean isForDeleteRow();

	/**
	 * Date of creation from the entry
	 * @return the creationDate of the entity
	 */
	Date created();

}