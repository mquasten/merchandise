package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map.Entry;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public interface CommercialSubjectItem {

	String name();

	boolean mandatory();
	
	Subject subject();

	<T>  Collection<Entry<Condition, Collection<T>>>conditionValues();

	<T> void assign(String conditionType, T value);

}