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
	
	enum State{
		New(false,false,false),
		Conflict(false,true,true),
		Forbidden(false,true,true),
		Unauthorized(false,true,true), 
		Unkown(false,true,true),
		Ok(true,true,false),
		Skipped(true,true,false);
		
		private final boolean finished;
		private final boolean assignable;
		private final boolean error;
		
		State(final boolean finished, final boolean assignable, final boolean error) {
			this.finished=finished;
			this.assignable=assignable;
			this.error=error;
		}
		
		boolean finised() {
			return finished;
		}
		
		boolean assignable() {
			return assignable;
		}
		
		boolean error() {
			return error;
		}
	}


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

	/**
	 * An EntityContext can have 0 ..n references, EntityClasses, Models
	 * @param clazz the Class or The Interface is the key
	 * @param reference the reference stored assigned to the key
	 */
	<T> void assign(Class<T> clazz, T reference);

	/**
	 * Get the reference assigned to the key, or throw an IllegalArgumentException, if the reference isn't assigned
	 * @param clazz the interface/class used as key
	 * @return the stored reference
	 */
	<T> T reference(Class<T> clazz);

	/**
	 * Checks if a reference is stored under the given key class
	 * @param clazz the interface/class used as key, for which should be checked if the reference is assigned
	 * @return true if the reference is aware else false
	 */
	boolean containsReference(Class<?> clazz);

	/**
	 * Change the State, if an Error occurs
	 * @param state the state of the Entity
	 */
	void assign(State state);
	
	


	boolean finished();
	
	
	boolean error();

}