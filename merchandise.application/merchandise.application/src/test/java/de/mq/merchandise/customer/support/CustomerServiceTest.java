package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.support.CustomerRepository;
import de.mq.merchandise.customer.support.CustomerServiceImpl;


public class CustomerServiceTest {

	private static final String LOGIN = "skype:singleLadies";
	private static final long ID = 19680528L;

	@Test
	public final void registerUser() {
		final State state = Mockito.mock(State.class);
		final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.hasId()).thenReturn(true);
		Mockito.when(customer.id()).thenReturn(ID);
		Mockito.when(customerRepository.forId(ID)).thenReturn(customer);
		
		
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.state()).thenReturn(state);
		
		final CustomerService customerService = new CustomerServiceImpl(customerRepository);
		customerService.register(customer, person);
		
		Mockito.verify(customer).grant(person, CustomerRole.Opportunities, CustomerRole.Bids, CustomerRole.Demands);
		Mockito.verify(customerRepository).store(customer);
		Mockito.verify(state).activate();
		
	}
	
	
	@Test
	public final void registerCustomer() {
		final State personState = Mockito.mock(State.class);
		
		final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		final Customer customer = Mockito.mock(Customer.class);
		 ArgumentCaptor<Customer> newCustomer = ArgumentCaptor.forClass(Customer.class);
		Mockito.when(customer.hasId()).thenReturn(false);
		
		
		
		final CustomerService customerService = new CustomerServiceImpl(customerRepository);
		
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.state()).thenReturn(personState);
		
		customerService.register(customer, person);
		
		Mockito.verify(customerRepository).store(newCustomer.capture());
		Assert.assertEquals(person, newCustomer.getValue().person());
		Assert.assertTrue(newCustomer.getValue().state().isActive());
		
	    final List<CustomerRole> roles = newCustomer.getValue().roles(person);
		Assert.assertEquals(4, roles.size());
		Assert.assertTrue(roles.contains(CustomerRole.Opportunities));
		Assert.assertTrue(roles.contains(CustomerRole.Bids));
		Assert.assertTrue(roles.contains(CustomerRole.Demands));
	    Mockito.verify(personState).activate();
	    Assert.assertTrue(newCustomer.getValue().state(person).isActive());
	}
	
	@Test
	public final void customerExists() {
		final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		final Customer customer = Mockito.mock(Customer.class);
		final CustomerService customerService = new CustomerServiceImpl(customerRepository);
		Mockito.when(customerRepository.forId(ID)).thenReturn(customer);
		Assert.assertEquals(customer, customerService.customer(ID));
		
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void customerNotFound() {
		final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		final CustomerService customerService = new CustomerServiceImpl(customerRepository);
		customerService.customer(ID);
	}
	
	@Test
	public final void login() {
		final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		final CustomerService customerService = new CustomerServiceImpl(customerRepository);
		@SuppressWarnings("unchecked")
		final Entry<Customer,Person> entry = Mockito.mock(Entry.class);
		final Collection<Entry<Customer,Person>> entries = new ArrayList<>();
		entries.add(entry);
		Mockito.when(customerRepository.forLogin(LOGIN)).thenReturn(entries);
	    Assert.assertEquals(entries, customerService.login(LOGIN));
		
	}
	
	
}
