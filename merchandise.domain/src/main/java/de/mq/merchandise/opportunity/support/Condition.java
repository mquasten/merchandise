package de.mq.merchandise.opportunity.support;

import java.util.List;

import de.mq.merchandise.BasicEntity;

public interface Condition extends BasicEntity, RuleOperations {
	
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
	
}
