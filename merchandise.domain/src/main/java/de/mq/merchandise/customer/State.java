package de.mq.merchandise.customer;

import java.io.Serializable;

public interface State extends Serializable {

	/**
	 * Is the state is active  or not
	 * @return true if state is active otherwise false
	 */
	public abstract boolean isActive();

	/**
	 * Set the state to active.
	 * Method is idempotent
	 */
	public abstract void activate();

	/**
	 * Set the state to not active
	 *  Method is idempotent
	 */
	public abstract void deActivate();

}