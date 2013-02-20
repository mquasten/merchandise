package de.mq.merchandise.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.EMailContactAO;
import de.mq.merchandise.contact.support.MessengerContactAO;
import de.mq.merchandise.contact.support.PhoneContactAO;
import de.mq.merchandise.contact.support.PostBoxAO;
import de.mq.merchandise.model.ContactModelImpl;
import de.mq.merchandise.model.ContactModelImpl.Kind;

public class ContactModelTest {
	
	private ContactModelImpl contactModel;
	private final AddressAO addressAO = Mockito.mock(AddressAO.class);
	private final PostBoxAO postBoxAO = Mockito.mock(PostBoxAO.class);
	private PhoneContactAO phoneContactAO = Mockito.mock(PhoneContactAO.class);
	private EMailContactAO eMailContactAO = Mockito.mock(EMailContactAO.class);
	private MessengerContactAO messengerContactAO = Mockito.mock(MessengerContactAO.class);
	
	@Before
	public final void init() {
		contactModel=new ContactModelImpl(addressAO,postBoxAO,phoneContactAO,eMailContactAO,messengerContactAO);
	}
	
	
	@Test
	public final void addressType() {
		Assert.assertEquals(ContactModelImpl.Kind.Address.name(), contactModel.getAddressType());
		contactModel.setAddressType(ContactModelImpl.Kind.PostBox.name());
		Assert.assertEquals(ContactModelImpl.Kind.PostBox.name(), contactModel.getAddressType());
	}
	@Test
	public final void contactType() {
		Assert.assertEquals(ContactModelImpl.Kind.Phone.name(), contactModel.getContactType());
		contactModel.setContactType(ContactModelImpl.Kind.EMail.name());
		Assert.assertEquals(ContactModelImpl.Kind.EMail.name(), contactModel.getContactType());
	}
	
	@Test
	public final void address() {
		Assert.assertEquals(addressAO, contactModel.getAddress());
		contactModel.setAddressType(ContactModelImpl.Kind.PostBox.name());
		Assert.assertEquals(postBoxAO, contactModel.getAddress());
	}
	
	@Test
	public final void contact(){
		Assert.assertEquals(phoneContactAO, contactModel.getContact());
		contactModel.setContactType(ContactModelImpl.Kind.Messenger.name());
		Assert.assertEquals(messengerContactAO, contactModel.getContact());
	}
	
	@Test
	public final void kind() {
		for(final Kind kind : Kind.values()) {
			Assert.assertEquals(kind, Kind.valueOf(kind.name()));
		}
	}

}
