package de.mq.merchandise.opportunity.support;

import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.support.AbstractString2EnumConverter;
import de.mq.merchandise.opportunity.support.Condition.InputType;

@Component
public class String2InputType  extends AbstractString2EnumConverter {

	@Override
	protected Enum<?> value(String value) {
		return InputType.valueOf(value);
	}

	

}
