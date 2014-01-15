package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;

public class ConditionTest {
	
	private static final String VALUE = "value";
	private static final long ID = 19680528L;
	private static final String CALCULATION = "calculation";
	private static final String VALIDATION = "validation";
	private final List<String> values = new ArrayList<>();
	@SuppressWarnings("unused")
	private final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
	
	@Before
	public final void setup() {
		values.add("Kylie");
	}
	
	@Test
	public final void createConditionAllValues() {		
		final Condition condition = new ConditionImpl(ConditionType.PricePerUnit, values, VALIDATION, CALCULATION );
		Assert.assertEquals(values, condition.values());
		Assert.assertEquals(VALIDATION, condition.validation());
		Assert.assertEquals(CALCULATION, condition.calculation());
		Assert.assertEquals(ConditionType.PricePerUnit, condition.conditionType());
		Assert.assertNull(condition.commercialRelation());
	}
	
	@Test
	public final void createOnlyValues() {
		final Condition condition = new ConditionImpl(ConditionType.PricePerUnit, values);
		Assert.assertEquals(values, condition.values());
		Assert.assertEquals(ConditionType.PricePerUnit, condition.conditionType());
		Assert.assertNull(condition.validation());
		Assert.assertNull(condition.calculation());
		Assert.assertNull(condition.commercialRelation());
	}
	
	@Test
	public final void conditionType() {
		for(final ConditionType conditionType : ConditionType.values()){
			Assert.assertEquals(conditionType, ConditionType.valueOf(conditionType.name()));
		}
	}
	
	@Test
	public final void hash() {
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		final RuleOperations condition = newCondition(commercialRelation, ConditionType.PricePerUnit);
		
		Assert.assertEquals(commercialRelation.hashCode() + ConditionType.PricePerUnit.hashCode() , condition.hashCode());
	}
	
	@Test
	public final void equals() {
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		Assert.assertTrue(newCondition(commercialRelation, ConditionType.PricePerUnit).equals(newCondition(commercialRelation, ConditionType.PricePerUnit)));
		Assert.assertFalse(newCondition(Mockito.mock(CommercialRelation.class), ConditionType.PricePerUnit).equals(newCondition(commercialRelation, ConditionType.PricePerUnit)));
	
	}

	private RuleOperations newCondition(final CommercialRelation commercialRelation, final ConditionType conditionType) {
		final RuleOperations condition = new ConditionImpl(conditionType, values);
		ReflectionTestUtils.setField(condition, "commercialRelation", commercialRelation);
		return condition;
	}
	
	@Test
	public final void create() {
		final Opportunity opportunity = new OpportunityImpl();
		CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		opportunity.assignConditions(commercialSubject,new ConditionImpl(ConditionType.PricePerUnit, new ArrayList<String>()), new ConditionImpl(ConditionType.Quantity, new ArrayList<String>()));
		Assert.assertEquals(1, opportunity.commercialRelations().size());
		Assert.assertEquals(2,  opportunity.commercialRelations().iterator().next().conditions().keySet().size());
		Assert.assertTrue(opportunity.commercialRelations().iterator().next().conditions().containsKey(ConditionType.PricePerUnit));
		Assert.assertTrue(opportunity.commercialRelations().iterator().next().conditions().containsKey(ConditionType.Quantity));
		
	}
	
	@Test
	public final void id() {
		final Condition condition = new ConditionImpl();
		ReflectionTestUtils.setField(condition, "id", ID);
		Assert.assertEquals(ID, condition.id());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNotSet()  {
		 new ConditionImpl().id();
	}
	
	@Test
	public final void hasId() {
		final Condition condition = new ConditionImpl();
		ReflectionTestUtils.setField(condition, "id", ID);
		Assert.assertTrue(condition.hasId());
		
	}
	
	@Test
	public final void hasIdNotExisting() {
		Assert.assertFalse(new ConditionImpl().hasId());
	}
	
	@Test
	public final void assign() {
		final Condition condition = new ConditionImpl();
		for(int i=0; i< 10; i++){
			condition.assignValue(VALUE);
		}
		Assert.assertEquals(1, condition.values().size());
		Assert.assertEquals(VALUE, condition.values().iterator().next());
	}
	
	@Test
	public final void deleteValue() {
		final Condition condition = new ConditionImpl();
		condition.assignValue(VALUE);
		Assert.assertEquals(1, condition.values().size());
		
		condition.removeValue(VALUE);
		
		Assert.assertEquals(0, condition.values().size());
	}
	
	
	@Test
	public final void ruleInstance() {
		
		final RuleOperations condition = EntityUtil.create(ConditionImpl.class);
		final List<RuleInstance> ruleInstances = new ArrayList<>();
		final RuleInstance ruleInstance =Mockito.mock(RuleInstance.class);
		final Rule rule = Mockito.mock(Rule.class);
		Mockito.when(ruleInstance.forRule(rule)).thenReturn(true);
		Mockito.when(ruleInstance.rule()).thenReturn(rule);
		Mockito.when(rule.name()).thenReturn("hotScore");
		final RuleInstance otherRuleInstance =Mockito.mock(RuleInstance.class);
		final Rule otherRule = Mockito.mock(Rule.class);
		Mockito.when(otherRuleInstance.forRule(otherRule)).thenReturn(true);
		Mockito.when(otherRuleInstance.rule()).thenReturn(otherRule);
		Mockito.when(otherRule.name()).thenReturn("category");
		
		ruleInstances.add(ruleInstance);
		ruleInstances.add(otherRuleInstance);
		
		ReflectionTestUtils.setField(condition, "ruleInstances", ruleInstances);
		
		Assert.assertEquals(ruleInstance, condition.ruleInstance(rule));
		Assert.assertEquals(otherRuleInstance, condition.ruleInstance(otherRule));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void ruleInstanceNotFound() {
		final RuleOperations condition = EntityUtil.create(ConditionImpl.class);
		final Rule rule = Mockito.mock(Rule.class);
		Mockito.when(rule.name()).thenReturn("hotScore");
		condition.ruleInstance(rule);
		
	}
	
	@Test
	public final void remove() {
		final Condition condition =  EntityUtil.create(ConditionImpl.class);
		final Rule rule = Mockito.mock(Rule.class);
		final RuleInstance ruleInstance = new RuleInstanceImpl(condition, rule, 4711);
		List<RuleInstance> ruleInstances = new ArrayList<>();
		ruleInstances.add(ruleInstance);
		ReflectionTestUtils.setField(condition, "ruleInstances", ruleInstances);
		Assert.assertFalse(condition.ruleInstances().isEmpty());
		condition.remove(rule);
		Assert.assertTrue(condition.ruleInstances().isEmpty());
		
	}

}
