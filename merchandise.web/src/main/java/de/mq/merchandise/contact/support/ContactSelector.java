package de.mq.merchandise.contact.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.EMailContactImpl;
import de.mq.merchandise.contact.support.PhoneContactImpl;

@Component
public class ContactSelector implements  Converter<LoginContact, Class<?>>  {

	@Override
	public Class<?> convert(final LoginContact contact) {
		
		if (contact instanceof PhoneContactImpl) {
			 return PhoneContactAO.class;
		} 
		
		if (contact instanceof EMailContactImpl) {
			 return EMailContactAO.class;
			
		}
		
		return MessengerContactAO.class;
	}

	

	

}
