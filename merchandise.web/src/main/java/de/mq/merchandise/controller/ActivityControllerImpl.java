package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.stereotype.Component;

import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ActivityClassificationImpl;
import de.mq.merchandise.opportunity.support.Classification;

@Component("classifications")
public class ActivityControllerImpl {

	
	
	public final TreeNode activities() {
		
		
		ActivityClassification root = new ActivityClassificationImpl("headline", null);
		final TreeNode headline = new DefaultTreeNode( root, null);
		
		
		
		for(int i = 0 ; i < 100; i++){
			ActivityClassificationImpl activity = new ActivityClassificationImpl("Artist " +i , root);
			TreeNode treeNode = new DefaultTreeNode(activity, headline) ;
			treeNode.setSelectable(false);
			
			new DefaultTreeNode(new ActivityClassificationImpl("child 01 von "+i, activity), treeNode);
			new DefaultTreeNode(new ActivityClassificationImpl("child 02 von " +i, activity), treeNode);
		}
		return headline;
	}
	
}
