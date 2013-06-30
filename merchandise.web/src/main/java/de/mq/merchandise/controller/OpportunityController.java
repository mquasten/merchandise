package de.mq.merchandise.controller;

import org.springframework.security.core.context.SecurityContext;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.customer.Customer;

import de.mq.merchandise.opportunity.support.OpportunityModelAO;

public interface OpportunityController {
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityModelAO.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)})}, clazz = OpportunityControllerImpl.class)
	void opportunities();

}
