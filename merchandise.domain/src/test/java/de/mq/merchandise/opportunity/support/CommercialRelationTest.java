package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.rule.Rule;

public class CommercialRelationTest {
	
	private static final int PRIORITY = 4711;
	private static final long ID = 19680528L;
	private Opportunity opportunity = Mockito.mock(Opportunity.class);
	private CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	
	
	@Test
	public final void create() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		Assert.assertEquals(opportunity, commercialRelation.opportunity());
		Assert.assertEquals(commercialSubject, commercialRelation.commercialSubject());
	}
	
	@Test
	public final void id() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		ReflectionTestUtils.setField(commercialRelation, "id", ID);
		Assert.assertTrue(commercialRelation.hasId());
		Assert.assertEquals(ID, commercialRelation.id());
	}

	@Test(expected=IllegalStateException.class)
	public final void idNotExists() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		Assert.assertFalse(commercialRelation.hasId());
		commercialRelation.id();
	}
	
	@Test
	public final void conditions() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.PricePerUnit);
		commercialRelation.assign(condition);
		Assert.assertEquals(1, commercialRelation.conditions().size());
		Assert.assertEquals(ConditionType.PricePerUnit, commercialRelation.conditions().keySet().iterator().next());
		Assert.assertEquals(condition, commercialRelation.conditions().values().iterator().next());
		commercialRelation.condition(ConditionType.PricePerUnit);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void conditionNotFound() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		commercialRelation.condition(ConditionType.PricePerUnit);
		
	}
	
	@Test
	public final void hash() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		Assert.assertEquals(commercialSubject.hashCode() + opportunity.hashCode(), commercialRelation.hashCode());
	}
	
	@Test
	public final void equals() {
		Assert.assertTrue(new CommercialRelationImpl(commercialSubject, opportunity).equals(new CommercialRelationImpl(commercialSubject, opportunity)));
		Assert.assertFalse(new CommercialRelationImpl(commercialSubject, opportunity).equals(new CommercialRelationImpl(commercialSubject, Mockito.mock(Opportunity.class))));
	}
	
	@Test
	public final void  ruleInstances() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		final RuleInstance ruleInstance = addSingleRuleinstance(commercialRelation);
		
		Assert.assertEquals(1, commercialRelation.ruleInstances().size());
		Assert.assertEquals(ruleInstance, commercialRelation.ruleInstances().get(0));
		
	}

	private RuleInstance addSingleRuleinstance(final CommercialRelation commercialRelation) {
		final List<RuleInstance> ruleInstances = new ArrayList<>();
		Rule rule = Mockito.mock(Rule.class);
		final RuleInstance ruleInstance = new RuleInstanceImpl(commercialRelation, rule, PRIORITY);
		
		
		ruleInstances.add(ruleInstance);
		ReflectionTestUtils.setField(commercialRelation, "ruleInstances", ruleInstances);
		return ruleInstance;
	}
	
	
	@Test
	public final void assign() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		final Rule rule = Mockito.mock(Rule.class);
		commercialRelation.assign(rule, PRIORITY);
		@SuppressWarnings({ "unchecked" })
		final List<RuleInstance> ruleInstances = (List<RuleInstance>) ReflectionTestUtils.getField(commercialRelation, "ruleInstances");
		Assert.assertEquals(1, ruleInstances.size());
		Assert.assertEquals(rule, ruleInstances.get(0).rule());
		Assert.assertEquals(PRIORITY, ruleInstances.get(0).priority());
		Assert.assertEquals(commercialRelation, (((RuleInstanceImpl)ruleInstances.get(0)).commercialRelation()));
	}
	
	@Test
	public final void rule() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		final List<RuleInstance> ruleInstances = new ArrayList<>();
		final Rule rule = Mockito.mock(Rule.class);
		
		final RuleInstance ruleInstance = Mockito.mock(RuleInstance.class);
		//Mockito.when(ruleInstance.rule()).thenReturn(rule);
		Mockito.when(ruleInstance.forRule(rule)).thenReturn(true);
		ruleInstances.add(ruleInstance);
		
		final Rule otherRule = Mockito.mock(Rule.class);
		
		final RuleInstance otherRuleInstance = Mockito.mock(RuleInstance.class);
		
		Mockito.when(otherRuleInstance.forRule(otherRule)).thenReturn(true);
		ruleInstances.add(otherRuleInstance);
		
		
		ReflectionTestUtils.setField(commercialRelation, "ruleInstances", ruleInstances);
		Assert.assertEquals(ruleInstance, commercialRelation.ruleInstance(rule));
		Assert.assertEquals(otherRuleInstance, commercialRelation.ruleInstance(otherRule));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void ruleNotAssined() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		commercialRelation.ruleInstance(Mockito.mock(Rule.class));
	}
	
	@Test
	public final void remove() {
		final CommercialRelation commercialRelation = new CommercialRelationImpl(commercialSubject, opportunity);
		final RuleInstance ruleInstance = addSingleRuleinstance(commercialRelation);
		
		Assert.assertFalse(commercialRelation.ruleInstances().isEmpty());
		commercialRelation.remove(ruleInstance.rule());
		Assert.assertTrue(commercialRelation.ruleInstances().isEmpty());
	}
	
}
