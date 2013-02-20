package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.contact.ContactBuilderFactory;
import de.mq.merchandise.contact.support.AddressBuilderImpl;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.contact.support.CoordinatesBuilderImpl;
import de.mq.merchandise.contact.support.EMailContactBuilderImpl;
import de.mq.merchandise.contact.support.InstantMessengerContactBuilderImpl;
import de.mq.merchandise.contact.support.PhoneContactBuilderImpl;
import de.mq.merchandise.contact.support.PostBoxAddressBuilderImpl;

public class ContactBuilderFactoryTest {
	
	private final  ContactBuilderFactory contactBuilderFactory = new ContactBuilderFactoryImpl();
	
	@Test
	public final void address() {
		Assert.assertEquals(AddressBuilderImpl.class, contactBuilderFactory.addressBuilder().getClass());
	}
	
	@Test
	public final void postBox() {
		Assert.assertEquals(PostBoxAddressBuilderImpl.class, contactBuilderFactory.postBoxAddressBuilder().getClass());
	}
	
	@Test
	public final void eMail() {
		Assert.assertEquals(EMailContactBuilderImpl.class, contactBuilderFactory.eMailContactBuilder().getClass());
	}
	
	@Test
	public final void instantMessenger() {
		Assert.assertEquals(InstantMessengerContactBuilderImpl.class, contactBuilderFactory.instantMessengerContactBuilder().getClass());
	}
	
	@Test
	public final void phone() {
		Assert.assertEquals(PhoneContactBuilderImpl.class, contactBuilderFactory.phoneContactBuilder().getClass());
	}
	
	@Test
	public final void coordinates() {
		Assert.assertEquals(CoordinatesBuilderImpl.class, contactBuilderFactory.coordinatesBuilder().getClass());
	}

}
