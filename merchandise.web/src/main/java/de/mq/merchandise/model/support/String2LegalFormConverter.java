package de.mq.merchandise.model.support;

import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.LegalForm;

public class String2LegalFormConverter implements Converter<String,LegalForm> {

	@Override
	public LegalForm convert(final String source) {
		return LegalForm.valueOf(source);
	}

}
