package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.List;

public interface Condition extends Serializable {
	
	enum ConditionType {
		Quantity,
		Unit,
		PricePerUnit;
	}

	List<String> values();

	String calculation();

	String validation(); 
	
}
