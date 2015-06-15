package de.mq.merchandise.subject.support;



import org.springframework.stereotype.Component;



import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectMapper.SubjectMapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

@Component
@SubjectMapper(SubjectMapperType.Subject2Subject)
class SubjectIntoSubjectMapperImpl extends ReflectionBasedFieldMapperImpl implements Mapper<Subject,Subject> {

	
	static final String DESCRIPTION_FIELD = "description";
	static final String NAME_FIELD = "name";

	@Override
	public Subject mapInto(final Subject source, final Subject target) {
		assign(NAME_FIELD, target, source.name());
		assign(DESCRIPTION_FIELD, target, source.description());
	
		return target;
	}

	

}
