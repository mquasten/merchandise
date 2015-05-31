package de.mq.merchandise.subject.support;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;


interface  SubjectEventFascade {
	
	@SubjectEventQualifier(EventType.SubjectChanged)
	abstract Subject subjectChanged(final Long id) ; 
	
	
	
}
