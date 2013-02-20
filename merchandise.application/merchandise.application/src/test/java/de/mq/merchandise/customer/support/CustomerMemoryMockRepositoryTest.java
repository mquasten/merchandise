package de.mq.merchandise.customer.support;

import java.util.Date;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.LegalForm;

public class CustomerMemoryMockRepositoryTest {
	
	@Test
	public final void id() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		Assert.assertEquals(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID, customer.id());
	}
	
	@Test
	public final void idNotFound() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		Assert.assertNull(customerRepository.forId(Math.round(Math.random()*1e20)));
	}
	
	
	@Test
	public final void storeLikeAVirgin(){
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = newCustomer();
		Assert.assertFalse(customer.hasId());
		customerRepository.store(customer);
		Assert.assertTrue(customer.hasId());
		
		Assert.assertEquals(customer, customerMap(customerRepository).get(customer.id()));
	}

	@SuppressWarnings("unchecked")
	private Map<Long, Customer> customerMap(final CustomerRepository customerRepository) {
		return (Map<Long, Customer>) ReflectionTestUtils.getField(customerRepository, "index");
	}

	private CustomerImpl newCustomer() {
		return new CustomerImpl(new LegalPersonImpl("Knowless-Music", "1234", new TradeRegisterImpl("41844", "Wegberg", "0815" , new Date()), LegalForm.AG, new Date()));
	}
	
	@Test
	public final void storeExisting() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(4711L);
		Mockito.when(customer.hasId()).thenReturn(true);
		customerRepository.store(customer);
		
		Assert.assertEquals(customer, customerMap(customerRepository).get(customer.id()));
		
	}

}
