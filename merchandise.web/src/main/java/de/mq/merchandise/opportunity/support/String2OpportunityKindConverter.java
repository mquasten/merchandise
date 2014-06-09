package de.mq.merchandise.opportunity.support;

import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.support.AbstractString2EnumConverter;

@Component
public class String2OpportunityKindConverter  extends AbstractString2EnumConverter{

	@Override
	protected Enum<?> value(final String value) {
		return Opportunity.Kind.valueOf(value);
	}

	

}
