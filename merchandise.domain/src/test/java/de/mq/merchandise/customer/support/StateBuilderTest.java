package de.mq.merchandise.customer.support;

import org.junit.Test;

import de.mq.merchandise.customer.StateBuilder;

import junit.framework.Assert;

public class StateBuilderTest {
	
	@Test
	public final void build() {
		Assert.assertFalse(new StateBuilderImpl().build().isActive());
		
	}
	
	@Test
	public final void forState() {
		final StateBuilder builder = new StateBuilderImpl();
		Assert.assertEquals(builder, builder.forState(true));
		Assert.assertTrue(builder.build().isActive());
		builder.forState(false);
		Assert.assertFalse(builder.build().isActive());
	}

}
