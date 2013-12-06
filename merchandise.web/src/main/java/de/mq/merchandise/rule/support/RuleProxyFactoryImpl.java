package de.mq.merchandise.rule.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.rule.RuleService;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.SimplePagingImpl;

@Configuration
public class RuleProxyFactoryImpl {
	
	@Autowired
	private final AOProxyFactory proxyFactory;
	
	@Autowired
	private  final BeanResolver beanResolver;
	
	@Autowired
	private final RuleService ruleService;
	
	public RuleProxyFactoryImpl() {
		this(null, null,null);
	}
	
	RuleProxyFactoryImpl(final RuleService ruleService, final AOProxyFactory proxyFactory, final BeanResolver beanResolver) {
		super();
		this.ruleService=ruleService;
		this.proxyFactory = proxyFactory;
		this.beanResolver = beanResolver;
	}
	
	
	@Bean(name="rulesModel")
	@Scope("view")
	public RuleModelAO opportunityModel() {
		return proxyFactory.createProxy(RuleModelAO.class,  new ModelRepositoryBuilderImpl().withMapEntry("paging", proxyFactory.createProxy(PagingAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(new SimplePagingImpl(10, "name, id")).build())).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="rule")
	@Scope("view")
	public  RuleAO rule() {
		return proxyFactory.createProxy(RuleAO.class, new ModelRepositoryBuilderImpl().withDomain(EntityUtil.create(RuleImpl.class)).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="ruleController")
	@Scope("singleton")
	public RuleController opportunityController() {
		return  proxyFactory.createProxy(RuleController.class,  new ModelRepositoryBuilderImpl().withDomain(new RuleControllerImpl(ruleService)).withBeanResolver(beanResolver).build());
	   
	}

}
