package de.mq.merchandise.customer;


/**
 * Create a State-Implementation
 * @author Admin
 *
 */
public interface StateBuilder {

	/**
	 * The activationLevel
	 * @param active true activated else false
	 * @return the builder itselves
	 */
	StateBuilder forState(boolean active);

	/**
	 * Return the created state
	 * @return the state
	 */
	State build();
	
}