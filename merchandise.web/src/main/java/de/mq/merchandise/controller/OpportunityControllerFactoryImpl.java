package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.support.OpportunityService;

@Configuration
public class OpportunityControllerFactoryImpl {
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Autowired
	private OpportunityService opportunityService;
	
	
	
	
	@Bean(name="opportunityController")
	@Scope("singleton")
	public OpportunityController subjectController() {
		return  webProxyFactory.webModell(OpportunityController.class, new OpportunityControllerImpl(opportunityService));
	   
	}
	

}
