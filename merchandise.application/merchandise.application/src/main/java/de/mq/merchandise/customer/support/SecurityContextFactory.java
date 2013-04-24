package de.mq.merchandise.customer.support;

import org.springframework.security.core.context.SecurityContext;

/**
 * Method injection for the static shit from securityContextHolder, to make it useable.
 * @author Manfred Quasten
 *
 */
public interface SecurityContextFactory {
	/**
	 * The current instance of securityContext from securityContextHolder
	 * @return the securityContext
	 */
	SecurityContext securityContext();

}
