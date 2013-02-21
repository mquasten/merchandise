package de.mq.merchandise.model.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.customer.LegalForm;

@Component
public class String2LegalFormConverter implements Converter<String,LegalForm> {

	@Override
	public LegalForm convert(final String source) {
		return LegalForm.valueOf(source);
	}

}
