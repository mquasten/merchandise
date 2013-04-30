package de.mq.merchandise.opportunity.support;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;

interface CommercialSubject extends BasicEntity {

	void assignDocument(final String name, final byte[] document);

	void removeDocument(final String name);

	String name();

	String description();

	Customer customer();

}