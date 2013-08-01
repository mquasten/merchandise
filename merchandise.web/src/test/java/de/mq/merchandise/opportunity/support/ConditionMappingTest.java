package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.util.EntityUtil;

public class ConditionMappingTest {

	private static final String PRICE = "47.11 EUR";

	private static final String CALCULATION = "calculation";

	private static final String VALIDATION = "validation";

	private static final long CONDITION_ID = 19680528L;

	private final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();

	private final AOProxyFactory proxyFactory = new BeanConventionCGLIBProxyFactory();

	@Test
	public final void toWeb() {
		final List<String> values = new ArrayList<>();
		values.add(PRICE);
		final Condition condition = new ConditionImpl(ConditionType.PricePerUnit, values, VALIDATION, CALCULATION);
		EntityUtil.setId(condition, CONDITION_ID);

		final ConditionAO web = proxyFactory.createProxy(ConditionAO.class, new ModelRepositoryBuilderImpl().withDomain(condition).withBeanResolver(beanResolver).build());

		Assert.assertEquals(""+ CONDITION_ID, web.getId());
		Assert.assertEquals(ConditionType.PricePerUnit.name(), web.getConditionType());
		Assert.assertEquals(VALIDATION, web.getValidation());
		Assert.assertEquals(CALCULATION, web.getCalculation());
	}
	
	@Test
	public final void toDomain() {
		final Condition condition = EntityUtil.create(ConditionImpl.class);
		final ConditionAO web = proxyFactory.createProxy(ConditionAO.class, new ModelRepositoryBuilderImpl().withDomain(condition).withBeanResolver(beanResolver).build());
		
		Assert.assertFalse(condition.hasId());
		Assert.assertNull(condition.conditionType());
		Assert.assertNull(condition.validation());
		Assert.assertNull(condition.calculation());
		
		web.setId(""+CONDITION_ID);
		web.setConditionType(ConditionType.Quantity.name());
		web.setValidation(VALIDATION);
		web.setCalculation(CALCULATION);
		
		Assert.assertTrue(condition.hasId());
		Assert.assertEquals(CONDITION_ID, condition.id());
		Assert.assertEquals(ConditionType.Quantity, condition.conditionType());
		Assert.assertEquals(VALIDATION, condition.validation());
		Assert.assertEquals(CALCULATION, condition.calculation());
	}
	
	

}
