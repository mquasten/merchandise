package de.mq.merchandise.customer.support;

import java.lang.reflect.Constructor;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.StateImpl;
import de.mq.merchandise.customer.support.UserRelation;
import de.mq.merchandise.customer.support.UserRelationImpl;
import de.mq.merchandise.util.EntityUtil;


public class UserRelationTest {
	
	private static final long ID = 19680528L;
	

	@Test
	public final void createForCustomerAndPerson() {
		final Person person = Mockito.mock(Person.class);
		final Customer customer = Mockito.mock(Customer.class);
		final UserRelationImpl userRelation = newUserRelation(customer, person);
		Assert.assertEquals(ID, userRelation.id());
		Assert.assertEquals(person, userRelation.person());
	}

	private UserRelationImpl newUserRelation(Customer customer,final Person  person) {
		return newUserRelation(customer, person, true);
	}
	
	private UserRelationImpl newUserRelation(Customer customer,final Person  person, final boolean withId) {
		final UserRelationImpl userRelation = new UserRelationImpl(customer, person);
		if( withId) {
				ReflectionTestUtils.setField(userRelation, "id", ID);
		}
		return userRelation;
	}
	
	
	
	
	@Test
	public final void grant() {
		final Person person = Mockito.mock(Person.class);
		final Customer customer = Mockito.mock(Customer.class);
		final UserRelation userRelation = newUserRelation(customer, person);
		userRelation.grant(CustomerRole.values());
		Assert.assertEquals(CustomerRole.values().length, userRelation.roles().size());
		for(final CustomerRole role : CustomerRole.values()){
			Assert.assertTrue(userRelation.roles().contains(role));
		}
	}
	
	@Test
	public final void revoke() {
		final Person person = Mockito.mock(Person.class);
		final Customer customer = Mockito.mock(Customer.class);
		final UserRelation userRelation = newUserRelation(customer, person);
		userRelation.grant(CustomerRole.Bids);
		Assert.assertTrue(userRelation.roles().contains(CustomerRole.Bids));
		
		userRelation.revoke(CustomerRole.Bids);
		Assert.assertFalse(userRelation.roles().contains(CustomerRole.Bids));
	}
	
	@Test
	public final void state() {
		final Person person = Mockito.mock(Person.class);
		final Customer customer = Mockito.mock(Customer.class);
		final UserRelation userRelation = newUserRelation(customer, person);
		final StateImpl state = Mockito.mock(StateImpl.class);
		ReflectionTestUtils.setField(userRelation, "state", state);
		
		Assert.assertEquals(state, userRelation.state());
	}
	
	
	
	@Test
	public final void hash() {
		final Customer customer = Mockito.mock(Customer.class);
		final Person person = Mockito.mock(Person.class);
		Assert.assertTrue(newUserRelation(customer, person).hashCode() == newUserRelation(customer, person).hashCode());
		Assert.assertFalse(newUserRelation(Mockito.mock(Customer.class), person, false).hashCode() == newUserRelation(customer, person).hashCode());
	    Assert.assertFalse(invalidUserRelation().hashCode() == invalidUserRelation().hashCode());
	    
	}

	private UserRelation invalidUserRelation()  {
		Constructor<UserRelationImpl> constructor;
		try {
			 constructor = UserRelationImpl.class.getDeclaredConstructor();
			 constructor.setAccessible(true);
			 return constructor.newInstance();
		} catch (final Exception ex) {
			throw new IllegalStateException("Error creating UserRelation" , ex );
		} 
	   
	   
	}
	@Test
	public final void equals() {
		final Customer customer = Mockito.mock(Customer.class);
		final Person person = Mockito.mock(Person.class);
		Assert.assertTrue(newUserRelation(customer, person).equals(newUserRelation(customer, person)));
		Assert.assertFalse(newUserRelation(Mockito.mock(Customer.class), person, false).equals(newUserRelation(customer, person)));
		Assert.assertFalse(newUserRelation(customer, person).equals(invalidUserRelation()));
		Assert.assertFalse(invalidUserRelation().equals(newUserRelation(customer, person)));
		Assert.assertFalse(invalidUserRelation().equals(invalidUserRelation()));
		Assert.assertFalse(newUserRelation(customer, person).equals("Kylie is nice"));
	}
	
	@Test
	public final void hasRole() {
		final UserRelation userRelation = newUserRelation(Mockito.mock(Customer.class), Mockito.mock(Person.class));
		Assert.assertFalse(userRelation.hasRole(CustomerRole.Bids));
		userRelation.grant(CustomerRole.Bids);
		Assert.assertTrue(userRelation.hasRole(CustomerRole.Bids));
	}
	
	@Test
	public final void customer() {
		final Customer customer = Mockito.mock(Customer.class);
		final UserRelation userRelation = newUserRelation(customer, Mockito.mock(Person.class));
		Assert.assertEquals(customer, userRelation.customer());
	}
	
	@Test
	public final void isOwner() {
		final Person person = Mockito.mock(Person.class);
		final UserRelation userRelation = newUserRelation(Mockito.mock(Customer.class), person);
		Assert.assertTrue(userRelation.isOwner(person));
		Assert.assertFalse(userRelation.isOwner(Mockito.mock(Person.class)));
	}
	
	@Test
	public final void hasId() {
		Assert.assertTrue(newUserRelation(Mockito.mock(Customer.class), Mockito.mock(Person.class)).hasId());
		Assert.assertFalse(EntityUtil.create(UserRelationImpl.class).hasId());
	}

}
