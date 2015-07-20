package de.mq.merchandise.subject.support;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.BasicEntity;

public interface CommercialSubjet extends  BasicEntity {

	Customer customer();

	String name();
	
	void assign(final Subject subject, final String name, final boolean mandatory);

}