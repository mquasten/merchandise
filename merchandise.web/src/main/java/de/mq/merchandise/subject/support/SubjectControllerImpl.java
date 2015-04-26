package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.LazyQueryContainerFactory.PagingMethod;
import de.mq.merchandise.util.LazyQueryContainerFactory.PagingMethods;

@Controller
class SubjectControllerImpl {
	
	private final  SubjectService subjectService;
	
	@Autowired
	SubjectControllerImpl(final SubjectService subjectService) {
		this.subjectService=subjectService;
	}
	
	@PagingMethod(PagingMethods.Count)
	Number countSubjects(final Subject subject) {
		
		return subjectService.subjects(subject, new ResultNavigation() {
			
			@Override
			public Number pageSize() {
				
				return Integer.MAX_VALUE;
			}
			
			@Override
			public Number firstRow() {
				
				return 0;
			}
		}).size();
	}
	
	@PagingMethod(PagingMethods.Read)
	Collection<Subject> subjects(Subject subjects, ResultNavigation paging) {
		return subjectService.subjects(subjects, paging);
	}

}
