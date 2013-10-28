package de.mq.merchandise.opportunity.support;

import java.util.Date;

interface EntityContextAggregation {
	
	long counter();
	Date minDate();

}