package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectMapper.SubjectMapperType;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.support.Mapper;

@Controller
class SubjectModelControllerImpl {
	
	
	private final  SubjectService subjectService;
	private final  Mapper<Subject,Subject> subjectIntoSubjectMapper;
	
	@Autowired
	SubjectModelControllerImpl(final SubjectService subjectService, final @SubjectMapper(SubjectMapperType.Subject2Subject) Mapper<Subject,Subject> subjectIntoSubjectMapper) {
		this.subjectService=subjectService;
		this.subjectIntoSubjectMapper=subjectIntoSubjectMapper;
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
	
	@SubjectEventQualifier(EventType.SubjectSaved)
	public  void save(final Long subjectId, final Subject subject ) {
		Assert.notNull(subject.customer(), "Customer is mandatory");
		if( subjectId == null){
			subjectService.save(new SubjectImpl(subject.customer(), subject.name(), subject.description()));
			return;
		}
		
		 final Subject toBeChanged = subjectService.subject(subjectId);
		 Assert.notNull(toBeChanged.customer(), "Customer is mandatory");
		
		 Assert.isTrue(toBeChanged.customer().equals(subject.customer()));
		
		 subjectIntoSubjectMapper.mapInto(subject, toBeChanged);
		 
		 subjectService.save(toBeChanged);
		
	}
	
	@SubjectEventQualifier(EventType.SubjectDeleted)
	public  void delete(final Subject subject) {
		
		subjectService.remove(subject);
		
	}

}
