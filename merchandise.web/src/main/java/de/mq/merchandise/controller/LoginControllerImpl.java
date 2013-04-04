package de.mq.merchandise.controller;

import java.util.Collection;
import java.util.Map.Entry;

import org.springframework.dao.EmptyResultDataAccessException;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.Login;


public class LoginControllerImpl {
	
	
	
	private   CustomerService customerService;
	protected  LoginControllerImpl() {
	}
	
	public LoginControllerImpl(final CustomerService customerService){
		this.customerService=customerService;
	}
	
	@ExceptionTranslations(value={
            @ExceptionTranslation(  action = SimpleFacesExceptionTranslatorImpl.class, source = EmptyResultDataAccessException.class  , bundle="login_user_not_found" ),
            @ExceptionTranslation(  action = SimpleFacesExceptionTranslatorImpl.class, source = SecurityException.class  , bundle="login_invalid_password" )
	
	
	},  clazz = LoginControllerImpl.class)
	
	public void login(final Login login ) {
		System.out.println(login);
		final Collection<Entry<Customer,Person>> customerMap = customerService.login(login.getUser().toLowerCase());
		System.out.println(customerMap.size());
		if(!customerMap.iterator().next().getValue().digest().check(login.getPassword())) {
			throw new SecurityException("Wrong password, login  failed");
		}
		
		
		
	}
	

}
