package de.mq.merchandise.rule.support;


import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.context.SecurityContext;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.controller.SimpleFacesExceptionTranslatorImpl;
import de.mq.merchandise.customer.Customer;


interface RuleController {
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = RuleModelAO.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)})}, clazz = RuleControllerImpl.class)
	void rules();
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz=RuleAO.class), @Parameter(clazz=RuleAO.class, el="#arg.idAsLong", elResultType=Long.class )})}, clazz = RuleControllerImpl.class, value={ @ExceptionTranslation(action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class, bundle="rule_not_found")})
	String initRule(); 
	
	

}
