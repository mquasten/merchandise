package de.mq.merchandise.subject.support;

import de.mq.merchandise.subject.Subject;

public interface CommercialSubjectItem {

	String name();

	boolean mandatory();
	
	Subject subject();

}