package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.SecurityContextFactory;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;

@Component("subjectController")
public class SubjectControllerImpl {
	
	private final CommercialSubjectService commercialSubjectService;
	
	private final SecurityContextFactory securityContextFactory;
	
	@Autowired
	public SubjectControllerImpl(final CommercialSubjectService commercialSubjectService, final SecurityContextFactory securityContextFactory){
		this.commercialSubjectService=commercialSubjectService;
		this.securityContextFactory=securityContextFactory;
	}
	
	public void subjects(final CommercialSubjectsModelAO commercialSubjectsModel) {
		final Customer customer = (Customer)securityContextFactory.securityContext().getAuthentication().getDetails();
		commercialSubjectsModel.setCommercialSubjects(commercialSubjectService.subjects(customer, commercialSubjectsModel.getPattern() + "%" , commercialSubjectsModel.getPaging().getPaging()));
	}

}
