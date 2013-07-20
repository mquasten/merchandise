package de.mq.merchandise.opportunity.support;

import org.primefaces.model.DefaultTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.model.support.Conversation;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.SimplePagingImpl;

@Configuration
public class OpportunityProxyFactoryImpl {

	final ClassificationTreeChangedObserveableControllerImpl classificationTreeChangedObserveableController = EntityUtil.create(ClassificationTreeChangedObserveableControllerImpl.class);
	
	
	@Autowired
	private AOProxyFactory proxyFactory;
	@Autowired
	private final BeanResolver beanResolver;
	@Autowired
	private final Conversation conversation;
	
	
	OpportunityProxyFactoryImpl(){
		this.proxyFactory=null;
		this.beanResolver=null;
		this.conversation=null;
	}
	
	OpportunityProxyFactoryImpl(final AOProxyFactory proxyFactory, final BeanResolver beanResolver, final Conversation conversation) {
		this.proxyFactory=proxyFactory;
		this.beanResolver=beanResolver;
		this.conversation=conversation;
	}
	
	
	         
	@Bean(name="opportunitiesModel")
	@Scope("view")
	public OpportunityModelAO opportunityModel() {
		conversation.begin();
		return proxyFactory.createProxy(OpportunityModelAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("paging", proxyFactory.createProxy(PagingAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(new SimplePagingImpl(10, "name, id")).build())).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="opportunity")
	@Scope("conversation") 
	public OpportunityAO opportunity() {
		final OpportunityImpl opportunity = EntityUtil.create(OpportunityImpl.class);
		opportunity.assignKeyWord("Escort Service");
		opportunity.assignKeyWord("Begleitservice");
		return proxyFactory.createProxy(OpportunityAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(opportunity).withDomain(classificationTreeChangedObserveableController).build());
	} 
	
	
	@Bean(name="activityClassifications")
	@Scope("conversation")
	public ActivityClassificationTreeAO activityClassifications() {
		conversation.begin();
		return proxyFactory.createProxy(ActivityClassificationTreeAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("treeNode", new DefaultTreeNode() ).withBeanResolver(beanResolver).withDomain(classificationTreeChangedObserveableController).build());
	}
	
	@Bean(name="productClassifications")
	@Scope("conversation")
	public ProductClassificationTreeAO productClassifications() {
		conversation.begin();
		return proxyFactory.createProxy(ProductClassificationTreeAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("treeNode", new DefaultTreeNode() ).withBeanResolver(beanResolver).withDomain(classificationTreeChangedObserveableController).build());
	}
	
	@Bean(name="keyWordModel")
	@Scope("view")
	public KeyWordModelAO keyWordModel() {
		return proxyFactory.createProxy(KeyWordModelAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).build());
	}
	
	
}
