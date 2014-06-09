package de.mq.merchandise.opportunity.support;

import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.support.AbstractString2EnumConverter;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;

@Component
public class String2ConditionType extends AbstractString2EnumConverter{

	@Override
	protected Enum<?> value(final String value) {
		return ConditionType.valueOf(value);
	}

	

}
