package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.mapping.util.proxy.support.String2IntegerConverter;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.support.RuleImpl;
import de.mq.merchandise.util.EntityUtil;

public class RuleInstanceMappingTest {
	
	private static final int PRIORITY = 4711;

	private static final String PARAMETER_VALUE = "10";

	private static final String PARAMETER_NAME = "hotScore";

	private static final long RULE_ID = 19680528L;
	
	private static final long RULE_INSTANCE_ID = Math.round(1e12 *Math.random());

	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl();
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	@Test
	public final void toWeb() {
		Condition condition = Mockito.mock(Condition.class);
		final Rule rule = EntityUtil.create(RuleImpl.class);
		EntityUtil.setId(rule, RULE_ID);
		final RuleInstance ruleInstance = new RuleInstanceImpl(condition, rule, PRIORITY);
		ruleInstance.assign(PARAMETER_NAME, PARAMETER_VALUE);
		EntityUtil.setId(ruleInstance, RULE_INSTANCE_ID);
		
		final RuleInstanceAO web = proxyFactory.createProxy(RuleInstanceAO.class, new ModelRepositoryBuilderImpl().withDomain(ruleInstance).withBeanResolver(beanResolver).build());
		Assert.assertEquals(String.valueOf(RULE_ID), web.getRule().getId());
		Assert.assertEquals(1, web.getParameter().size());
		Assert.assertEquals(PARAMETER_VALUE, web.getParameter().values().iterator().next());
		Assert.assertEquals(PARAMETER_NAME, web.getParameter().keySet().iterator().next());
		Assert.assertEquals(String.valueOf(RULE_INSTANCE_ID), web.getId());
		Assert.assertEquals(String.valueOf(PRIORITY), web.getPriority());
		Assert.assertEquals(ruleInstance, web.getRuleInstance());
	}
	
	@Test
	public final void toDomain() {
		
	
		RuleInstanceImpl newDomain = EntityUtil.create(RuleInstanceImpl.class);
		final RuleInstance ruleInstance = newDomain;
		final RuleInstanceAO web = proxyFactory.createProxy(RuleInstanceAO.class,new ModelRepositoryBuilderImpl().withDomain(ruleInstance).withBeanResolver(beanResolver).build());
		
		web.setId(String.valueOf(RULE_INSTANCE_ID));
		web.setPriority(String.valueOf(PRIORITY));
		Assert.assertTrue(ruleInstance.hasId());
		Assert.assertEquals(RULE_INSTANCE_ID, ruleInstance.id());
		Assert.assertEquals(PRIORITY, ruleInstance.priority());
		Assert.assertEquals(ruleInstance, web.getRuleInstance());
		
		web.setRuleInstance(newDomain);
		Assert.assertEquals(newDomain, web.getRuleInstance());
		
	}

}
