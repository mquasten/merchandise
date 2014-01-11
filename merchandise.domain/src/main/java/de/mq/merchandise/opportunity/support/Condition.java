package de.mq.merchandise.opportunity.support;

import java.util.List;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.rule.Rule;

public interface Condition extends BasicEntity {
	
	enum ConditionType {
		Quantity,
		Unit,
		PricePerUnit,
		Currency; 
	}

	List<String> values();

	String calculation();

	String validation(); 
	
	CommercialRelation commercialRelation();
	
	ConditionType conditionType();
	
	void assignValue(final String value);
	
	void removeValue(final String value);

	List<RuleInstance> ruleInstances();

	void assign(final Rule rule, final int priority);

	RuleInstance ruleInstance(final Rule rule);
	
}
