package de.mq.merchandise.controller;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

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
	
	
	void onActivityNodeSelect(final NodeSelectEvent e, final TreeNode treeNode) {
		processTree(treeNode, e.getTreeNode());
	}
	
	
	void processTree(final TreeNode node, final TreeNode  selected) {
		for(final TreeNode tn  : node.getChildren() ) {
			if (tn.equals(selected)) {
				final boolean newValue = !tn.isSelected();
				tn.setSelected(newValue);
				handleDomainModelUpdate(newValue);
				
				expandParentIfChildSelected(tn);
				
				return ;
			}
			processTree(tn, selected);
		}
	}


	private void handleDomainModelUpdate(final boolean newValue) {
		if( newValue){
			System.out.println("add activity to opportunity");
			return;
		}
		System.out.println("remove activity from opportunity");
	}


	private void expandParentIfChildSelected(final TreeNode tn) {
		for(final TreeNode child : tn.getParent().getChildren()){
			child.getParent().setExpanded(false);
			if(child.isSelected()){
				child.getParent().setExpanded(true);
				break;
			}
			
		}
	}
	
	

}
