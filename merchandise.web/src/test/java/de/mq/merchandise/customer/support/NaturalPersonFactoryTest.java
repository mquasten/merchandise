package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.model.support.WebProxyFactory;

public class NaturalPersonFactoryTest {
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void naturalPerson() {
		
		final WebProxyFactory proxyFactory = Mockito.mock(WebProxyFactory.class);
		final NaturalPersonFactoryImpl naturalPersonFactory = new NaturalPersonFactoryImpl(proxyFactory);
		final NaturalPersonAO result = Mockito.mock(NaturalPersonAO.class);
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> classCapture = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<NaturalPerson> domainCapture = ArgumentCaptor.forClass(NaturalPerson.class);
		Mockito.when(proxyFactory.webModell(classCapture.capture(), domainCapture.capture())).thenReturn(result);
		
		Assert.assertEquals(result, naturalPersonFactory.naturalPerson());
		
		Assert.assertEquals(NaturalPersonAO.class, classCapture.getValue());
		Assert.assertEquals(NaturalPersonImpl.class, domainCapture.getValue().getClass());
		Assert.assertNotNull(domainCapture.getValue().nativity());
	}
	
	@Test
	// for coverage only
	public final void defaultConstructor() {
		Assert.assertNotNull(new NaturalPersonFactoryImpl());
	}

}
