package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.GeocodingService;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.util.EntityUtil;

public class PersonControllerImpl {
	
	public PersonControllerImpl() {
		
	}
	
	private  GeocodingService geocodingService;
	
	
	PersonControllerImpl(final GeocodingService geocodingService) {
		this.geocodingService=geocodingService;
	}
	
	/*
	 * Dirrrty   viewScope must be used for cityAddress, requestScope will not be enough
	 * The address is empty after closing the dialog, why don't ask me, ask Ed ... 
	 */
	@MethodInvocation(clazz=PersonControllerImpl.class ,value={@ExceptionTranslation(action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class , bundle="geo_coding_error_deviation" ) ,
		@ExceptionTranslation(action = SimpleFacesExceptionTranslatorImpl.class, source = IllegalArgumentException.class , bundle="geo_coding_error_deviation" ) ,
		                                                            @ExceptionTranslation(action = SimpleFacesExceptionTranslatorImpl.class, source = IllegalStateException.class , bundle="geo_coding_error_status" ) , 
		                                                            @ExceptionTranslation(action = SimpleFacesExceptionTranslatorImpl.class, source = IncorrectResultSizeDataAccessException.class , bundle="geo_coding_error_multiple" ) 
	
	})
	public  void addAddress(final Person person, final CityAddress cityAddress) {	
		final CityAddress address = EntityUtil.copy(cityAddress);
		if (address instanceof Address) {
			((Address)address).assign(geocodingService.coordinates(cityAddress));
		}

		person.assign(address); 
		EntityUtil.setFieldsToNull(cityAddress);
	}
	
	
	
	
	/*
	 * 	 dispensable, waste , only used, because the shit el resolver can not handle overloaded methods,
	 *   if the monent will come, the operation can be done directly on the domain person object inside the el in person.xhtml
	 */
	public  void removeAddress(final Person person, final CityAddress cityAddress) {
		person.remove(cityAddress);
	}
	
	
	
	public  void addContact(final Person person, final LoginContact loginContact) {	
		person.assign(EntityUtil.copy(loginContact));
		EntityUtil.setFieldsToNull(loginContact);
		
	}
	
	public  void removeContact(final Person person, final LoginContact loginContact) {
		person.remove(loginContact);
	}
	
	// You have been a really bad el resolver, a very bad bad el resolver ... 
	public List<String> contacts(final Person person) {
		final List<String> results = new ArrayList<>();
		for(final Contact contact : person.contacts()){
			results.add(contact.contact());
		}
		return results;
		
	}

}
