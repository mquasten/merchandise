package de.mq.merchandise.customer;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.CustomerRole;

public class CustomerRoleTest {
	
	@Test
	public final void size() {
		Assert.assertEquals(4, CustomerRole.values().length);
	}
	
	@Test
	public final void roles() {
		for(final CustomerRole role : CustomerRole.values()) {
			CustomerRole.valueOf(role.name());
		}
	}

}
