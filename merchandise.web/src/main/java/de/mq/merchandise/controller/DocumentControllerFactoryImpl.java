package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.model.support.WebProxyFactory;

@Configuration
public class DocumentControllerFactoryImpl {
	
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	
	@Bean(name="documentController")
	@Scope("singleton")
	public DocumentController opportunityController() {
		return  webProxyFactory.webModell(DocumentController.class, new DocumentControllerImpl());
	   
	}
	
	

}
