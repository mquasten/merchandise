package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class CommercialRelationTest {
	
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
	
	
	
}
