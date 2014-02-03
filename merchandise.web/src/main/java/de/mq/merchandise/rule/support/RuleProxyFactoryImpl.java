package de.mq.merchandise.rule.support;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.controller.SerialisationControllerImpl;
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
	private final  RuleService ruleService ;
	
	
	private RuleControllerImpl ruleController;
	
	RuleProxyFactoryImpl() {
		this(null,null,null);
	}
	
	
	RuleProxyFactoryImpl(final RuleService ruleService, final AOProxyFactory proxyFactory, final BeanResolver beanResolver) {
		super();
		this.proxyFactory = proxyFactory;
		this.beanResolver = beanResolver;
		this.ruleService=ruleService;
		
	}


	@PostConstruct
	void init() {
		this.ruleController=new RuleControllerImpl(ruleService);
	}
	
	
	@Bean(name="rulesModel")
	@Scope("view")
	public RuleModelAO rulesModel() {
		return   proxyFactory.createProxy(RuleModelAO.class,  new ModelRepositoryBuilderImpl().withDomain(EntityUtil.create(SerialisationControllerImpl.class)).withDomain(ruleController).withMapEntry("selected", proxyFactory.createProxy(RuleAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(EntityUtil.create(RuleImpl.class)).build())).withMapEntry("paging", proxyFactory.createProxy(PagingAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(new SimplePagingImpl(10, "name, id")).build())).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="rule")
	@Scope("view")
	public  RuleAO rule() {
		return proxyFactory.createProxy(RuleAO.class, new ModelRepositoryBuilderImpl().withDomain(EntityUtil.create(RuleImpl.class)).withDomain(ruleController).withBeanResolver(beanResolver).build());
	}
	
	@Bean(name="ruleController")
	@Scope("singleton")
	public RuleController ruleController() {
		return  proxyFactory.createProxy(RuleController.class,  new ModelRepositoryBuilderImpl().withDomain(ruleController).withBeanResolver(beanResolver).build());
	}

}
