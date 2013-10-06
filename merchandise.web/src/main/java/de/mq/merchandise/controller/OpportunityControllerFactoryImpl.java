package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.Conversation;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.ClassificationService;
import de.mq.merchandise.opportunity.OpportunityService;

@Configuration
public class OpportunityControllerFactoryImpl {
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Autowired
	private OpportunityService opportunityService;
	
	@Autowired
	private ClassificationService classificationService;
	
	@Autowired
	private  Conversation conversation;
	
	@Bean(name="opportunityController")
	@Scope("singleton")
	public OpportunityController opportunityController() {
		return  webProxyFactory.webModell(OpportunityController.class, new OpportunityControllerImpl(opportunityService, classificationService, conversation));
	   
	}
	

}
