package de.mq.merchandise.model.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.AddressSelector;
import de.mq.merchandise.contact.support.PostBoxAO;

public class AddressSelectorTest {
	
	
	private AddressSelector addressSelector = new AddressSelector();
	
	@Test
	public final void address() {
		Assert.assertEquals(AddressAO.class, addressSelector.convert(Mockito.mock(Address.class)));
	}
	
	@Test
	public final void cityAddress() {
		Assert.assertEquals(PostBoxAO.class, addressSelector.convert(Mockito.mock(CityAddress.class)));
	}

}
