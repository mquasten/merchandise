package de.mq.merchandise.opportunity.support;

import de.mq.merchandise.BasicEntity;

public interface Classification extends BasicEntity {
	enum Kind {
		Product,
		Activity
	}
	
	String description();
	
	Classification parent() ;

}
