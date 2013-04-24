package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.PersonRole;
import de.mq.merchandise.customer.State;

public class UserCustomerAuthentificationTest {
	
	
	private static final String NAME = "Minogue";
	private final Person person = Mockito.mock(Person.class);
	private final State personState = Mockito.mock(State.class);
	private final Customer customer = Mockito.mock(Customer.class);
	private final State customerState = Mockito.mock(State.class);
	private final Customer newCustomer = Mockito.mock(Customer.class);
	private final Set<PersonRole> personRoles = new HashSet<>();
	private final List<CustomerRole> customerRoles = new ArrayList<>();
	private final Set<Customer> customers = new HashSet<>();
	
	
	
	
	@Before
	public final void setup() {
		Mockito.when(personState.isActive()).thenReturn(true);
		Mockito.when(person.state()).thenReturn(personState);
		Mockito.when(customerState.isActive()).thenReturn(true);
		Mockito.when(customer.state()).thenReturn(customerState);
		personRoles.add(PersonRole.Catalogs);
		Mockito.when(person.roles()).thenReturn(personRoles);
		customerRoles.add(CustomerRole.Bids);
		Mockito.when(customer.roles(person)).thenReturn(customerRoles);
		customers.add(customer);
		customers.add(newCustomer);
		Mockito.when(person.customers()).thenReturn(customers);
	}
	
	
	@Test
	public final void create() {
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		Assert.assertEquals(person, authentification.getPrincipal());
		Assert.assertEquals(customer, authentification.getDetails());
		
	}
	
	@Test(expected=AuthenticationServiceException.class)
	public final void userNotActive() {
		Mockito.when(personState.isActive()).thenReturn(false);
		new PersonCustomerAuthentificationImpl(person, customer);
	}
	
	@Test(expected=AuthenticationServiceException.class)
	public final void customerrNotActive() {
		Mockito.when(customerState.isActive()).thenReturn(false);
		new PersonCustomerAuthentificationImpl(person, customer);
	}
	
	@Test
	public final void getName() {
		Mockito.when(person.name()).thenReturn(NAME);
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		Assert.assertEquals(NAME, authentification.getName());
	}
	
	@Test
	public final void getAuthorities() {
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		final Collection<? extends GrantedAuthority> result = authentification.getAuthorities();
		Assert.assertEquals(2, result.size());
		int catalogCounter=0;
		int bidsCounter=0;
		for(final GrantedAuthority authority : result){
			if( authority.getAuthority().equals(PersonRole.Catalogs.name())){
				catalogCounter++;
				continue;
			}
			if( authority.getAuthority().equals(CustomerRole.Bids.name())){
				bidsCounter++;
				continue;
			}
			Assert.fail("Wrong Authority: " + authority.getAuthority());
		}
		Assert.assertEquals(1, catalogCounter);
		Assert.assertEquals(1, bidsCounter);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public final void getCredentials() {
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		authentification.getCredentials();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public final void setAuthenticated() {
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		authentification.setAuthenticated(true);
	}
	
	@Test
	public final void isAuthenticated() {
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		Assert.assertTrue(authentification.isAuthenticated());
	}
	
	@Test
	public final void setDetails() {
		
		Mockito.when(newCustomer.hasUser(person)).thenReturn(true);
		Mockito.when(newCustomer.state()).thenReturn(customerState);
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		Assert.assertEquals(customer, authentification.getDetails());
		authentification.setDetails(newCustomer);
		Assert.assertEquals(newCustomer, authentification.getDetails());
	}
	
	@Test(expected=SecurityException.class)
	public final void setDetailsWrongCustomer() {
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		authentification.setDetails(newCustomer);
	}
	
	@Test
	public final void getCustomers() {
		final PersonCustomerAuthentification authentification = new PersonCustomerAuthentificationImpl(person, customer); 
		Assert.assertEquals(customers, authentification.getCustomers());
	}

}
