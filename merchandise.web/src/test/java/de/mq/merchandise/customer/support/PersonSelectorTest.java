package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.LegalPersonImpl;
import de.mq.merchandise.customer.support.NaturalPersonAO;
import de.mq.merchandise.customer.support.NaturalPersonImpl;
import de.mq.merchandise.customer.support.PersonSelector;
import de.mq.merchandise.util.EntityUtil;

public class PersonSelectorTest {
	
	@Test
	public final void naturalPerson() {
		final Converter<Person, Class<?>> personSelector = new PersonSelector();
		Assert.assertEquals(NaturalPersonAO.class, personSelector.convert(EntityUtil.create(NaturalPersonImpl.class)));
		
		Assert.assertEquals(LegalPersonAO.class, personSelector.convert(EntityUtil.create(LegalPersonImpl.class)));
		
		
		
	}
	

}
