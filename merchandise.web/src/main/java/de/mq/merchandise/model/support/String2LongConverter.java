package de.mq.merchandise.model.support;

import org.springframework.core.convert.converter.Converter;

public class String2LongConverter implements Converter<String,Long> {

	@Override
	public Long convert(final String source) {
		
		return Long.valueOf(source);
	}

}
