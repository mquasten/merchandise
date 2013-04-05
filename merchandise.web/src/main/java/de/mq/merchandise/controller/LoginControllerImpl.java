package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.dao.EmptyResultDataAccessException;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.LoginAO;


public class LoginControllerImpl {
	
	
	
	private   CustomerService customerService;
	protected  LoginControllerImpl() {
	}
	
	public LoginControllerImpl(final CustomerService customerService){
		this.customerService=customerService;
	}
	
	@ExceptionTranslations(value={
            @ExceptionTranslation( action = SimpleFacesExceptionTranslatorImpl.class, source = EmptyResultDataAccessException.class  , bundle="login_user_not_found" ),
            @ExceptionTranslation( action = SimpleFacesExceptionTranslatorImpl.class, source = SecurityException.class  , bundle="login_invalid_password" )
	
	
	},  clazz = LoginControllerImpl.class)
	
	public String login(final LoginAO login ) {
		System.out.println(login);
		final Collection<Entry<Customer,Person>> customerEntries = customerService.login(login.getUser().toLowerCase());
		System.out.println(customerEntries.size());
		final Person person = customerEntries.iterator().next().getValue();
		if(!person.digest().check(login.getPassword())) {
			throw new SecurityException("Wrong password, login  failed");
		}
		
		final List<Customer> customers = new ArrayList<>();
		for(Entry<Customer,Person> entry : customerEntries){
			customers.add(entry.getKey());
		}
		
		login.setCustomers(customers);
		login.setPerson(person);
		
		if( customerEntries.size() > 1){
			return null;
		}
		
		return "overview" ;
		
	}
	

}
