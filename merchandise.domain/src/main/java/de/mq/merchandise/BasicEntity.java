package de.mq.merchandise;

import java.io.Serializable;


/**
 * Marks a class as an entity, a persistent object An Entity must have at least
 * a database identifier (in this project)
 * 
 * @author ManfredQuasten
 * 
 */
public interface BasicEntity extends Serializable{

	/**
	 * The identifier for the databse
	 * 
	 * @return the identifier, the unique key of the database
	 */
	long id();

	
	/**
	 * Returns true if the identifier exists, else false
	 * @return true if the entity is persistent, else false
	 */
	boolean hasId();
	
	

}