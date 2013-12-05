package de.mq.merchandise.rule.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;



import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;

public class RuleTest {
	
	private static final long ID = 19680528L;
	private static final String NAME = "calculate artists hotscore";
	
	private Customer customer = Mockito.mock(Customer.class);

	@Test
	public final void name() {
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertEquals(NAME, rule.name());
	}
	
	@Test
	public final void id() {
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertFalse(rule.hasId());
		EntityUtil.setId(rule, ID);
		Assert.assertTrue(rule.hasId());
		Assert.assertEquals(ID, rule.id());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNotAware() {
		 new RuleImpl(customer, NAME).id();
	}
	
	@Test
	public final void state(){
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertNotNull(rule.state());
		final State state = Mockito.mock(State.class);
		ReflectionTestUtils.setField(rule, "state", state);
		Assert.assertEquals(state, rule.state());
	}
	@Test
	public final void hash() {
		Assert.assertEquals(customer.hashCode() + NAME.hashCode(), new RuleImpl(customer, NAME).hashCode());
	}
	
	@Test
	public final void equals() {
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertTrue(rule.equals(new RuleImpl(customer, NAME)));
		Assert.assertFalse(rule.equals(new RuleImpl(customer, "dontLetMeGetMe")));
		Assert.assertFalse(rule.equals(new RuleImpl(Mockito.mock(Customer.class), NAME)));
	}
	
	@Test
	public final void string(){
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertEquals("name="+NAME, rule.toString());
	}
	
	@Test
	public final void customer() {
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertEquals(customer, rule.customer());
	}

}
