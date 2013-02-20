package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;

public class CustomerFactoryTest {
	
	
	@Test
	public final void createProxy() {
		
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		Mockito.when(webProxyFactory.webModell(CustomerAO.class, CustomerImpl.class)).thenReturn(customerAO);
		final CustomerProxyFactoryImpl customerProxyFactory = new CustomerProxyFactoryImpl();
		ReflectionTestUtils.setField(customerProxyFactory, "webProxyFactory", webProxyFactory);
		Assert.assertEquals(customerAO, customerProxyFactory.customer());
		
		
	}
	
	@Test
	public final void createBuilder() {
		final CustomerProxyFactoryImpl customerProxyFactory = new CustomerProxyFactoryImpl();
		Assert.assertEquals(CustomerBuilderFactoryImpl.class, customerProxyFactory.customerBuilderFactory().getClass());
	}
	
	
	@Test
	public final void coverageOnly() {
		Assert.assertNotNull(new CustomerProxyFactoryImpl());
	}

}
