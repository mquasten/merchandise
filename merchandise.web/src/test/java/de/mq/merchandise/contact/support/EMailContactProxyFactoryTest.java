package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;

public class EMailContactProxyFactoryTest {
	@Test
	public final void email() {
		
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		final EMailContactProxyFactoryImpl eMailContactProxyFactory = new EMailContactProxyFactoryImpl();
		ReflectionTestUtils.setField(eMailContactProxyFactory, "webProxyFactory", webProxyFactory);
		EMailContactAO eMailContactAO = Mockito.mock(EMailContactAO.class);
		Mockito.when(webProxyFactory.webModell(EMailContactAO.class, EMailContactImpl.class)).thenReturn(eMailContactAO);
		Assert.assertEquals(eMailContactAO, eMailContactProxyFactory.email());
	}

}
