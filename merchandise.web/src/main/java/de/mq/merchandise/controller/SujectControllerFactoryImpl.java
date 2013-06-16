package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.customer.support.SecurityContextFactory;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.CommercialSubjectService;

@Configuration
public class SujectControllerFactoryImpl {
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Autowired
	private CommercialSubjectService commercialSubjectService;
	
	@Autowired
	private SecurityContextFactory securityContextFactory;
	
	
	
	@Bean(name="subjectController")
	@Scope("singleton")
	public SubjectController subjectController() {
		return  webProxyFactory.webModell(SubjectController.class, new SubjectControllerImpl(commercialSubjectService, securityContextFactory));
	   
	}
	

}
