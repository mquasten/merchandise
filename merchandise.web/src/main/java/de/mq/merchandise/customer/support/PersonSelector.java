package de.mq.merchandise.customer.support;

import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.Person;



public class PersonSelector implements Converter<Person, Class<?>> {
	
	

	
	
	@Override
	public Class<?> convert(final Person person) {
	
		if (person instanceof NaturalPerson) {
			return NaturalPersonAO.class;
		}
		
		return LegalPersonAO.class;
	}

	

}
