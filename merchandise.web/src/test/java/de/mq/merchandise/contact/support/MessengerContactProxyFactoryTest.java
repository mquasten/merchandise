package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;

public class MessengerContactProxyFactoryTest {
	
	@Test
	public final void messenger() {
		
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		final MessengerContactProxyFactoryImpl messengerContactProxyFactory = new MessengerContactProxyFactoryImpl();
		ReflectionTestUtils.setField(messengerContactProxyFactory, "webProxyFactory", webProxyFactory);
		final MessengerContactAO messengerContactAO = Mockito.mock(MessengerContactAO.class);
		Mockito.when(webProxyFactory.webModell(MessengerContactAO.class, InstantMessengerContactImpl.class)).thenReturn(messengerContactAO);
		
		Assert.assertEquals(messengerContactAO, messengerContactProxyFactory.messenger());
	}

}
