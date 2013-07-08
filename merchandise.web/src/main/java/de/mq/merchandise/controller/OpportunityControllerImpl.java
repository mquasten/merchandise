package de.mq.merchandise.controller;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityAO;
import de.mq.merchandise.opportunity.support.OpportunityModelAO;
import de.mq.merchandise.opportunity.support.OpportunityService;

class OpportunityControllerImpl {
	
	
	private final OpportunityService opportunityService;
	
	
	OpportunityControllerImpl(final OpportunityService opportunityService){
		this.opportunityService=opportunityService;
	}
	
	
	void opportunities(final OpportunityModelAO opportunityModelAO, final Customer customer) {
		opportunityModelAO.setOpportunities(opportunityService.opportunities(customer, opportunityModelAO.getPattern() + "%" , opportunityModelAO.getPaging().getPaging()));
	    updateSelection(opportunityModelAO);
	}
	
	
	
	
	

	void updateSelection(final OpportunityModelAO opportunityModelAO) {
		if( opportunityModelAO.getSelected() == null){
			return;
		}
		for(final OpportunityAO opportunityAO : opportunityModelAO.getOpportunities()){
		    if(opportunityAO.getOpportunity().equals(opportunityModelAO.getSelected().getOpportunity())){
		    	return;
		    }
		} 
		
		
		opportunityModelAO.setSelected(null);
	}
	
	
	void save(final Opportunity opportunity){
		System.out.println(opportunity.activityClassifications().size());
	}

}
