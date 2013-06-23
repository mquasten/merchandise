package de.mq.merchandise.util;

/**
 * A QueryParameter for JPA, described by its name and its values
 * @author mquasten
 *
 * @param <T> the type of the parameter value
 */
public interface Parameter<T> {
	
	/**
	 * The name of the parameter
	 * @return its name
	 */
	String name() ;
	
	/**
	 * The value of the parameter
	 * @return its value
	 */
	T value();

}
