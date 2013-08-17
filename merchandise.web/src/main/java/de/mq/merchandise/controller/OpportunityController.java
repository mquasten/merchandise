package de.mq.merchandise.controller;



import org.primefaces.event.NodeSelectEvent;
import org.springframework.security.core.context.SecurityContext;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ActivityClassificationTreeAO;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;
import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.opportunity.support.ConditionAO;
import de.mq.merchandise.opportunity.support.ConditionTreeAO;
import de.mq.merchandise.opportunity.support.KeyWordModelAO;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityAO;
import de.mq.merchandise.opportunity.support.OpportunityModelAO;
import de.mq.merchandise.opportunity.support.ProductClassification;
import de.mq.merchandise.opportunity.support.ProductClassificationTreeAO;

public interface OpportunityController {
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityModelAO.class), @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)})}, clazz = OpportunityControllerImpl.class)
	void opportunities();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityAO.class, el="#arg.opportunity", elResultType=Opportunity.class)})}, clazz = OpportunityControllerImpl.class)
	String save();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = NodeSelectEvent.class,originIndex=0,elResultType=ActivityClassification.class, el="#arg.treeNode.data"), @Parameter(clazz = OpportunityAO.class)})}, clazz = OpportunityControllerImpl.class)
	void onActivityNodeSelect(final NodeSelectEvent nodeSelectEvent);
	
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = NodeSelectEvent.class,originIndex=0,elResultType=ProductClassification.class, el="#arg.treeNode.data"), @Parameter(clazz = OpportunityAO.class)})}, clazz = OpportunityControllerImpl.class)
	void onProductNodeSelect(final NodeSelectEvent nodeSelectEvent);
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = ActivityClassificationTreeAO.class), @Parameter(clazz = ProductClassificationTreeAO.class)})}, clazz = OpportunityControllerImpl.class)
	String create();


	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityAO.class, el="#arg.opportunity", elResultType=Opportunity.class), @Parameter(clazz=KeyWordModelAO.class)})}, clazz = OpportunityControllerImpl.class)
	void addKeyWord();
	
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityAO.class, el="#arg.opportunity", elResultType=Opportunity.class), @Parameter(clazz=KeyWordModelAO.class)})}, clazz = OpportunityControllerImpl.class)
	void deleteKeyWord();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityAO.class), @Parameter(clazz = CommercialSubjectsModelAO.class, el="#arg.selected.commercialSubject" , elResultType=CommercialSubject.class )})}, clazz = OpportunityControllerImpl.class)	
	String addSubject();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = ConditionAO.class)})}, clazz = OpportunityControllerImpl.class)	
	void addConditionValue();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = ConditionAO.class)})}, clazz = OpportunityControllerImpl.class)	
	void deleteConditionValue();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = ConditionAO.class ) })}, clazz = OpportunityControllerImpl.class)	
	void clearCondition();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityAO.class ), @Parameter(clazz = ConditionAO.class, elResultType=Condition.class, el="#arg.condition" ) })}, clazz = OpportunityControllerImpl.class)	
	void addCondition();
	
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = NodeSelectEvent.class,originIndex=0,elResultType=Object.class, el="#arg.treeNode.data"), @Parameter(clazz = ConditionAO.class)})}, clazz = OpportunityControllerImpl.class)
	void onConditionNodeSelect(final NodeSelectEvent nodeSelectEvent);
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = OpportunityAO.class), @Parameter(clazz = ConditionTreeAO.class, elResultType=Object.class, el="#arg.selected.data" , skipNotReachableOnNullElException=true) })}, clazz = OpportunityControllerImpl.class)	
	void deleteCondition();

	
}
