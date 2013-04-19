package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.model.support.WebProxyFactory;

public class LoginControllerFactoryTest {
	
    private WebProxyFactory webProxyFactory=Mockito.mock(WebProxyFactory.class);
	private CustomerService customerService=Mockito.mock(CustomerService.class);
	private FacesContextFactory facesContextFactory=Mockito.mock(FacesContextFactory.class);

	private LoginControllerFactoryImpl loginControllerFactory = new LoginControllerFactoryImpl();
	
	@Before
	public final void setup() {
		ReflectionTestUtils.setField(loginControllerFactory, "webProxyFactory", webProxyFactory);
		ReflectionTestUtils.setField(loginControllerFactory, "customerService", customerService);
		ReflectionTestUtils.setField(loginControllerFactory, "facesContextFactory", facesContextFactory);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void loginController() {
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> classCapturer = ArgumentCaptor.forClass(Class.class);
		final LoginControllerImpl loginController = Mockito.mock(LoginControllerImpl.class);
		final ArgumentCaptor<LoginControllerImpl> donmainCapturer = ArgumentCaptor.forClass(LoginControllerImpl.class);
		Mockito.when(webProxyFactory.webModell(classCapturer.capture(), donmainCapturer.capture())).thenReturn(loginController);
		Assert.assertEquals(loginController, loginControllerFactory.loginController());
		
		Assert.assertEquals(customerService, ReflectionTestUtils.getField(donmainCapturer.getValue(), "customerService"));
		Assert.assertEquals(facesContextFactory, ReflectionTestUtils.getField(donmainCapturer.getValue(), "facesContextFactory"));
		Assert.assertEquals(LoginControllerImpl.class, classCapturer.getValue());
	}

}