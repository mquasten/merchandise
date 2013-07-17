package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

class ClassificationTreeChangedObserveableControllerImpl {

	
	final void notifyClassificationChanged(final Collection<Classification> activityClassifications, final TreeNode treeNode) {
		processTree(treeNode, activityClassifications);
		System.out.println("change tree: " );
	} 
	
	
	final void notifyClassificationsChanged(final Collection<Classification> classifications, final TreeNode   root ) {
		addTree(root, classifications);
		
		System.out.println("create Activitytree: " );
	
	} 
	
	           
	
	
	private void  addTree(final TreeNode root, final Collection<? extends Classification> classifications) {
		
		final Map<Classification, Set<Classification>> domainTree = new HashMap<>();
		for(final Classification classification : classifications){
			
			if (( classification.parent() == null) && ( ! domainTree.containsKey(classification))){
				domainTree.put(classification, new HashSet<Classification>());
			}
			
			if( classification.parent() == null){
				continue;
			}
			
			if( ! domainTree.containsKey(classification.parent())){
				domainTree.put(classification.parent(), new HashSet<Classification>());
			}
			
			domainTree.get(classification.parent()).add(classification);
		}
		
		root.getChildren().clear();
		
		for(final Classification parent : sortedList(domainTree.keySet())){
			final TreeNode treeNode = new DefaultTreeNode(parent,  root);
			treeNode.setSelectable(false);
			addChilds(domainTree, parent, treeNode);
		}
			 
		
	}


	private void addChilds(final Map<Classification, Set<Classification>> domainTree, final Classification parent, final TreeNode treeNode) {
		for(final Classification child : sortedList(domainTree.get(parent))){
			new DefaultTreeNode(child, treeNode);
		}
	}


	private List<Classification> sortedList(Collection<Classification> classifications) {
		final List<Classification> results = new ArrayList<>(classifications);
		Collections.sort(results, new Comparator<Classification>() {

			@Override
			public int compare(final Classification c1, final Classification c2) {
				 return c1.id().compareToIgnoreCase(c2.id());
			} });
		
		return Collections.unmodifiableList(results);
	}
	
	
	
	
	
	

	private void processTree(final TreeNode node, final Collection<? extends Classification> activityClassifications) {
		for (final TreeNode tn : node.getChildren()) {

			tn.setSelected(nodeSElected(activityClassifications, tn));
			expandParentIfChildSelected(tn);

			if (tn.getChildCount() == 0) {
				continue;
			}
			processTree(tn, activityClassifications);
		}
	}

	private boolean nodeSElected(final Collection<? extends Classification> activityClassifications, final TreeNode tn) {
		boolean selected = false;
		if (activityClassifications.contains(tn.getData())) {
			selected = true;
		}
		return selected;
	}

	private void expandParentIfChildSelected(final TreeNode tn) {
		for (final TreeNode child : tn.getParent().getChildren()) {
			child.getParent().setExpanded(false);
			if (child.isSelected()) {
				child.getParent().setExpanded(true);
				break;
			}

		}
	}

}
