package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;

public class LoginFactoryTest {
	
	
	private LoginFactoryImpl loginFactory = new LoginFactoryImpl();
	
    private AOProxyFactory proxyFactory = Mockito.mock(AOProxyFactory.class) ;
	private BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void login() {
		ReflectionTestUtils.setField(loginFactory, "proxyFactory", proxyFactory);
		ReflectionTestUtils.setField(loginFactory, "beanResolver", beanResolver);
		ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		ArgumentCaptor<ModelRepository> modelRepositoryCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		final LoginAO result = Mockito.mock(LoginAO.class);
		Mockito.when(proxyFactory.createProxy(classCaptor.capture(), modelRepositoryCaptor.capture())).thenReturn(result);
		
		Assert.assertEquals(result, loginFactory.login());
		Assert.assertEquals(beanResolver,  ReflectionTestUtils.getField(modelRepositoryCaptor.getValue(), "beanResolver"));
		Assert.assertEquals(LoginAO.class, classCaptor.getValue());
	}

}
