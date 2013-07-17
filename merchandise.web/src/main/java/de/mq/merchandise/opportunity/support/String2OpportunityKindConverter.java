package de.mq.merchandise.opportunity.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class String2OpportunityKindConverter implements Converter<String,Opportunity.Kind> {

	@Override
	public Opportunity.Kind convert(final String source) {
		if(source == null){
			return null;
		}
		return Opportunity.Kind.valueOf(source);
	}

}
