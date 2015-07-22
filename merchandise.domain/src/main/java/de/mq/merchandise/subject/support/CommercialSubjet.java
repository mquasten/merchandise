package de.mq.merchandise.subject.support;

import java.util.Collection;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.BasicEntity;

public interface CommercialSubjet extends  BasicEntity {

	Customer customer();

	String name();
	
	void assign(final Subject subject, final String name, final boolean mandatory);

	Collection<Subject> subjects();

	Collection<Condition> conditions(final Subject subject);

	<T> void assign(Condition condition, T value);

}