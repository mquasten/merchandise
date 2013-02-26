package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


import de.mq.merchandise.contact.GeocodingService;
import de.mq.merchandise.model.support.WebProxyFactory;

public class PersonControllerFactoryTest {
	@SuppressWarnings("unchecked")
	@Test
	public final void createProxy() {
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		
		final PersonControllerImpl personController = Mockito.mock(PersonControllerImpl.class);
		final PersonControllerFactory personControllerFactory = new PersonControllerFactory(Mockito.mock(GeocodingService.class), webProxyFactory);
		
		
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> clazzArgumentCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<PersonControllerImpl> personControllerArgumentCaptor = ArgumentCaptor.forClass(PersonControllerImpl.class);
		Mockito.when(webProxyFactory.webModell(clazzArgumentCaptor.capture(), personControllerArgumentCaptor.capture())).thenReturn(personController);
		
		
		Assert.assertEquals(personController, personControllerFactory.personController());
		Assert.assertEquals(PersonControllerImpl.class, clazzArgumentCaptor.getValue());
		Assert.assertEquals(PersonControllerImpl.class, personControllerArgumentCaptor.getValue().getClass());
		
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new PersonControllerFactory());
	}

}
