package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.support.EMailContactAO;
import de.mq.merchandise.contact.support.EMailContactImpl;
import de.mq.merchandise.contact.support.InstantMessengerContactImpl;
import de.mq.merchandise.contact.support.MessengerContactAO;
import de.mq.merchandise.contact.support.PhoneContactAO;
import de.mq.merchandise.contact.support.PhoneContactImpl;

public class ContactSelectorTest {
	
	private ContactSelector contactSelector = new ContactSelector();
	
	@Test
	public final void phoneContact() {
		Assert.assertEquals(PhoneContactAO.class, contactSelector.convert(Mockito.mock(PhoneContactImpl.class)));
	}
	
	
	@Test
	public final void emailContact() {
		Assert.assertEquals(EMailContactAO.class, contactSelector.convert(Mockito.mock(EMailContactImpl.class)));
	}
	
	@Test
	public final void messengerContact() {
		Assert.assertEquals(MessengerContactAO.class, contactSelector.convert(Mockito.mock(InstantMessengerContactImpl.class)));
	}

}
