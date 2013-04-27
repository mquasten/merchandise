package de.mq.merchandise.customer.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


public class SimpleAuthenticationEntryPointImpl implements AuthenticationEntryPoint {

	private final boolean showMessage;
	public SimpleAuthenticationEntryPointImpl(final boolean showMessage) {
		this.showMessage=showMessage;
	}
	
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException ex) throws IOException, ServletException {
		if( showMessage){
		  response.sendError(HttpStatus.FORBIDDEN.value() , ex.getMessage());
		  return;
		}
	    response.sendError(HttpStatus.FORBIDDEN.value());
	}



}
