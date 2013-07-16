package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Collection;

import org.primefaces.model.TreeNode;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.mapping.util.proxy.Setter;

public abstract class ClassificationTreeAO  implements Serializable{
	

	private static final long serialVersionUID = 1L;

	@Getter(value = "treeNode")
	public abstract TreeNode getTreeNode();

	@Setter(value = "classifications")
	public abstract void setClassifications(Collection<? extends Classification> classifications);

	@Getter(value = "classifications")
	public abstract Collection<Classification> getClassifications();

	@MethodInvocation(actions = { @ActionEvent(params = { @Parameter(clazz = Collection.class, property = "classifications"), @Parameter(clazz = TreeNode.class, property = "treeNode") }) }, clazz = ClassificationTreeChangedObserveableControllerImpl.class)
	public abstract void notifyClassificationsChanged();

}