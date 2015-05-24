package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.util.LazyQueryContainerFactory.PagingMethod;
import de.mq.merchandise.util.LazyQueryContainerFactory.PagingMethods;

@Controller

class SubjectModelControllerImpl {
	
	final static  String NAME = PagingMethods.Count.name();
	private final  SubjectService subjectService;
	
	@Autowired
	SubjectModelControllerImpl(final SubjectService subjectService) {
		this.subjectService=subjectService;
	}
	
	@PagingMethod(PagingMethods.Count)
	Number countSubjects(final SubjectModel subjectModel) {
		return subjectService.subjects(subjectModel.getSearchCriteria());
	
	}
	

	@PagingMethod(PagingMethods.Read)
	Collection<Subject> subjects(final SubjectModel subjectModel, final ResultNavigation paging) {
		return subjectService.subjects(subjectModel.getSearchCriteria(), paging);
	}
	
	@SubjectEventQualifier(EventType.SubjectChanged)
	public  void test() {
		
	}

}
