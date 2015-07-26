package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.BasicEntity;

public interface CommercialSubjet extends  BasicEntity {

	Customer customer();

	String name();
	
	void assign(final Subject subject, final String name, final boolean mandatory);

	Collection<Subject> subjects();

	<T> Collection<Entry<Condition, Collection<T>>> conditionValues(final Subject subject);

	<T> void assign(Condition condition, T value);

	void remove(Subject subject);

	<T> void remove(Condition condition, T value);

	Collection<CommercialSubjectItem> commercialSubjectItems();

	Optional<CommercialSubjectItem> commercialSubjectItem(Subject subject);
	
	

}