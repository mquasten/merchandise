package de.mq.merchandise.subject.support;

import java.util.Collection;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;

public interface SubjectService {

	Collection<Subject> subjects(Subject subject, final ResultNavigation paging);

	Number subjects(Subject subject); 
	
	void save(Subject subject);

	public abstract void remove(Subject subject);

	

}