package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.support.StateImpl;

public class StateTest {
	
	@Test
	public final void defaultState() {
		final State state = new StateImpl();
		Assert.assertFalse(state.isActive());
	}
	
	@Test
	public final void activate() {
		final State state = new StateImpl();
		state.activate();
		Assert.assertTrue(state.isActive());
	}
	
	@Test
	public final void constructor() {
		final State state = new StateImpl(true);
		Assert.assertTrue(state.isActive());
	}
	
	@Test
	public final void deactivate() {
		final State state = new StateImpl(true);
		state.deActivate();
		Assert.assertFalse(state.isActive());
	}
	
	@Test
	public final void hash() {
		Assert.assertEquals(Boolean.FALSE.hashCode(), new StateImpl(false).hashCode());
		Assert.assertEquals(Boolean.TRUE.hashCode(), new StateImpl(true).hashCode());
	}
	
	@Test
	public final void equals() {
		Assert.assertTrue(new StateImpl(false).equals(new StateImpl(false)));
		Assert.assertFalse(new StateImpl(false).equals(new StateImpl(true)));
		Assert.assertFalse(new StateImpl(false).equals("dontLetMeGetMe"));
	}

}
