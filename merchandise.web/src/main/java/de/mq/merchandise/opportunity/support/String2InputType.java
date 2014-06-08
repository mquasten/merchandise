package de.mq.merchandise.opportunity.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.opportunity.support.Condition.InputType;

@Component
public class String2InputType implements Converter<String, InputType> {

	@Override
	public InputType convert(final String source) {
		
		if( source == null){
			return null;
		}
		return InputType.valueOf(source);
	}

}
