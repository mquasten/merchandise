package de.mq.merchandise.rule.support;


import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

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
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz=RuleAO.class, el="#arg.rule.id()", elResultType=Long.class), @Parameter(clazz=RuleAO.class, el="#arg.state", elResultType=Boolean.class)})}, clazz = RuleControllerImpl.class)
	void changeState();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = RuleModelAO.class)})}, clazz = RuleControllerImpl.class)
	List<SelectItem> ruleItems() ;
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = ValueChangeEvent.class,originIndex=0)})}, clazz = RuleControllerImpl.class)
	List<SelectItem> change() ;

}
