package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContext;

public class AuthentificationServiceTest {
	
		 private final SecurityContextFactory securityContextFactory = Mockito.mock(SecurityContextFactory.class);
		 private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		
		 @Before
		 public final void setup() {
			 Mockito.when(securityContextFactory.securityContext()).thenReturn(securityContext);
			 Assert.assertEquals(securityContext, securityContextFactory.securityContext());
		 }
	

}
