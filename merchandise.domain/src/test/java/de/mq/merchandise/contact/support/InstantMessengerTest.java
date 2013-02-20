package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.contact.InstantMessenger;

public class InstantMessengerTest {
	
	@Test
	public final void size() {
		Assert.assertEquals(8, InstantMessenger.values().length);
	}
	
	@Test
	public final void instantMessenger(){
		for(final InstantMessenger instantMessenger : InstantMessenger.values()) {
			Assert.assertNotNull(InstantMessenger.valueOf(instantMessenger.name()));
		}
	}

}
