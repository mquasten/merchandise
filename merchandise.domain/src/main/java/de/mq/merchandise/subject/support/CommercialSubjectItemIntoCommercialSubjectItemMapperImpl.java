package de.mq.merchandise.subject.support;

import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

class CommercialSubjectItemIntoCommercialSubjectItemMapperImpl  extends ReflectionBasedFieldMapperImpl implements Mapper<CommercialSubjectItemImpl,CommercialSubjectItemImpl>{

	private static final String NAME_FIELD = "name";
	private static final String MANDATORY_FIELD = "mandatory";

	@Override
	public final CommercialSubjectItemImpl mapInto(final CommercialSubjectItemImpl source, final CommercialSubjectItemImpl target) {
		assign(MANDATORY_FIELD,target, source.mandatory());
		assign(NAME_FIELD,target, source.name());
		return target;
	}

}
