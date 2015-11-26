package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

@Component
@MapperQualifier(MapperType.CommercialSubjectItemIntoCommercialSubject)
class CommercialSubjectItemIntoCommercialSubjectMapperImpl extends ReflectionBasedFieldMapperImpl implements Mapper<CommercialSubjectItem,CommercialSubject>{

	private static final String SUBJECT_FIELD = "subject";
	private static final String MANDATORY_FIELD = "mandatory";
	private static final String NAME_FIELD = "name";
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
		

		final Optional<CommercialSubjectItem> item =  target.commercialSubjectItems().stream().filter(currentItem -> currentItem.id().equals(source.id())).findFirst();
	
		
		Assert.isTrue(item.isPresent(), "Item not assigned for subject");
		
		
		
		final CommercialSubjectItem toBeUpdated = item.get();
		
		
		if( ! toBeUpdated.subject().id().equals(source.subject().id()))  {
			toBeUpdated.conditionValues().forEach(e -> remove(toBeUpdated, e.getKey(), e.getValue()));
		}
		
		assign(NAME_FIELD, toBeUpdated, source.name());
		assign(MANDATORY_FIELD, toBeUpdated, source.mandatory());
		assign(SUBJECT_FIELD, toBeUpdated, subject);
		return target;
	}

	private <T> void remove(final CommercialSubjectItem item, final Condition condition, final Collection<T> values) {
		values.forEach(v -> item.remove(condition.conditionType(), v));	
	}
	
}
