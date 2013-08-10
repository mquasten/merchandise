package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;

public class ClassificationTreeMappingTest {
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	final TreeNode treeNode = new DefaultTreeNode();
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	@Test
	public final void model() {
		
		final ClassificationTreeAO web = proxyFactory.createProxy(ClassificationTreeAO.class, modelRepository(treeNode));
		
        Assert.assertEquals(treeNode, web.getTreeNode());
        Assert.assertNull(web.getClassifications());
        
        final Collection<Classification> classifications = new ArrayList<>();
        classifications.add(Mockito.mock(Classification.class));
        
        web.setClassifications(classifications);
        
        Assert.assertEquals(classifications, web.getClassifications());
	
	}


	private ModelRepository modelRepository(final TreeNode treeNode) {
		return new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withMapEntry("treeNode", treeNode).build();
	}
	
	@Test
	public final void activity() {
		final ActivityClassificationTreeAO web = proxyFactory.createProxy(ActivityClassificationTreeAO.class, modelRepository(treeNode));
		Assert.assertEquals(treeNode, web.getTreeNode());
	}
	
	@Test
	public final void product() {
		final ProductClassificationTreeAO web = proxyFactory.createProxy(ProductClassificationTreeAO.class, modelRepository(treeNode));
		Assert.assertEquals(treeNode, web.getTreeNode());
	}
}
