package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class ConditionConstants {
	
	public static  Condition CONDITION = condition();
	
	 private static Condition  condition() {
		final List<String> values = new ArrayList<>();
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		values.add("value");
		final Condition condition = new ConditionImpl(ConditionType.PricePerUnit, values, "validation", "calculation");
		ReflectionTestUtils.setField(condition, "commercialRelation", commercialRelation);
		return condition;
		
	}

}
