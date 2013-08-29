package de.mq.merchandise.opportunity.support;


import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;

public interface CommercialSubject extends BasicEntity, DocumentsAware {
	
	
	String name();

	String description();

	Customer customer();

}