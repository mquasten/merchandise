package de.mq.merchandise.subject.support;

import java.util.Collection;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;


interface   SubjectEventFascade {
	
	@SubjectEventQualifier(EventType.SubjectChanged)
	abstract Subject subjectChanged(final Long id) ; 
	
	
	@SubjectEventQualifier(EventType.SubjectSaved)
	abstract void save(final Long subjectId, final Subject subject);
	
	
	@SubjectEventQualifier(EventType.CountPaging)
	abstract Number countSubjects();
	


	@SubjectEventQualifier(EventType.ListPaging)
	abstract Collection<Subject> subjects(final ResultNavigation paging);
	
}
