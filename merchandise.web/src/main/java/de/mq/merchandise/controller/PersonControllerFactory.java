
package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.contact.GeocodingService;
import de.mq.merchandise.model.support.WebProxyFactory;

@Configuration
public class PersonControllerFactory {
	
	@Autowired
	private GeocodingService geocodingService;
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Bean()
	
    @Scope("singleton")
	public PersonControllerImpl  personController() {
		
		System.out.println(geocodingService);
		
		return webProxyFactory.webModell(PersonControllerImpl.class, new PersonControllerImpl(geocodingService));
		
	}

}
