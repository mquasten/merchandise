package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;

@Controller
class CommercialSubjectModelControllerImpl {

	private final CommercialSubjectService commercialSubjectService;
	
	@Autowired
	CommercialSubjectModelControllerImpl(CommercialSubjectService commercialSubjectService) {
		this.commercialSubjectService = commercialSubjectService;
		
	}

	@CommercialSubjectEventQualifier(EventType.CountPaging)
	Number countCommercialSubjects(final CommercialSubjectModel subjectModel) {
		return commercialSubjectService.commercialSubjects(subjectModel.getSearch());
	}

	@CommercialSubjectEventQualifier(EventType.ListPaging)
	Collection<CommercialSubject> commercialSubjects(final CommercialSubjectModel subjectModel, final ResultNavigation paging) {
		return commercialSubjectService.commercialSubjects(subjectModel.getSearch(),paging);
	}


	@CommercialSubjectEventQualifier(EventType.CommericalSubjectChanged)
	CommercialSubject subject(final Long id) {
		return commercialSubjectService.commercialSubject(id);
	}


	


	

}
