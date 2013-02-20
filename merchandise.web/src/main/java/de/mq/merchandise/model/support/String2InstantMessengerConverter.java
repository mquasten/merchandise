package de.mq.merchandise.model.support;

import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.contact.InstantMessenger;

public class String2InstantMessengerConverter implements Converter<String, InstantMessenger > {

	
	@Override
	public InstantMessenger convert(final String value) {
		return InstantMessenger.valueOf(value);
	}

}
