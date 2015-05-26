package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;

@Controller
class SubjectModelControllerImpl {
	
	
	private final  SubjectService subjectService;
	
	@Autowired
	SubjectModelControllerImpl(final SubjectService subjectService) {
		this.subjectService=subjectService;
	}
	
	
	@SubjectEventQualifier(EventType.CountPaging)
	Number countSubjects(final SubjectModel subjectModel) {
		return subjectService.subjects(subjectModel.getSearchCriteria());
	
	}
	


	@SubjectEventQualifier(EventType.ListPaging)
	Collection<Subject> subjects(final SubjectModel subjectModel, final ResultNavigation paging) {
		return subjectService.subjects(subjectModel.getSearchCriteria(), paging);
	}
	
	@SubjectEventQualifier(EventType.SubjectChanged)
	public  Subject subject(final Long id ) {
		return subjectService.subject(id);
	}

}
