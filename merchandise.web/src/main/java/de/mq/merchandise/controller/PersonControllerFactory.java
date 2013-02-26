
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
	
	protected PersonControllerFactory() {
		
	}
	PersonControllerFactory(GeocodingService geocodingService, WebProxyFactory webProxyFactory) {
		this.geocodingService = geocodingService;
		this.webProxyFactory = webProxyFactory;
	}

	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Bean()
    @Scope("singleton")
	public PersonControllerImpl  personController() {
		return webProxyFactory.webModell(PersonControllerImpl.class, new PersonControllerImpl(geocodingService));
		
	}

}
