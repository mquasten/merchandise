package de.mq.merchandise.opportunity.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.controller.DocumentControllerImpl;
import de.mq.merchandise.opportunity.ResourceOperations;
import de.mq.merchandise.util.EntityUtil;

@Configuration
public class DocumentModelProxyFactoryImpl {
	
	@Autowired
	private AOProxyFactory proxyFactory;
	
	@Autowired
	private BeanResolver beanResolver;
	
	@Autowired
	private  ResourceOperations resourceOperations; 
	
	@Autowired
	private  DocumentService documentService;
	
	
	
	
	@Bean(name="documentModel")
	@Scope("view")
	public DocumentModelAO documentModel() {
		return proxyFactory.createProxy(DocumentModelAO.class,  new ModelRepositoryBuilderImpl().withDomain(new DocumentControllerImpl(documentService,resourceOperations)).withMapEntry("document", EntityUtil.create(OpportunityImpl.class)).withBeanResolver(beanResolver).build());
	}
	


}
