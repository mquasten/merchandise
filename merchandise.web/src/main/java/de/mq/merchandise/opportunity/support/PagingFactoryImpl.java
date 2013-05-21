package de.mq.merchandise.opportunity.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.SimplePagingImpl;

@Configuration
public class PagingFactoryImpl {
	
	@Autowired
	private final WebProxyFactory webProxyFactory;
	@Autowired
	private final BeanResolver beanResolver;
	@Autowired
	PagingFactoryImpl(){
		this.webProxyFactory=null;
		this.beanResolver=null;
	}
	
	PagingFactoryImpl(final WebProxyFactory webProxyFactory, final BeanResolver beanResolver) {
		this.webProxyFactory=webProxyFactory;
		this.beanResolver=beanResolver;
	}
	
	@Bean(name="paging")
	@Scope("view")
	
	public PagingAO paging() {
		final Paging paging =  new SimplePagingImpl(10, "id");
		return webProxyFactory.webModell(PagingAO.class, paging);
	}
	
	@Bean(name="subjectsModel")
	@Scope("view")
	public CommercialSubjectsModelAO commercialSubjectsModel() {
		final CommercialSubjectsModelAO model = webProxyFactory.webModell(CommercialSubjectsModelAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).build());
	  //  model.setSelected(webProxyFactory.webModell(CommercialSubjectAO.class, EntityUtil.create(CommercialSubjectImpl.class)));
		return model; 
	}
	
	@Bean(name="commercialSubject")
	@Scope("prototype") /* like a virgin ...*/ 
	public CommercialSubjectAO commercialSubject() {
		 return webProxyFactory.webModell(CommercialSubjectAO.class, EntityUtil.create(CommercialSubjectImpl.class));
	}
	
	

}
