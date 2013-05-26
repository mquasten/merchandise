package de.mq.merchandise.opportunity.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.SimplePagingImpl;

@Configuration
public class CommercialSubjectProxyFactoryImpl {
	
	@Autowired
	private AOProxyFactory proxyFactory;
	@Autowired
	private final BeanResolver beanResolver;
	@Autowired
	CommercialSubjectProxyFactoryImpl(){
		this.proxyFactory=null;
		this.beanResolver=null;
	}
	
	CommercialSubjectProxyFactoryImpl(final AOProxyFactory proxyFactory, final BeanResolver beanResolver) {
		this.proxyFactory=proxyFactory;
		this.beanResolver=beanResolver;
	}
	
	
	
	@Bean(name="subjectsModel")
	@Scope("view")
	public CommercialSubjectsModelAO commercialSubjectsModel() {
		return proxyFactory.createProxy(CommercialSubjectsModelAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("paging", proxyFactory.createProxy(PagingAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(new SimplePagingImpl(10, "name, id")).build())).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="commercialSubject")
	@Scope("request") 
	public CommercialSubjectAO commercialSubject() {
		 return proxyFactory.createProxy(CommercialSubjectAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(EntityUtil.create(CommercialSubjectImpl.class)).build());
	} 
	
	

}
