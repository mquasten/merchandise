package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;

public class RuleInstanceTest {
	
	private static final long RULE_ID = 19680528L;
	private static final long CONDITION_ID = 4711L;
	private static final String PARAMETER_VALUE = "10";
	private static final String PARAMETER_NAME = "hotScore";
	private static final int PRIORITY = 4711;

	@Test
	public final void  create() {
		final Condition condition = Mockito.mock(Condition.class);
		final Rule rule= Mockito.mock(Rule.class);
		final RuleInstance ruleInstance = new RuleInstanceImpl(condition, rule, PRIORITY);
		Assert.assertEquals(rule, ruleInstance.rule());
		Assert.assertEquals(condition, ReflectionTestUtils.getField(ruleInstance, "condition"));
		Assert.assertEquals(PRIORITY, ruleInstance.priority());
	}
	
	@Test
	public final void  createDefault() {
		final RuleInstance ruleInstance = EntityUtil.create(RuleInstanceImpl.class);
		Assert.assertEquals(0, ruleInstance.priority());
		Assert.assertNull(ruleInstance.rule());
		Assert.assertNull(ReflectionTestUtils.getField(ruleInstance, "condition"));
	}
	
	@Test
	public final void assignPriority() {
		final RuleInstance ruleInstance = EntityUtil.create(RuleInstanceImpl.class);
		Assert.assertEquals(0, ruleInstance.priority());
		ruleInstance.assign(PRIORITY);
		Assert.assertEquals(PRIORITY, ruleInstance.priority());
	}
	

	@Test
	public final void assign() {
		final RuleInstance ruleInstance = EntityUtil.create(RuleInstanceImpl.class);
		ruleInstance.assign(PARAMETER_NAME, PARAMETER_VALUE);
		@SuppressWarnings("unchecked")
		final Map<String, String> parameters = (Map<String, String>) ReflectionTestUtils.getField(ruleInstance, "parameters");
		Assert.assertEquals(1, parameters.size());
		Assert.assertEquals(PARAMETER_NAME, parameters.keySet().iterator().next());
		Assert.assertEquals(PARAMETER_VALUE, parameters.values().iterator().next());
	}
	
	@Test
	public final void parameter() {
		final RuleInstance ruleInstance = EntityUtil.create(RuleInstanceImpl.class);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(PARAMETER_NAME, PARAMETER_VALUE);
		ReflectionTestUtils.setField(ruleInstance, "parameters", parameters);
		Assert.assertEquals(PARAMETER_VALUE, ruleInstance.parameter(PARAMETER_NAME));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void parameterNotFound() {
		final RuleInstance ruleInstance = EntityUtil.create(RuleInstanceImpl.class);
		ruleInstance.parameter(PARAMETER_NAME);
	}
	
	@Test
	public final void hash() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.id()).thenReturn(CONDITION_ID);
		final Rule rule= Mockito.mock(Rule.class);
		Mockito.when(rule.id()).thenReturn(RULE_ID);
		final RuleInstance ruleInstance = new RuleInstanceImpl(condition, rule, PRIORITY);
		
		Assert.assertEquals( condition.hashCode() + rule.hashCode() , ruleInstance.hashCode());
	}
	
	
	@Test
	public final void hashCommercialSubject() {
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		
		final Rule rule= Mockito.mock(Rule.class);
		
		final RuleInstance ruleInstance = new RuleInstanceImpl(commercialRelation, rule, PRIORITY);
		
		Assert.assertEquals( commercialRelation.hashCode() + rule.hashCode() , ruleInstance.hashCode());
	}
	
	@Test
	public final void equals() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.id()).thenReturn(CONDITION_ID);
		final Rule rule= Mockito.mock(Rule.class);
		Mockito.when(rule.id()).thenReturn(RULE_ID);
		final Rule otherRule= Mockito.mock(Rule.class);
		Mockito.when(otherRule.id()).thenReturn(2*RULE_ID);
		
		Assert.assertTrue(new RuleInstanceImpl(condition, rule, PRIORITY).equals(new RuleInstanceImpl(condition, rule, PRIORITY)));
		Assert.assertFalse(new RuleInstanceImpl(condition, rule, PRIORITY).equals(new RuleInstanceImpl(condition, otherRule, PRIORITY)));
	}
	
	@Test
	public final void equalsCommercialRelation() {
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
	
		final Rule rule= Mockito.mock(Rule.class);
		
		final Rule otherRule= Mockito.mock(Rule.class);
		Mockito.when(otherRule.id()).thenReturn(2*RULE_ID);
		
		Assert.assertTrue(new RuleInstanceImpl(commercialRelation, rule, PRIORITY).equals(new RuleInstanceImpl(commercialRelation, rule, PRIORITY)));
		Assert.assertFalse(new RuleInstanceImpl(commercialRelation, rule, PRIORITY).equals(new RuleInstanceImpl(commercialRelation, otherRule, PRIORITY)));
	}
	
	@Test
	public final void forRule() {
		final Condition condition = Mockito.mock(Condition.class);
		final Rule rule= Mockito.mock(Rule.class);
		final RuleInstance ruleInstance = new RuleInstanceImpl(condition, rule, PRIORITY);
		Assert.assertTrue(ruleInstance.forRule(rule));
		Assert.assertFalse(ruleInstance.forRule(Mockito.mock(Rule.class)));
	}
	
	@Test
	public final void id() {
		final RuleInstance ruleInstance =  EntityUtil.create(RuleInstanceImpl.class);
		final long id = randomId();
		EntityUtil.setId(ruleInstance, id);
		Assert.assertEquals(id, ruleInstance.id());
		
		
	}

	private long randomId() {
		return (long)  (1e12 * Math.random());
	}
	
	@Test
	public final void hasId() {
		final RuleInstance ruleInstance =  EntityUtil.create(RuleInstanceImpl.class);
		Assert.assertFalse(ruleInstance.hasId());
		EntityUtil.setId(ruleInstance, randomId());
		Assert.assertTrue(ruleInstance.hasId());
	}

}
