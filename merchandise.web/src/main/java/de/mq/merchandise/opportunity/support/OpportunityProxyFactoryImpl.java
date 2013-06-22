package de.mq.merchandise.opportunity.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.SimplePagingImpl;

public class OpportunityProxyFactoryImpl {

	@Autowired
	private AOProxyFactory proxyFactory;
	@Autowired
	private final BeanResolver beanResolver;
	@Autowired
	OpportunityProxyFactoryImpl(){
		this.proxyFactory=null;
		this.beanResolver=null;
	}
	
	OpportunityProxyFactoryImpl(final AOProxyFactory proxyFactory, final BeanResolver beanResolver) {
		this.proxyFactory=proxyFactory;
		this.beanResolver=beanResolver;
	}
	
	
	
	@Bean(name="opportunityModel")
	@Scope("view")
	public OpportunityModelAO opportunityModel() {
		return proxyFactory.createProxy(OpportunityModelAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("paging", proxyFactory.createProxy(PagingAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(new SimplePagingImpl(10, "name, id")).build())).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="opportunity")
	@Scope("request") 
	public OpportunityAO opportunity() {
		 return proxyFactory.createProxy(OpportunityAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(EntityUtil.create(OpportunityImpl.class)).build());
	} 
	
	
	
	
}
