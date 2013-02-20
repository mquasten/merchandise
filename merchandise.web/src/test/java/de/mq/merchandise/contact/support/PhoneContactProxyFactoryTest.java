package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;

public class PhoneContactProxyFactoryTest {
	
	

	@Test
	public final void phone() {
	
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		final PhoneContactProxyFactoryImpl phoneContactProxyFactory = new PhoneContactProxyFactoryImpl();
		ReflectionTestUtils.setField(phoneContactProxyFactory, "webProxyFactory", webProxyFactory);
		final PhoneContactAO phoneContactAO=Mockito.mock(PhoneContactAO.class);
		Mockito.when(webProxyFactory.webModell(PhoneContactAO.class, PhoneContactImpl.class)).thenReturn(phoneContactAO);
		Assert.assertEquals(phoneContactAO, phoneContactProxyFactory.phone());
	}

}
