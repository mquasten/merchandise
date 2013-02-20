package de.mq.merchandise.customer;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.PersonRole;


public class PersonRoleTest {
	
	@Test
	public final void personRoles() {
		Assert.assertEquals(4, PersonRole.values().length);
	}
	
	@Test
	public final void personRolesString() {
		for(PersonRole role : PersonRole.values()) {
			Assert.assertNotNull(PersonRole.valueOf(role.name()));
		}
	}

}
