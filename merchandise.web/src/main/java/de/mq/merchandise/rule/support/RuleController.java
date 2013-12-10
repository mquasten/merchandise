package de.mq.merchandise.rule.support;


import org.springframework.security.core.context.SecurityContext;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.customer.Customer;


interface RuleController {
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = RuleModelAO.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)})}, clazz = RuleControllerImpl.class)
	void rules();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz=RuleAO.class)})}, clazz = RuleControllerImpl.class)
	String save();

}
