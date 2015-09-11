package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

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


	@CommercialSubjectEventQualifier(EventType.CommercialSubjectSaved)
	void save(final Long commercialSubjectId, final CommercialSubject commercialSubject) {
		Assert.notNull(commercialSubject.customer(), "Customer is mandatory");
		
		if (commercialSubjectId == null) {
			commercialSubjectService.save(new CommercialSubjectImpl( commercialSubject.name(), commercialSubject.customer()));
			return;
		}

		final CommercialSubject toBeChanged = commercialSubjectService.commercialSubject(commercialSubjectId);
		Assert.notNull(toBeChanged.customer(), "Customer is mandatory");

		Assert.isTrue(toBeChanged.customer().equals(commercialSubject.customer()));

		
		final Field field = ReflectionUtils.findField(toBeChanged.getClass(), "name");
		field.setAccessible(true);
		ReflectionUtils.setField(field, toBeChanged, commercialSubject.name());
		

		commercialSubjectService.save(toBeChanged);
	}


	@CommercialSubjectEventQualifier(EventType.CommercialSubjectDeleted)
	void delete(final CommercialSubject commercialSubject) {
		commercialSubjectService.remove(commercialSubject);
	}

}
