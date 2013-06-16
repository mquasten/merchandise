package de.mq.merchandise.controller;

import org.springframework.security.core.context.SecurityContext;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectAO;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;

public interface SubjectController {
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = CommercialSubjectsModelAO.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)})}, clazz = SubjectControllerImpl.class)
	void subjects();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = CommercialSubjectAO.class, el="#arg.commercialSubject", elResultType=CommercialSubject.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)}),  @ActionEvent(name="subjects" , params={@Parameter(clazz = CommercialSubjectsModelAO.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)})}, clazz = SubjectControllerImpl.class)
	void save();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = CommercialSubjectsModelAO.class, el="#arg.selected", elResultType=CommercialSubjectAO.class)}), @ActionEvent(name="subjects" , params={@Parameter(clazz = CommercialSubjectsModelAO.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)})}, clazz = SubjectControllerImpl.class)
	void delete();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = CommercialSubjectsModelAO.class, el="#arg.selected", elResultType=CommercialSubjectAO.class), @Parameter(clazz=CommercialSubjectAO.class)})}, clazz = SubjectControllerImpl.class)
	void openChangeDialog();

}
