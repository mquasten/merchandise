package de.mq.merchandise.customer.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.StateImpl;
import de.mq.merchandise.customer.support.UserRelation;
import de.mq.merchandise.customer.support.UserRelationImpl;

import de.mq.merchandise.util.EntityUtil;

public class CustomerTest {
	
	private static final long ID = 19680528L;

	@Test
	public final void id() {
		final BasicEntity basicEntity = newInvalidCustomer();
		ReflectionTestUtils.setField(basicEntity, "id", ID);
		Assert.assertEquals(ID, basicEntity.id());
	}

	private CustomerImpl newInvalidCustomer() {
		return EntityUtil.create(CustomerImpl.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNotPersistent() {
		 newInvalidCustomer().id();
	}
	
	@Test
	public final void person() {
		final Person person = Mockito.mock(Person.class);
		final Customer customer = new CustomerImpl(person);
		Assert.assertEquals(person, customer.person());
	}
	
	@Test()
	public final void personNoPersonAware() {
		Assert.assertNull(newInvalidCustomer().person());
	}
	
	@Test
	public final void state() {
		final Customer customer = newInvalidCustomer();
		final State state = Mockito.mock(StateImpl.class);
		ReflectionTestUtils.setField(customer, "state", state);
		Assert.assertEquals(state, customer.state());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void stateNull() {
		final Customer customer = newInvalidCustomer();
		ReflectionTestUtils.setField(customer, "state", null);
		customer.state();
	}
	
	@Test
	public final void grant() {
		final Customer customer = newCustomer();
		final Person person = Mockito.mock(Person.class);
		customer.grant(person, CustomerRole.Bids);
		
		@SuppressWarnings("unchecked")
		final Set<UserRelation> results = (Set<UserRelation>) ReflectionTestUtils.getField(customer, "userRelations");
		Assert.assertEquals(1, results.size());
		
		final UserRelation result =  results.iterator().next();
		Assert.assertEquals(person, result.person());
		Assert.assertEquals(customer, result.customer());
		Assert.assertEquals(1, result.roles().size());
		Assert.assertFalse(result.state().isActive());
		Assert.assertEquals(CustomerRole.Bids, result.roles().iterator().next());
		
	
	}
	
	
	@Test
	public final void grantPesonWithoutRoles() {
		final Customer customer = newCustomer();
		
		final Person person = Mockito.mock(Person.class);
		
		final UserRelation userRelation = new UserRelationImpl(customer, person);
		
		final Set<UserRelation> roles = new HashSet<>();
		roles.add(userRelation);
		ReflectionTestUtils.setField(customer, "userRelations", roles);
		
		@SuppressWarnings("unchecked")
		final Set<UserRelation> results = (Set<UserRelation>) ReflectionTestUtils.getField(customer, "userRelations");
		Assert.assertEquals(1, results.size());
		
		Assert.assertTrue(results.iterator().next().roles().isEmpty());
		
		customer.grant(person, CustomerRole.Bids);
		Assert.assertEquals(1, results.iterator().next().roles().size());
		Assert.assertEquals(CustomerRole.Bids, results.iterator().next().roles().iterator().next());
		
		
	
		
	}

	
	
	@Test
	public final void revoke() {
		final Customer customer = newInvalidCustomer();
		final Person person = Mockito.mock(Person.class);
		
		final Set<UserRelation> roles = new HashSet<UserRelation>();
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.customer()).thenReturn(customer);
		Mockito.when(userRelation.person()).thenReturn(person);
		Mockito.when(userRelation.isOwner(person)).thenReturn(true);
		
		roles.add(userRelation);
		ReflectionTestUtils.setField(customer, "userRelations", roles);
		
		customer.revoke(person, CustomerRole.Bids);
	
		Mockito.verify(userRelation).revoke(CustomerRole.Bids);
		
		
		
		
	}
	
	@Test
	public final void revokePerson() {
		final Customer customer = newInvalidCustomer();
		
		final Person person = Mockito.mock(Person.class);
		
		
		final Set<UserRelation> roles = new HashSet<>();
		
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.isOwner(person)).thenReturn(true);
		
		
		roles.add(userRelation);
		ReflectionTestUtils.setField(customer, "userRelations", roles);
		@SuppressWarnings("unchecked")
		final Set<UserRelation> results = (Set<UserRelation>) ReflectionTestUtils.getField(customer, "userRelations");
		Assert.assertEquals(1,results.size());
		Assert.assertEquals(userRelation, results.iterator().next());
		
		customer.revoke(person);
		
		Assert.assertTrue(results.isEmpty());
		Mockito.when(userRelation.isOwner(person)).thenReturn(false);
		
		//Mockito.verifyNoMoreInteractions(userRelation);
		customer.revoke(person);
	
	}
	
	@Test
	public final void activePersons() {
		final Customer customer = newCustomer();
		final Person person = newPerson();
		final State state = Mockito.mock(State.class);
		Mockito.when(state.isActive()).thenReturn(true);
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.customer()).thenReturn(customer);
		Mockito.when(userRelation.person()).thenReturn(person);
		Mockito.when(userRelation.state()).thenReturn(state);
		//userRelation.state().activate();
		
		
		
		Set<UserRelation> userRelations = new HashSet<>();
		userRelations.add(userRelation);
		
		ReflectionTestUtils.setField(customer, "userRelations", userRelations);
		
		Assert.assertEquals(1, customer.activePersons().size());
		Assert.assertEquals(person, customer.activePersons().iterator().next());
		
		//userRelation.state().deActivate();
		Mockito.when(state.isActive()).thenReturn(false);
		Assert.assertTrue(customer.activePersons().isEmpty());
	}
	
	@Test()
	public final void inActivePersons() {
		final Customer customer = newCustomer();
		final Person person = newPerson();
		
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.person()).thenReturn(person);
		final State state = Mockito.mock(State.class);
		Mockito.when(userRelation.state()).thenReturn(state);
		Set<UserRelation> userRelations = new HashSet<>();
		userRelations.add(userRelation);
		ReflectionTestUtils.setField(customer, "userRelations", userRelations );
		
		Assert.assertEquals(1, customer.inActivePersons().size());
		Assert.assertEquals(person, customer.inActivePersons().iterator().next());
		
		Mockito.when(state.isActive()).thenReturn(true);
		
		Assert.assertTrue(customer.inActivePersons().isEmpty());
		
		
	}
	@Test
	public final void persons() {
		final Customer customer = newCustomer();
		final Person person = newPerson();
		
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.person()).thenReturn(person);
		final State state = Mockito.mock(State.class);
		Mockito.when(userRelation.state()).thenReturn(state);
		Set<UserRelation> userRelations = new HashSet<>();
		userRelations.add(userRelation);
		ReflectionTestUtils.setField(customer, "userRelations", userRelations);
		
		
		final Map<Person,State> results = customer.persons();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(person, results.keySet().iterator().next());
		Assert.assertEquals(userRelation.state(), results.values().iterator().next());
		
	}

	private Person newPerson() {
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.id()).thenReturn(ID);
		return person;
	}

	private Customer newCustomer() {
		final Customer customer = newInvalidCustomer();
		ReflectionTestUtils.setField(customer, "id", ID);
		return customer;
	}

	
	
	@Test
	public final void roles() {
		final Customer customer = newInvalidCustomer();
		final Person person = Mockito.mock(Person.class);
		
		final Set<UserRelation> roles = new HashSet<UserRelation>();
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.isOwner(person)).thenReturn(true);
		
		final UserRelation otherUserRelation = Mockito.mock(UserRelation.class);
		
		
		final Set<CustomerRole> customerRoles = new HashSet<>();
		for(final CustomerRole customerRole : CustomerRole.values()){
			customerRoles.add(customerRole);
		}
		Mockito.when(userRelation.roles()).thenReturn(customerRoles);
		userRelation.grant(CustomerRole.values());
		roles.add(userRelation);
		roles.add(otherUserRelation);
		ReflectionTestUtils.setField(customer, "userRelations", roles);
		
	    Assert.assertEquals(CustomerRole.values().length, customer.roles(person).size());
	    for(final CustomerRole role : CustomerRole.values()){
	    	Assert.assertTrue(customer.roles(person).contains(role));
	    }
	    Mockito.when(userRelation.isOwner(person)).thenReturn(false);
	   
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void rolesPersonNotExists() {
		newInvalidCustomer().roles(Mockito.mock(Person.class));
	}
	
	@Test
	public final void hasRole() {
		final Customer customer = newInvalidCustomer();
		
		final Person person = Mockito.mock(Person.class);
		
		final Set<UserRelation> roles = new HashSet<UserRelation>();
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.isOwner(person)).thenReturn(true);
		Mockito.when(userRelation.hasRole(CustomerRole.Bids)).thenReturn(true);
		roles.add(userRelation);
		ReflectionTestUtils.setField(customer, "userRelations", roles);
		
		Assert.assertTrue(customer.hasRole(person, CustomerRole.Bids));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void hasRolePersonNotExists() {
		newInvalidCustomer().hasRole(Mockito.mock(Person.class), CustomerRole.Bids);
	}
	
	@Test
	public final void userRoleState() {
		final Customer customer = newInvalidCustomer();
		final State state = Mockito.mock(State.class);
		final Person person = Mockito.mock(Person.class);
		final UserRelation userRelation = Mockito.mock(UserRelation.class);
		Mockito.when(userRelation.person()).thenReturn(person);
		Mockito.when(userRelation.state()).thenReturn(state);
		Mockito.when(userRelation.isOwner(person)).thenReturn(true);
		
		
		customer.grant(person);
		Assert.assertFalse(customer.state(person).isActive());
		Assert.assertTrue(customer.activePersons().isEmpty());
		Mockito.when(state.isActive()).thenReturn(true);
		customer.state(person).activate();
		Assert.assertFalse(customer.activePersons().isEmpty());
	}
	
	@Test
	public final void hash() {
		final Person person = Mockito.mock(Person.class);
		
		Assert.assertTrue(person.hashCode() == new CustomerImpl(person).hashCode());
		
		Assert.assertFalse(newInvalidCustomer().hashCode() == newInvalidCustomer().hashCode());
	}
	
	@Test
	public final void equals() {
		
		final Person person = Mockito.mock(Person.class);
		
		Assert.assertTrue(new CustomerImpl(person).equals(new CustomerImpl(person)));
		Assert.assertFalse(new CustomerImpl(person).equals(new CustomerImpl(Mockito.mock(Person.class))));
		
		Assert.assertFalse(new CustomerImpl(person).equals(newInvalidCustomer()));
		Assert.assertFalse(newInvalidCustomer().equals(new CustomerImpl(person)));
		
		Assert.assertFalse(new CustomerImpl(person).equals("Kylie is nice"));
		
	}
	
	@Test
	public final void string() {
		final Person person = Mockito.mock(Person.class);
		Assert.assertEquals("person="+person.toString(), new CustomerImpl(person).toString());
		Assert.assertTrue(newInvalidCustomer().toString().startsWith(CustomerImpl.class.getName()+"@"));
	}
	
	@Test
	public final void hasId() {
		final Person person = Mockito.mock(Person.class);
		final CustomerImpl customer = new CustomerImpl(person);
		Assert.assertFalse(customer.hasId());
		ReflectionTestUtils.setField(customer, "id", ID);
		Assert.assertTrue(customer.hasId());
		
		
	}
}
