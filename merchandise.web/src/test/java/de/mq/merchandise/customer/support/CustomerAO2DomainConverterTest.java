package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;

public class CustomerAO2DomainConverterTest {
	
	@Test
	public final void convert() {
		final CustomerAO2DomainConverter converter = new CustomerAO2DomainConverter();
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		final Customer domain = Mockito.mock(Customer.class);
		Mockito.when(customerAO.getCustomer()).thenReturn(domain);
		Assert.assertEquals(domain, converter.convert(customerAO));
		Mockito.verify(customerAO).getCustomer();
	}
	
	@Test
	public final void convertNull() {
		final CustomerAO2DomainConverter converter = new CustomerAO2DomainConverter();
		Assert.assertNull(converter.convert(null));
	}

}
