package de.mq.merchandise.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ActivityClassificationImpl;

@Component("classifications")
@Scope("view")
public class ActivityControllerImpl implements Serializable {

	private static final long serialVersionUID = 1L;
	private TreeNode activities; 
	
	
	public TreeNode getActivities() {
	
		return activities;
	}


	@PostConstruct
	public final void init() {
		
		final ActivityClassification root = new ActivityClassificationImpl("headline", null);
		this.activities = new DefaultTreeNode( root, null);
		
		
		
		for(int i = 0 ; i < 100; i++){
			ActivityClassificationImpl activity = new ActivityClassificationImpl("Artist " +i , root);
			TreeNode treeNode = new DefaultTreeNode(activity, activities) ;
			treeNode.setSelectable(false);
			
			new DefaultTreeNode(new ActivityClassificationImpl("child 01 von "+i, activity), treeNode);
			new DefaultTreeNode(new ActivityClassificationImpl("child 02 von " +i, activity), treeNode);
		}
		
	}
	
	
	public void onNodeSelect(NodeSelectEvent e) {
		processTree(activities, e.getTreeNode());
	}


	private void processTree(final TreeNode node, final TreeNode  selected) {
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
