package de.mq.merchandise.customer.support;

import org.springframework.security.authentication.AuthenticationProvider;

public interface AuthentificationService  extends AuthenticationProvider {

	void createSecurityToken(long userId, long customerId, String credentials);
}