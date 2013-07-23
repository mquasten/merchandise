package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Collection;

import org.primefaces.model.TreeNode;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;

public abstract class ConditionTreeAO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter(value = "treeNode")
	public abstract TreeNode getTreeNode();

	@Setter(value = "commercialRelations")
	public abstract void setCommercicRealtions(final Collection<CommercialRelation> commercialRelations);

	@Getter(value = "commercialRelations")
	public abstract Collection<CommercialRelation> getCommercialRelations();

	@Getter(value = "selected")
	public abstract Object getSelected();
	
	@Setter(value = "selected")
	public abstract void setSelected(final Object item);

}
