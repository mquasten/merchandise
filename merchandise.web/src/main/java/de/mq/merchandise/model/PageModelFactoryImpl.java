package de.mq.merchandise.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;



@Configuration
public  class PageModelFactoryImpl {

	@Autowired
	private final AOProxyFactory proxyFactory;
	
	@Autowired
	private final BeanResolver beanResolver;
	
	PageModelFactoryImpl(final AOProxyFactory proxyFactory, final BeanResolver beanResolver) {
		this.proxyFactory = proxyFactory;
		this.beanResolver = beanResolver;
	}

	protected PageModelFactoryImpl() {
		this.proxyFactory = null;
		this.beanResolver = null;
	}
	
	
	
	@Bean(name="pageModel")
	@Scope("view")
	public PageModelAO pageModel() {
		return proxyFactory.createProxy(PageModelAO.class, new ModelRepositoryBuilderImpl().withMapEntry("selectMode", false).withBeanResolver(beanResolver).build());
	}
}