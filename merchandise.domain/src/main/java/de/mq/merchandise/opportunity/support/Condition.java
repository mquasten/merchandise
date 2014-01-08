package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.List;

import de.mq.merchandise.BasicEntity;

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

	Collection<RuleInstance> ruleInstances();
	
}
