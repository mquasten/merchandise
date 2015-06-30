package de.mq.merchandise.subject.support;

import java.util.Collection;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;


interface   SubjectEventFascade {
	
	@SubjectEventQualifier(EventType.SubjectChanged)
	abstract Subject subjectChanged(final Long id) ; 
	
	
	@SubjectEventQualifier(EventType.SubjectSaved)
	abstract void save(final Long subjectId, final Subject subject);
	
	@SubjectEventQualifier(EventType.ConditionSaved)
	abstract Subject save(final Condition subject, final Long subjectId);
	
	@SubjectEventQualifier(EventType.SubjectDeleted)
	abstract void delete(final Subject subject);
	
	@SubjectEventQualifier(EventType.ConditionDeleted)
	abstract Subject delete(final Condition condition, final Long subjectId);
	
	
	@SubjectEventQualifier(EventType.CountPaging)
	abstract Number countSubjects();
	


	@SubjectEventQualifier(EventType.ListPaging)
	abstract Collection<Subject> subjects(final ResultNavigation paging);

	@SubjectEventQualifier(EventType.ConditionChanged)
	abstract Condition conditionChanged(final Long conditionId);
	
}
