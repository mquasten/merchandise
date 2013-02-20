package de.mq.merchandise.customer.support;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerRepository;
import de.mq.merchandise.customer.support.CustomerServiceImpl;


public class CustomerServiceTest {

	private static final long ID = 19680528L;

	@Test
	public final void registerUser() {
		final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.hasId()).thenReturn(true);
		Mockito.when(customer.id()).thenReturn(ID);
		Mockito.when(customerRepository.forId(ID)).thenReturn(customer);
		
		final Person person = Mockito.mock(Person.class);
		
		final CustomerService customerService = new CustomerServiceImpl(customerRepository);
		customerService.register(customer, person);
		
		Mockito.verify(customer).grant(person, CustomerRole.Opportunities, CustomerRole.Bids, CustomerRole.Demands);
		Mockito.verify(customerRepository).store(customer);
		
	}
	
	
	@Test
	public final void registerCustomer() {
		final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		final Customer customer = Mockito.mock(Customer.class);
		 ArgumentCaptor<Customer> argument = ArgumentCaptor.forClass(Customer.class);
		Mockito.when(customer.hasId()).thenReturn(false);
		
		
		
		final CustomerService customerService = new CustomerServiceImpl(customerRepository);
		
		final Person person = Mockito.mock(Person.class);
		
		customerService.register(customer, person);
		
		Mockito.verify(customerRepository).store(argument.capture());
		Assert.assertEquals(person, argument.getValue().person());
		
	    final List<CustomerRole> roles = argument.getValue().roles(person);
		Assert.assertEquals(3, roles.size());
		Assert.assertTrue(roles.contains(CustomerRole.Opportunities));
		Assert.assertTrue(roles.contains(CustomerRole.Bids));
		Assert.assertTrue(roles.contains(CustomerRole.Demands));
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
	
	
}
