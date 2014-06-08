package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.opportunity.support.Condition.InputType;
import de.mq.merchandise.util.EntityUtil;

public class ConditionConstants {
	
	public static  Condition CONDITION = condition();
	
	 private static Condition  condition() {
		final List<String> values = new ArrayList<>();
		final CommercialRelation commercialRelation = EntityUtil.create(CommercialRelationImpl.class);
		values.add("value");
		final Condition condition = new ConditionImpl(ConditionType.PricePerUnit, InputType.User, values);
		ReflectionTestUtils.setField(condition, "commercialRelation", commercialRelation);
		ReflectionTestUtils.setField(condition, "id", 19680528L);
		return condition;
		
	}

}
