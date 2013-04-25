package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import de.mq.merchandise.util.StringCrypter;

public class AuthentificationServiceTest {
	
		private static final String CRYPTED_PASSWORD = "xxxxxxx";
		private static final String PASSWORD = "kinkyKylie";
		private static final long CUSTOMER_ID = 4711L;
		private static final long USER_ID = 19680528L;
		private final SecurityContextFactory securityContextFactory = Mockito.mock(SecurityContextFactory.class);
		private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		private final StringCrypter stringCrypter = Mockito.mock(StringCrypter.class);
		final AuthentificationService authentificationService = new AuthentificationServiceImpl(customerRepository, securityContextFactory, stringCrypter);
		@Before
		public final void setup() {
			 final String key  = String.format("%s:%s:%s" ,  USER_ID, CUSTOMER_ID, System.currentTimeMillis()/1000/60);
			 Mockito.when(securityContextFactory.securityContext()).thenReturn(securityContext);
			 Mockito.when(stringCrypter.encrypt(String.format(PASSWORD, USER_ID, CUSTOMER_ID), key)).thenReturn(CRYPTED_PASSWORD);
			 Assert.assertEquals(securityContext, securityContextFactory.securityContext());
		}
		 
		 
		 @Test
		 public final void createSecurityToken() {
			
			 authentificationService.createSecurityToken(USER_ID, CUSTOMER_ID, PASSWORD);
			 final ArgumentCaptor<Authentication> argumentCaptor = ArgumentCaptor.forClass(Authentication.class);
			 Mockito.verify(securityContext).setAuthentication(argumentCaptor.capture());
			 Assert.assertEquals(CRYPTED_PASSWORD, argumentCaptor.getValue().getCredentials());
			 Assert.assertEquals(String.format("%s:%s", USER_ID, CUSTOMER_ID), argumentCaptor.getValue().getPrincipal());
			 Assert.assertFalse(argumentCaptor.getValue().isAuthenticated());
			 
		 }
		 
		 @Test
		 public final void supports() {
			 Assert.assertTrue(authentificationService.supports(UsernamePasswordAuthenticationToken.class));
			 Assert.assertFalse(authentificationService.supports(JaasAuthenticationToken.class));
		 }
	

}
