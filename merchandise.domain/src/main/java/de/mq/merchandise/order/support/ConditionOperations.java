package de.mq.merchandise.order.support;

import java.util.Collection;
import java.util.Map;

import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.order.Item;

public interface ConditionOperations {

	void copy(final Collection<Condition> conditions, final Item item);

	void copy(final Map<Condition.ConditionType, String> values, final Item item);

	void copy(final ConditionType conditionType, final String value, final Item item);

	

}