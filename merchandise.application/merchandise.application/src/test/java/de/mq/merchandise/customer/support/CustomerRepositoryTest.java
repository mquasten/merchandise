package de.mq.merchandise.customer.support;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerRepository;
import de.mq.merchandise.customer.support.CustomerRepositoryImpl;

public class CustomerRepositoryTest {
	
	@Test
	public final void store() {
		final Customer customer = Mockito.mock(Customer.class);
		final EntityManager entityManager = Mockito.mock(EntityManager.class);
		final CustomerRepository customerRepository = new  CustomerRepositoryImpl(entityManager);
		
		Mockito.when(entityManager.merge(customer)).thenReturn(customer);
		
		Assert.assertEquals(customer, customerRepository.store(customer));
		
		Mockito.verify(entityManager).merge(customer);
		
		
	}
	
	@Test
	public final void forId() {
		final Customer customer = Mockito.mock(Customer.class);
		final EntityManager entityManager = Mockito.mock(EntityManager.class);
		final CustomerRepository customerRepository = new  CustomerRepositoryImpl(entityManager);
		
		Mockito.when(customerRepository.forId(19680528L)).thenReturn(customer);
		
		Assert.assertEquals(customer, customerRepository.forId(19680528L));
		
		
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new CustomerRepositoryImpl());
	}
	

}
