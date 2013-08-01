package de.mq.merchandise.opportunity.support;

import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class String2ConditionType  implements Converter<String, ConditionType>{

	@Override
	public ConditionType convert(final String source) {
		if( source == null){
			return null;
		}
		return ConditionType.valueOf(source);
	}

}
