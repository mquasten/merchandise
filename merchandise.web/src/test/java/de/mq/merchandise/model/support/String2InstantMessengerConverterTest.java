package de.mq.merchandise.model.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.model.support.String2InstantMessengerConverter;

public class String2InstantMessengerConverterTest {
	
	private Converter<String,InstantMessenger> converter = new String2InstantMessengerConverter();
	
	@Test
	public final void convert() {
		for( InstantMessenger instantMessenger : InstantMessenger.values()){
				Assert.assertEquals(instantMessenger, converter.convert(instantMessenger.name()));
		}
	}

}
