package de.mq.merchandise.subject.support;



import org.springframework.stereotype.Component;


import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

@Component
@MapperQualifier(MapperType.CommercialSubject2CommercialSubject)
class CommercialSubjectIntoCommercialSubjectMapperImpl extends ReflectionBasedFieldMapperImpl implements Mapper<CommercialSubject,CommercialSubject> {

	
	
	static final String NAME_FIELD = "name";

	@Override
	public CommercialSubject mapInto(final CommercialSubject source, final CommercialSubject target) {
		assign(NAME_FIELD, target, source.name());
		return target;
	}

	

}
