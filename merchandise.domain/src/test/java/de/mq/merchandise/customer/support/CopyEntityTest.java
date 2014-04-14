package de.mq.merchandise.customer.support;

import java.util.GregorianCalendar;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.util.EntityUtil;

public class CopyEntityTest {
	
	@Test
	public final void copy() {
	
		Customer kylie = newCustomer();
		
		
		final Customer kyliesCopy = EntityUtil.copy(kylie);
	
		modify(kyliesCopy);
		
		for(final Contact contact : kylie.person().contacts()) {
			Assert.assertFalse(contact.hasId());
		}

		Assert.assertFalse(kylie.person().hasId());
		Assert.assertFalse(kylie.hasId());
	    Assert.assertNotNull(((NaturalPerson)kylie.person()).nativity().birthPlace());
	
	}
	
	@Test
	public final void shallowCopy() {
		final Customer kylie = newCustomer();
		final Customer kyliesCopy = EntityUtil.create(CustomerImpl.class);
		ReflectionUtils.shallowCopyFieldState(kylie, kyliesCopy);
		
		modify(kyliesCopy);
		
		
		
		
		
		for(final Contact contact : kylie.person().contacts()) {
			Assert.assertTrue(contact.hasId());
		}

		Assert.assertTrue(kylie.person().hasId());
		Assert.assertFalse(kylie.hasId());
	    Assert.assertNull(((NaturalPerson)kylie.person()).nativity().birthPlace());
	
	}

	private void modify(final Customer kyliesCopy) {
		ReflectionTestUtils.setField(kyliesCopy.person(), "id", (long) (Math.random()* 1e12));
		ReflectionTestUtils.setField(kyliesCopy, "id", (long) (Math.random()* 1e12));
		ReflectionTestUtils.setField(((NaturalPerson)kyliesCopy.person()).nativity(), "birthPlace", null);
		for(final Contact contact : kyliesCopy.person().contacts()) {
			
			ReflectionTestUtils.setField(contact, "id", (long) (Math.random()* 1e12));
			
		}
	}
	
	
	private Customer newCustomer() {
		final Nativity nativity = new NativityImpl("Melborne", new GregorianCalendar(1968,5,28).getTime());
		final NaturalPerson person = new NaturalPersonImpl("Kylie Ann", "Minogue",nativity, Locale.UK);
		
	    final Coordinates coordinates = new ContactBuilderFactoryImpl().coordinatesBuilder().withLongitude(145).withLatitude(38).build();
		final Address address = new ContactBuilderFactoryImpl().addressBuilder().withCity("Melborne").withCountry(Locale.UK).withStreet("Strasse").withZipCode("12345").withCoordinates(coordinates).withHouseNumber("xy").build();
		person.assign(address);
		LoginContact login = new ContactBuilderFactoryImpl().eMailContactBuilder().withAccount("kinkyKylie@fever.net").build();
		person.assign(login);
		
		return  new CustomerImpl(person);
		
	}
	
}

