package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.ResourceOperations;
import de.mq.merchandise.opportunity.support.DocumentService;

@Configuration
public class DocumentControllerFactoryImpl {
	
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Autowired
	private FacesContextFactory facesContextFactory;
	
	@Autowired
	private ResourceOperations resourceOperations;
	
	@Autowired
	private DocumentService documentService;
	
	
	@Bean(name="documentController")
	@Scope("singleton")
	public DocumentController documentController() {
		return  webProxyFactory.webModell(DocumentController.class, new DocumentControllerImpl(facesContextFactory,documentService, resourceOperations));
	}
	
	

}
