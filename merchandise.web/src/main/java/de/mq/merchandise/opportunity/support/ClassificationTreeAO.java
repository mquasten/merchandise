package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import org.primefaces.model.TreeNode;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;

public abstract class ClassificationTreeAO   implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	@Getter( value = "treeNode")
	public abstract TreeNode getTreeNode();

	
	
	

}
