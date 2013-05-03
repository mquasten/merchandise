package de.mq.merchandise.opportunity.support;

import java.util.Map;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;

interface CommercialRelation extends BasicEntity{

	DocumentsAware commercialSubject();

	Opportunity opportunity();

	Condition condition(final Condition.ConditionType conditionType);

	void assign(final ConditionType conditionType, final Condition condition);

	Map<ConditionType, Condition> conditions();

}