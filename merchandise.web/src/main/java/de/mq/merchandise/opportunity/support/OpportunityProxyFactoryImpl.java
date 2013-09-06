package de.mq.merchandise.opportunity.support;

import org.primefaces.model.DefaultTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.Conversation;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.SimplePagingImpl;

@Configuration
public class OpportunityProxyFactoryImpl {

	final ClassificationTreeChangedObserveableControllerImpl classificationTreeChangedObserveableController = EntityUtil.create(ClassificationTreeChangedObserveableControllerImpl.class);
	
	final ConditionsChangedObserverableControllerImpl conditionsChangedObserverableController = EntityUtil.create(ConditionsChangedObserverableControllerImpl.class);
	
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
	@Scope("conversation")
	public OpportunityModelAO opportunityModel() {
		conversation.begin();
		return proxyFactory.createProxy(OpportunityModelAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("paging", proxyFactory.createProxy(PagingAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(new SimplePagingImpl(10, "name, id")).build())).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="opportunity")
	@Scope("conversation") 
	public OpportunityAO opportunity() {
		return proxyFactory.createProxy(OpportunityAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(EntityUtil.create(OpportunityImpl.class)).withDomain(classificationTreeChangedObserveableController).withDomain(conditionsChangedObserverableController).build());
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
	
	
	@Bean(name="conditions")
	@Scope("conversation")
	public ConditionTreeAO conditions() {
		conversation.begin();
		return proxyFactory.createProxy(ConditionTreeAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("treeNode", new DefaultTreeNode() ).withBeanResolver(beanResolver).withDomain(conditionsChangedObserverableController).build());
	}
	
	@Bean(name="condition")
	@Scope("view")
	public ConditionAO condition() {
		 return proxyFactory.createProxy(ConditionAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(EntityUtil.create(ConditionImpl.class)).build());
	}
	
	@Bean(name="documentModel")
	@Scope("conversation")
	public DocumentModelAO documentModel() {	
		return proxyFactory.createProxy(DocumentModelAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("document", EntityUtil.create(OpportunityImpl.class)).withBeanResolver(beanResolver).build());
	}
	
	
}
