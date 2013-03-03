package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.model.support.WebProxyFactory;

public class RegistrationWizardControllerFactoryTest {
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void registrationWizardController() {
		
		final CustomerService customerService = Mockito.mock(CustomerService.class);
		final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
		Mockito.when(applicationContext.getBean(CustomerService.class)).thenReturn(customerService);
		
		final RegistrationWizardControllerFactory registrationWizardController = new RegistrationWizardControllerFactory();
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		ArgumentCaptor<RegistrationWizardControllerImpl> registrationWizadControllerArgumentCaptor = ArgumentCaptor.forClass(RegistrationWizardControllerImpl.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> clazzCaptor = ArgumentCaptor.forClass(Class.class);
		RegistrationWizardControllerImpl proxy = new RegistrationWizardControllerImpl();
		Mockito.when(webProxyFactory.webModell(clazzCaptor.capture(), registrationWizadControllerArgumentCaptor.capture())).thenReturn(proxy);
		
		
		ReflectionTestUtils.setField(registrationWizardController, "webProxyFactory", webProxyFactory);
		ReflectionTestUtils.setField(registrationWizardController, "applicationContext", applicationContext);
		
		
		
		Assert.assertEquals(proxy, registrationWizardController.registrationWizardController());
		Assert.assertEquals(RegistrationWizardControllerImpl.class, clazzCaptor.getValue());
		Assert.assertEquals(RegistrationWizardControllerImpl.class, registrationWizadControllerArgumentCaptor.getValue().getClass());
		
		final RegistrationWizardControllerImpl model = registrationWizadControllerArgumentCaptor.getValue();
		Assert.assertEquals(customerService, ReflectionTestUtils.getField(model, "customerService"));
		Assert.assertEquals(applicationContext, ReflectionTestUtils.getField(model, "applicationContext"));
		
		Assert.assertEquals(customerService, ReflectionTestUtils.getField(proxy, "customerService"));
		Assert.assertEquals(applicationContext, ReflectionTestUtils.getField(proxy, "applicationContext"));
		
	}

}
