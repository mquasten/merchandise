package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.TreeNode;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;

public class ConditionTreeMappingTest {
	
	

	private static final String SELECTED = "selected";

	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 

	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private final TreeNode treeNode = Mockito.mock(TreeNode.class);
	
	
	@Test
	public final void model() {
		final ConditionTreeAO conditionTreeAO = proxyFactory.createProxy(ConditionTreeAO.class, new ModelRepositoryBuilderImpl().withMapEntry("treeNode", treeNode).withBeanResolver(beanResolver).build());
		
		Assert.assertEquals(treeNode, conditionTreeAO.getTreeNode());
		
		Assert.assertNull(conditionTreeAO.getCommercialRelations());
		Assert.assertNull(conditionTreeAO.getSelected());
		
		final Collection<CommercialRelation> commercialRelations = new ArrayList<>();
		commercialRelations.add(Mockito.mock(CommercialRelation.class));
		
		conditionTreeAO.setSelected(SELECTED);
		conditionTreeAO.setCommercicRealtions(commercialRelations);
		
		Assert.assertEquals(SELECTED, conditionTreeAO.getSelected());
		Assert.assertEquals(commercialRelations, conditionTreeAO.getCommercialRelations());
	}
	
	

}
