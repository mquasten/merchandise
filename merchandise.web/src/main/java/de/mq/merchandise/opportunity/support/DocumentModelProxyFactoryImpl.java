package de.mq.merchandise.opportunity.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.util.EntityUtil;

@Configuration
public class DocumentModelProxyFactoryImpl {
	
	@Autowired
	private AOProxyFactory proxyFactory;
	
	@Autowired
	private BeanResolver beanResolver;
	
	@Bean(name="documentModel")
	@Scope("conversation")
	public DocumentModelAO documentModel() {
		return proxyFactory.createProxy(DocumentModelAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("document", EntityUtil.create(OpportunityImpl.class)).withBeanResolver(beanResolver).build());
	}
	


}
