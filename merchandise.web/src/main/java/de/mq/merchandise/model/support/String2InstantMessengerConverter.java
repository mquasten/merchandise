package de.mq.merchandise.model.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.contact.InstantMessenger;

@Component
public class String2InstantMessengerConverter implements Converter<String, InstantMessenger > {

	
	@Override
	public InstantMessenger convert(final String value) {
		return InstantMessenger.valueOf(value);
	}

}
