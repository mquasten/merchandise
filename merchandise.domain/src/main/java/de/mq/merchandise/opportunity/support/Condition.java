package de.mq.merchandise.opportunity.support;

import java.util.List;

import de.mq.merchandise.BasicEntity;

public interface Condition extends BasicEntity, RuleOperations {
	
	enum ConditionType {
		Product,
		Quantity,
		Unit,
		PricePerUnit,
		Quality,
		Currency; 
		
	}
	
	enum InputType {
		User,
		Calculated
	}

	List<String> values();
	
	CommercialRelation commercialRelation();
	
	ConditionType conditionType();
	
	InputType inputTyp();
	
	void assignValue(final String value);
	
	void removeValue(final String value);
	
}
