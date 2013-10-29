package de.mq.merchandise.opportunity.support;

import java.util.Date;

public interface EntityContextAggregation {
	
	long counter();
	Date minDate();

}