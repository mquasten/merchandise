
package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.model.support.WebProxyFactory;

@Configuration
public class LoginControllerFactoryImpl {
	
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Autowired
	private CustomerService customerService;
	
	
	@Bean(name="loginController")
	@Scope("singleton")
	public LoginControllerImpl loginController() {
		  return   webProxyFactory.webModell(LoginControllerImpl.class, new LoginControllerImpl(customerService));
	}


}