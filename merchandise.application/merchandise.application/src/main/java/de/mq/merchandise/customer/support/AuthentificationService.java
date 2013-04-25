package de.mq.merchandise.customer.support;

import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Specific AuthentificationProvider. Puts user password into security context.
 * Use this token for  authentification, get roles from database
 * @author Admin
 *
 */
public interface AuthentificationService  extends AuthenticationProvider {

	/**
	 * Put a security token into the security context, that can be used to authentificate a person customer combination.
	 * @param userId the id of the person
	 * @param customerId the id from the (current) customer
	 * @param credentials an encrypted password for the customer,
	 */
	void createSecurityToken(long userId, long customerId, String credentials);
}