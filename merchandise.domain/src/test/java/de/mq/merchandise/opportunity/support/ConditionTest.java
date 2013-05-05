package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class ConditionTest {
	
	private static final String CALCULATION = "calculation";
	private static final String VALIDATION = "validation";
	private final List<String> values = new ArrayList<>();
	private final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
	
	@Before
	public final void setup() {
		values.add("Kylie");
	}
	
	@Test
	public final void createConditionAllValues() {		
		final Condition condition = new ConditionImpl(commercialRelation, values, VALIDATION, CALCULATION );
		Assert.assertEquals(values, condition.values());
		Assert.assertEquals(VALIDATION, condition.validation());
		Assert.assertEquals(CALCULATION, condition.calculation());
		Assert.assertEquals(commercialRelation, condition.commercialRelation());
	}
	
	@Test
	public final void createOnlyValues() {
		final Condition condition = new ConditionImpl(commercialRelation, values);
		Assert.assertEquals(values, condition.values());
		Assert.assertEquals(commercialRelation, condition.commercialRelation());
		Assert.assertNull(condition.validation());
		Assert.assertNull(condition.calculation());
	}
	
	@Test
	public final void conditionType() {
		for(final ConditionType conditionType : ConditionType.values()){
			Assert.assertEquals(conditionType, ConditionType.valueOf(conditionType.name()));
		}
	}

}
