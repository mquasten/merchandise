package de.mq.merchandise.customer.support;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class AuthenticationEntryPointTest {
	
	private static final String ERRORMESSAGE = "Errormessage";

	
	private final MockHttpServletResponse response = new MockHttpServletResponse();
	private final AuthenticationException  authenticationException = Mockito.mock(AuthenticationException.class);
	
	@Before
	public final void setup() {
		Mockito.when(authenticationException.getMessage()).thenReturn(ERRORMESSAGE);
	}
	
	
	@Test
	public final void commence() throws IOException, ServletException {
		final AuthenticationEntryPoint  authenticationEntryPoint = new SimpleAuthenticationEntryPointImpl(true);
		
		authenticationEntryPoint.commence(new MockHttpServletRequest(), response, authenticationException);
		Assert.assertEquals(ERRORMESSAGE, response.getErrorMessage());
		Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
	}
	
	@Test
	public final void commenceWithOutMessage() throws IOException, ServletException {
		final AuthenticationEntryPoint  authenticationEntryPoint = new SimpleAuthenticationEntryPointImpl(false);
		authenticationEntryPoint.commence(new MockHttpServletRequest(), response, authenticationException);
		Assert.assertNull(response.getErrorMessage());
		Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
	}

}
