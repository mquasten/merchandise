package de.mq.merchandise.model.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;


import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.AddressTestConstants;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.model.support.WebProxyFactoryImpl;

public class WebProxyFactoryTest {
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void  webModel() {
	
		final AOProxyFactory  proxyFactory = Mockito.mock(AOProxyFactory.class);
		final AddressAO result = Mockito.mock(AddressAO.class);
		final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
		
		
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> clazzCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> addressCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		Mockito.when(proxyFactory.createProxy(clazzCaptor.capture(), addressCaptor.capture())).thenReturn(result);
		final WebProxyFactory webProxyFactoty = new WebProxyFactoryImpl(proxyFactory, beanResolver);
		Assert.assertEquals(result, webProxyFactoty.webModell(AddressAO.class, AddressTestConstants.ADDRESS_CLASS));
		Assert.assertEquals("ModelRepositoryImpl", addressCaptor.getValue().getClass().getSimpleName());
		
		Assert.assertEquals(AddressTestConstants.ADDRESS_CLASS, addressCaptor.getValue().get(AddressTestConstants.ADDRESS_CLASS).getClass());
		Assert.assertEquals(AddressAO.class,clazzCaptor.getValue());
	}

}
