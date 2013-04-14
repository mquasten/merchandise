package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.model.support.Conversation;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.util.ValidationService;

public class RegistrationWizardControllerFactoryTest {
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void registrationWizardController() {
		
		final CustomerService customerService = Mockito.mock(CustomerService.class);
		final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
		final ValidationService validationService = Mockito.mock(ValidationService.class);
		final Conversation conversation = Mockito.mock(Conversation.class);
		
		final RegistrationWizardControllerFactory registrationWizardController = new RegistrationWizardControllerFactory();
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		ArgumentCaptor<RegistrationWizardControllerImpl> registrationWizadControllerArgumentCaptor = ArgumentCaptor.forClass(RegistrationWizardControllerImpl.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> clazzCaptor = ArgumentCaptor.forClass(Class.class);
		RegistrationWizardControllerImpl proxy = new RegistrationWizardControllerImpl();
		Mockito.when(webProxyFactory.webModell(clazzCaptor.capture(), registrationWizadControllerArgumentCaptor.capture())).thenReturn(proxy);
		
		
		ReflectionTestUtils.setField(registrationWizardController, "webProxyFactory", webProxyFactory);
		ReflectionTestUtils.setField(registrationWizardController, "beanResolver", beanResolver);
		ReflectionTestUtils.setField(registrationWizardController, "customerService", customerService);
		ReflectionTestUtils.setField(registrationWizardController, "validationService", validationService);
		ReflectionTestUtils.setField(registrationWizardController, "conversation", conversation);
		
		Assert.assertEquals(proxy, registrationWizardController.registrationWizardController());
		Assert.assertEquals(RegistrationWizardControllerImpl.class, clazzCaptor.getValue());
		Assert.assertEquals(RegistrationWizardControllerImpl.class, registrationWizadControllerArgumentCaptor.getValue().getClass());
		
		final RegistrationWizardControllerImpl model = registrationWizadControllerArgumentCaptor.getValue();
		Assert.assertEquals(customerService, ReflectionTestUtils.getField(model, "customerService"));
		Assert.assertEquals(beanResolver, ReflectionTestUtils.getField(model, "beanResolver"));
		Assert.assertEquals(validationService, ReflectionTestUtils.getField(model, "validationService"));
		Assert.assertEquals(conversation, ReflectionTestUtils.getField(model, "conversation"));
	
		
		
	}
}