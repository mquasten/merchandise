package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;

public class AddressProxyFactoryTest {
	
	
	@Test
	public final void address() {
		final AddressProxyFactoryImpl addressProxyFactory = new AddressProxyFactoryImpl();
	
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		ReflectionTestUtils.setField(addressProxyFactory, "webProxyFactory", webProxyFactory);
		final AddressAO addressAO = Mockito.mock(AddressAO.class);
		Mockito.when(webProxyFactory.webModell(AddressAO.class, AddressImpl.class)).thenReturn(addressAO);
		Assert.assertEquals(addressAO, addressProxyFactory.address());
	}

}
