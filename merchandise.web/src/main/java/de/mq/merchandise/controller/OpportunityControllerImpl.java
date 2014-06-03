package de.mq.merchandise.controller;

import java.util.Collection;

import de.mq.mapping.util.proxy.Conversation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.ClassificationService;
import de.mq.merchandise.opportunity.OpportunityService;
import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ActivityClassificationTreeAO;
import de.mq.merchandise.opportunity.support.CommercialRelation;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.opportunity.support.ConditionAO;
import de.mq.merchandise.opportunity.support.KeyWordModelAO;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityAO;
import de.mq.merchandise.opportunity.support.OpportunityModelAO;
import de.mq.merchandise.opportunity.support.ProductClassification;
import de.mq.merchandise.opportunity.support.ProductClassificationTreeAO;
import de.mq.merchandise.opportunity.support.RuleInstance;
import de.mq.merchandise.opportunity.support.RuleOperations;
import de.mq.merchandise.rule.support.RuleInstanceAO;
import de.mq.merchandise.util.EntityUtil;

class OpportunityControllerImpl {

	static final String OPPORTUNITY_PAGE = "opportunity.xhtml";

	private final OpportunityService opportunityService;
	
	private final ClassificationService classificationService;
	
	private final Conversation conversation;

	OpportunityControllerImpl(final OpportunityService opportunityService, final ClassificationService classificationService, final Conversation conversation) {
		this.opportunityService = opportunityService;
		this.classificationService = classificationService;
		this.conversation=conversation;
	}

	void opportunities(final OpportunityModelAO opportunityModelAO, final Customer customer) {
		opportunityModelAO.setOpportunities(opportunityService.opportunities(customer, opportunityModelAO.getPattern() + "%", opportunityModelAO.getPaging().getPaging()));
		updateSelection(opportunityModelAO);
	}

	void updateSelection(final OpportunityModelAO opportunityModelAO) {
		if (opportunityModelAO.getSelected() == null) {
			return;
		}
		for (final OpportunityAO opportunityAO : opportunityModelAO.getOpportunities()) {
			if (opportunityAO.getOpportunity().equals(opportunityModelAO.getSelected().getOpportunity())) {
				return;
			}
		}

		opportunityModelAO.setSelected(null);
	}
	
	
	String create(final ActivityClassificationTreeAO activityClassificationTreeAO, final ProductClassificationTreeAO productClassificationTreeAO) {
		System.out.println("create");
		activityClassificationTreeAO.setClassifications(classificationService.activityClassifications());
		activityClassificationTreeAO.notifyClassificationsChanged();
		
		productClassificationTreeAO.setClassifications(classificationService.productClassCollections());
		productClassificationTreeAO.notifyClassificationsChanged();
		
		
		return "opportunity";
	}

	
	String change(final OpportunityAO opportunityAO, final Long opportunityId) {
		if( opportunityId == null){
			return null;
		}
		
		opportunityAO.setOpportunity(opportunityService.read(opportunityId));
		opportunityAO.notifyConditionsChanged();
		opportunityAO.notifyActivityClassificationChanged();
		opportunityAO.notifyProductClassificationChanged();
		
		return OPPORTUNITY_PAGE;
	}
	
	void delete(final OpportunityModelAO opportunityModelAO){
		if( opportunityModelAO.getSelected()==null){
			return;
		}
		
		
		opportunityService.delete(opportunityModelAO.getSelected().getOpportunity());
		
		opportunityModelAO.setSelected(null);
		
	}
	

	String  save(final Opportunity opportunity, final Customer customer) {
		System.out.println("save opportunity with activities:" +opportunity.activityClassifications().size());
		System.out.println("save opportunity with product:" +opportunity.productClassifications().size());
		System.out.println("save opportunity with relation:" +opportunity.commercialRelations().size());
		EntityUtil.setDependency(opportunity, Customer.class, customer);
	//	removeNewCommercialRelationsFromConditionEspeciallyForHibernate(opportunity); 
		
		opportunityService.createOrUpdate(opportunity);
		conversation.end();
		return "opportunities.xhtml";
	}

	/*
	 * Ulgy nasty dirrrty, hibernate isn't able to handle (ignore!!!)  transient objects in reverse relations
	 */
	void removeNewCommercialRelationsFromConditionEspeciallyForHibernate(final Opportunity opportunity) {
		for(final CommercialRelation commercialRelation : opportunity.commercialRelations()) {
			if( commercialRelation.hasId() ) {
			
				continue;
			}
			for(final Condition condition : commercialRelation.conditions().values()) {
				EntityUtil.setDependency(condition, CommercialRelation.class, null);
			}
		}
	}

	void onActivityNodeSelect(final ActivityClassification activityClassification, final OpportunityAO opportunityAO) {
		final Opportunity opportunity = opportunityAO.getOpportunity();
		if (opportunity.activityClassifications().contains(activityClassification)) {
			opportunity.removeClassification(activityClassification);
		} else {
			opportunity.assignClassification(activityClassification);
		}

		opportunityAO.notifyActivityClassificationChanged();
	}
	
	void onProductNodeSelect(final ProductClassification productClassification, final OpportunityAO opportunityAO) {
		final Opportunity opportunity = opportunityAO.getOpportunity();
		if (opportunity.productClassifications().contains(productClassification)) {
			opportunity.removeClassification(productClassification);
		} else {
			opportunity.assignClassification(productClassification);
		}

		opportunityAO.notifyProductClassificationChanged();
	}
	
	void addKeyWord(final Opportunity opportunity, final KeyWordModelAO keyWordModel){
		opportunity.assignKeyWord(keyWordModel.getKeyWord());
		keyWordModel.setKeyWord(null);
	}
	
	void deleteKeyWord(final Opportunity opportunity, final KeyWordModelAO keyWordModel) {
		opportunity.removeKeyWord(keyWordModel.getSelectedKeyWord());
		keyWordModel.setSelectedKeyWord(null);
	}
	
	String addSubject(final OpportunityAO opportunityAO, final CommercialSubject commercialSubject) {
		final Opportunity opportunity = opportunityAO.getOpportunity();
		opportunity.assignConditions(commercialSubject);
		opportunityAO.notifyConditionsChanged();
		return OPPORTUNITY_PAGE;
	}
	
	
	void addConditionValue(final ConditionAO conditionAO)  {
		if(conditionAO.getValue() == null ){
			return;
		}
		if(conditionAO.getValue().trim().length()==0) {
			return ; 
		}
		conditionAO.getCondition().assignValue(conditionAO.getValue());
		conditionAO.setValue(null);
	}
	
	void deleteConditionValue(final ConditionAO conditionAO) {
		if( conditionAO.getSelectedValue() == null){
			return;
		}
		conditionAO.getCondition().removeValue(conditionAO.getSelectedValue());
		conditionAO.setSelectedValue(null);
	}
	
	void clearCondition(final ConditionAO conditionAO) {
	
		final CommercialRelation relation = conditionAO.getCommercialRelation();
		
		EntityUtil.clearFields(conditionAO.getCondition());
		//EntityUtil.setDependency(conditionAO.getCondition(), List.class, new ArrayList<>());
		conditionAO.setCommercialRelation(relation);
		
	}

	
	void addCondition(final OpportunityAO opportunityAO, final Condition condition) {
		
		final Condition newCondition = EntityUtil.copy(condition);
		
		System.out.println(newCondition.ruleInstances());
		opportunityAO.getOpportunity().assignConditions(condition.commercialRelation().commercialSubject(), newCondition);
		opportunityAO.notifyConditionsChanged();
	}

	
	
	
	
	void onConditionNodeSelect(final Object selected, final ConditionAO conditionAO, final RuleInstanceAO ruleInstanceAO) {
	
		if (selected instanceof CommercialRelation) {
	    	conditionAO.setCommercialRelation((CommercialRelation) selected);	
	    	
		}
		
		
		if( selected instanceof RuleOperations){
		     
			ruleInstanceAO.setParent((RuleOperations) selected);
			
			
		} 
	}
	
	
	
	void deleteCondition(final OpportunityAO opportunityAO, final Object data){
		if (data instanceof CommercialRelation) {
			opportunityAO.getOpportunity().remove(((CommercialRelation) data).commercialSubject());
			opportunityAO.notifyConditionsChanged();
			return;
		}
		if (data instanceof Condition) {
			opportunityAO.getOpportunity().remove(((Condition)data).commercialRelation().commercialSubject(), ((Condition)data).conditionType());
			opportunityAO.notifyConditionsChanged();
			return;
		}
	}
	
	
	String init(final Collection<CommercialRelation> relations) {
		for(final CommercialRelation relation: relations){
		
			init(relation);
		}
		return OPPORTUNITY_PAGE;
	}

	private void init(final CommercialRelation relation) {
		for(final RuleInstance ruleinInstance : relation.ruleInstances()) {
			ruleinInstance.parameterNames().size();
		}
	}

}
