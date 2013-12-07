package de.mq.merchandise.model.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class String2LongConverter implements Converter<String,Long> {

	@Override
	public Long convert(final String source) {
		if( source == null){
			return null;
		}
		return Long.valueOf(source);
	}

}
