package de.mq.merchandise.customer.support;

import org.junit.Test;

import de.mq.merchandise.customer.support.CustomerBuilderFactoryImpl;
import de.mq.merchandise.customer.support.CustomerBuilderImpl;

import junit.framework.Assert;

public class CustomerBuilderFactoryTest {
	
	@Test
	public final void customerBuilder() {
		Assert.assertEquals(CustomerBuilderImpl.class, new CustomerBuilderFactoryImpl().customerBuilder().getClass());
	}

}
