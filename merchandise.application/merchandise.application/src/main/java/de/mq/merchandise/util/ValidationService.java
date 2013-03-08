package de.mq.merchandise.util;


/**
 * Do Beanvalidation manually. Executes a JSR 303 beanvalidation,
 * on the given object.
 * @author MQuasten
 *
 */
public interface ValidationService {

	/**
	 * Execute beanvalidation for the given object.
	 * @param entity the object that should be validated. It must be annotated with JSR 303 annotations
	 * @param groups the optional validation groups, for that validation should be executed.
	 */
	void validate(final Object entity, final Class<?>... groups);

}