package de.mq.merchandise.opportunity.support;

import java.util.Map;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public interface CommercialRelation extends BasicEntity, RuleOperations {

	CommercialSubject commercialSubject();

	Opportunity opportunity();

	Condition condition(final Condition.ConditionType conditionType);

	void assign(final Condition condition);

	Map<ConditionType, Condition> conditions();

}