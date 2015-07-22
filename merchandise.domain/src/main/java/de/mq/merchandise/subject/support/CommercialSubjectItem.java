package de.mq.merchandise.subject.support;

import java.util.Collection;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public interface CommercialSubjectItem {

	String name();

	boolean mandatory();
	
	Subject subject();

	Collection<Condition> conditions();

	<T> void assign(String conditionType, T value);

}