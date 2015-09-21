package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map.Entry;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.BasicEntity;

interface CommercialSubjectItem  extends BasicEntity {
	

	String name();

	boolean mandatory();

	Subject subject();

	CommercialSubject commercialSubject();

	<T> Collection<Entry<Condition, Collection<T>>> conditionValues();

	<T> void assign(String conditionType, T value);

	<T> void remove(String conditionType, T value);

}