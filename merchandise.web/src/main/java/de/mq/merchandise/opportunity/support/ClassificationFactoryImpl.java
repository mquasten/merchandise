package de.mq.merchandise.opportunity.support;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;

@Configuration
public class ClassificationFactoryImpl {

	@Autowired
	private AOProxyFactory proxyFactory;
	@Autowired
	private final BeanResolver beanResolver;
	@Autowired
	ClassificationFactoryImpl(){
		this.proxyFactory=null;
		this.beanResolver=null;
	}
	
	ClassificationFactoryImpl(final AOProxyFactory proxyFactory, final BeanResolver beanResolver) {
		this.proxyFactory=proxyFactory;
		this.beanResolver=beanResolver;
	}
	
	
	         
	@Bean(name="activityClassifications")
	@Scope("view")
	public ClassificationTreeAO opportunityModel() {
		return proxyFactory.createProxy(ClassificationTreeAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("treeNode",activities()).withBeanResolver(beanResolver).build());
	}

	private TreeNode activities() {
		final ActivityClassification root = new ActivityClassificationImpl("headline", null);
		final TreeNode activities = new DefaultTreeNode( root, null);
		
		
		
		for(int i = 0 ; i < 100; i++){
			ActivityClassificationImpl activity = new ActivityClassificationImpl("Artist " +i , root);
			TreeNode treeNode = new DefaultTreeNode(activity, activities) ;
			treeNode.setSelectable(false);
			
			new DefaultTreeNode(new ActivityClassificationImpl("child 01 von "+i, activity), treeNode);
			new DefaultTreeNode(new ActivityClassificationImpl("child 02 von " +i, activity), treeNode);
		}
		return activities;
	}
	
	

}
