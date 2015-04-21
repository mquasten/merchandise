package de.mq.merchandise.customer.support;

import java.util.Optional;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;

public class CustomerServiceTest {
	
	private static final Optional<Long> ID = Optional.of(19680528L);

	private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
	
	private final CustomerService customerService = new CustomerServiceImpl(customerRepository);
	
	@Test
	public final void customer() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customerRepository.customerById(ID)).thenReturn(customer);
		Assert.assertEquals(customer, customerService.customer(ID));
		
	}

}
