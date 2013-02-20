package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.NativityBuilderFactory;
import de.mq.merchandise.customer.support.NativityBuilderFactoryImpl;
import de.mq.merchandise.customer.support.NativityBuilderImpl;

public class NativityBuilderFactoryTest {
	
	private final NativityBuilderFactory nativityBuilderFactory = new NativityBuilderFactoryImpl();
	
	@Test
	public final void create() {
		Assert.assertEquals(NativityBuilderImpl.class, nativityBuilderFactory.nativityBuilder().getClass());
	}
	

}
