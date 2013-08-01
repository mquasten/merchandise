package de.mq.merchandise.opportunity.support;

import java.util.List;

import de.mq.merchandise.BasicEntity;

public interface Condition extends BasicEntity {
	
	enum ConditionType {
		Quantity,
		Unit,
		PricePerUnit;
	}

	List<String> values();

	String calculation();

	String validation(); 
	
	CommercialRelation commercialRelation();
	
	ConditionType conditionType();
	
	
	
}
