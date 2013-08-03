package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

class ConditionsChangedObserverableControllerImpl {
	
	final void notifyConditionsChanged(final Collection<CommercialRelation> commercialRelations, final TreeNode node, final ConditionTreeAO conditionTreeAO) {
		/*
		 * reading martin fowler would be good for primefaces framework developers, 
		 * shit api (encapsulate collections and so on) !!!!
		 * modification shouldn't be done directly on a list, unmodifyable collections ...
		 */
	
		conditionTreeAO.setSelected(null);
		node.getChildren().clear();
		 for(final CommercialRelation commercialRelation : commercialRelations){
		
			 handleSubject(node, commercialRelation);
		 }
	}

	private void handleSubject(final TreeNode node, final CommercialRelation commercialRelation) {
		final TreeNode subjectNode = new DefaultTreeNode(commercialRelation, node);
		
		 for(final Condition condition : commercialRelation.conditions().values()){
			 handleType(subjectNode, condition);
		 }
	}
                      
	

	private void handleType(final TreeNode subjectNode, final Condition condition) {
		final TreeNode typeNode = new DefaultTreeNode(condition, subjectNode);
		
		 for(final String value : condition.values()){
			 new DefaultTreeNode(value, typeNode);
		 }
	}
	

}
