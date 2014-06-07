package de.mq.merchandise.opportunity.support;

import java.util.List;

import de.mq.merchandise.BasicEntity;

public interface Condition extends BasicEntity, RuleOperations {
	
	enum ConditionType {
		Quantity,
		Unit,
		PricePerUnit,
		Quality,
		Currency; 
		
	}

	List<String> values();
	
	CommercialRelation commercialRelation();
	
	ConditionType conditionType();
	
	void assignValue(final String value);
	
	void removeValue(final String value);
	
}
