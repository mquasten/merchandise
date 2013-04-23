package de.mq.merchandise.customer.support;

import org.springframework.security.core.context.SecurityContext;

/**
 * Method injection for the static shit from securityContextHolder, to make it useable.
 * @author Manfred Quasten
 *
 */
public abstract class SecurityContextFactory {
	/**
	 * The current instance of securityContext from securityContextHolder
	 * @return the securityContext
	 */
	public abstract SecurityContext securityContext();

}
