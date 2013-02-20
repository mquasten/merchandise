package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.model.support.WebProxyFactory;

public class LegalPersonFactoryTest {
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void createLegalPerson() {
		final ArgumentCaptor<LegalPerson> argument2 = ArgumentCaptor.forClass(LegalPerson.class);
		
		
		
		final ArgumentCaptor<Class> argument1 = ArgumentCaptor.forClass(Class.class);
		
		
		final WebProxyFactory proxyFactory = Mockito.mock(WebProxyFactory.class);
		LegalPersonAO result = Mockito.mock(LegalPersonAO.class);
		Mockito.when(proxyFactory.webModell(argument1.capture(), argument2.capture())).thenReturn(result);
		final LegalPersonFactoryImpl legalPersonFactory = new LegalPersonFactoryImpl(proxyFactory);
		Assert.assertEquals(result, legalPersonFactory.legalPerson());
		Assert.assertEquals(LegalPersonAO.class, argument1.getValue());
		Assert.assertNotNull((((LegalPerson)argument2.getValue()).tradeRegister()));
	
	}
	
	@Test
	//TestCoverage only
	public final void createLegalPersonDEfaultConstructor() {
		Assert.assertNotNull(new LegalPersonFactoryImpl());
	}

}
