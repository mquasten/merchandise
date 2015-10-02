package de.mq.merchandise.subject.support;

import org.springframework.stereotype.Component;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

@Component
@MapperQualifier(MapperType.Condition2Subject)
class ConditionIntoSubjectMapperImpl extends ReflectionBasedFieldMapperImpl implements Mapper<Condition, Subject>{

	private static final String DATA_TYPE_FIELD = "dataType";
	private static final String CONDITION_TYPE_FIELD = "conditionType";

	@Override
	public Subject mapInto(final Condition condition, final Subject subject) {
		if(  condition.id().orElse(-1L) < 0 ) {
			subject.add(condition.conditionType(), condition.conditionDataType());
			return subject;
		}
		final Condition toBeUpdated = subject.condition(condition.conditionType());
		assign(CONDITION_TYPE_FIELD, toBeUpdated, condition.conditionType());
		assign(DATA_TYPE_FIELD, toBeUpdated, condition.conditionDataType());
		return subject;
	}

}
