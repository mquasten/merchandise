package de.mq.merchandise.opportunity.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class Opportunity2TSStringConverterImpl implements Converter<Opportunity,String> {

	@Override
	public String convert(final Opportunity opportunity) {
		final StringBuilder builder = new StringBuilder();
		builder.append(nvl(opportunity.name()));
		builder.append(" ");
		builder.append(nvl(opportunity.description()));
		builder.append(" ");
		builder.append(nvl(opportunity.customer().person().name()));
		builder.append(" ");
		for(final String keyword : opportunity.keyWords()){
			builder.append(nvl(keyword));
			builder.append(" ");
		}
		
		builder.append(nvl(opportunity.kind()));
		
		return builder.toString();
	}
	

	String nvl(final Object value){
		if( value==null){
			return "";
		}
		return String.valueOf(value).trim();
	}

}
