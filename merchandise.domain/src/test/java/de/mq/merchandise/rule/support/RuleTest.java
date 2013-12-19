package de.mq.merchandise.rule.support;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;



import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.opportunity.support.Resource;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;

public class RuleTest {
	
	private static final String SOURCE_NAME = "source.groovy";
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
	
	
	@Test
	public final void documents() {
		final Rule rule = new RuleImpl(customer, NAME);
		ReflectionTestUtils.setField(rule, "source", SOURCE_NAME);
		ReflectionTestUtils.setField(rule, "id", ID);
		
		final Map<String,String> results = rule.documents();
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(SOURCE_NAME, results.keySet().iterator().next());
		Assert.assertEquals(String.format("%s/%s/%s", Resource.Source.urlPart() , ID,SOURCE_NAME ), results.values().iterator().next());
		
	}
	
	@Test
	public final void documentsNotAware() {
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertTrue(rule.documents().isEmpty());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public final void assignWebLink() {
		final Rule rule = new RuleImpl(customer, NAME);
		rule.assignWebLink(SOURCE_NAME);
	}
	
	@Test
	public final void assignDocument() {
		final Rule rule = new RuleImpl(customer, NAME);
		rule.assignDocument(SOURCE_NAME);
		
		Assert.assertEquals(SOURCE_NAME, ReflectionTestUtils.getField(rule, "source"));
	}
	
	@Test
	public final void removeDocument() {
		final Rule rule = new RuleImpl(customer, NAME);
		ReflectionTestUtils.setField(rule, "source", SOURCE_NAME);
		Assert.assertEquals(SOURCE_NAME, ReflectionTestUtils.getField(rule, "source"));
		
		rule.removeDocument("dontLetMeGetMe");
		
		Assert.assertNull(ReflectionTestUtils.getField(rule, "source"));
		
	}
	
	@Test
	public final void urlForName() {
		final Rule rule = new RuleImpl(customer, NAME);
		ReflectionTestUtils.setField(rule, "source", SOURCE_NAME );
		ReflectionTestUtils.setField(rule, "id", ID);
		
		Assert.assertEquals(String.format("%s/%s/%s", Resource.Source.urlPart() , ID,SOURCE_NAME ), rule.urlForName("dontLetMeGetMe"));
		
		
	}
	
	
	@Test
	public final void urlForNameSourceNotAware() {
		final Rule rule = new RuleImpl(customer, NAME);
		Assert.assertNull(rule.urlForName(SOURCE_NAME));
		
	}

}
