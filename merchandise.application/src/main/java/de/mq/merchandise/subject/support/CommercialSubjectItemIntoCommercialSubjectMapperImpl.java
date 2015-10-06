package de.mq.merchandise.subject.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

@Component
@MapperQualifier(MapperType.CommercialSubjectItemIntoCommercialSubject)
class CommercialSubjectItemIntoCommercialSubjectMapperImpl extends ReflectionBasedFieldMapperImpl implements Mapper<CommercialSubjectItem,CommercialSubject>{

	private final SubjectService subjectService; 
	
	@Autowired
	CommercialSubjectItemIntoCommercialSubjectMapperImpl(final SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	@Override
	public CommercialSubject mapInto(final CommercialSubjectItem source, final CommercialSubject target) {
		
		Assert.notNull(source, "CommercialSubjectItem is mandatory");
		Assert.notNull(target, "CommercialSubject is mandatory");
		Assert.notNull(source.subject(), "Subject must be assigned");
		Assert.isTrue( source.subject().id().isPresent(), "Subject must be assigned");
		

		final Subject subject = subjectService.subject(source.subject().id().get());
		
		if( source.id().orElse(-1L) <= 0 ) {
			target.assign(subject, source.name(), source.mandatory());
			return target;
		}
		

		
		
		Assert.isTrue(target.commercialSubjectItem(subject).isPresent(), "Item not assigned for subject");
		final CommercialSubjectItem toBeUpdated = target.commercialSubjectItem(subject).get();
				
		assign("name", toBeUpdated, source.name());
		assign("mandatory", toBeUpdated, source.mandatory());
		return target;
	}

	
	
}
