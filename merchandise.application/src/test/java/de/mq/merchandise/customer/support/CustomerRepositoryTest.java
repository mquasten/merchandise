package de.mq.merchandise.customer.support;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;

public class CustomerRepositoryTest {
	
	private static final Optional<Long> ID = Optional.of(19680528L);


	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	
	private  final CustomerRepository  customerRepository = new CustomerRepositoryImpl(); 
	
	@Before
	public final void setup() {
		ReflectionTestUtils.setField(customerRepository, "entityManager", entityManager);
	}
	
	@Test
	public final void customerById() {
	
		@SuppressWarnings("unchecked")
		final TypedQuery<Customer> typedQuery = Mockito.mock(TypedQuery.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(typedQuery.getSingleResult()).thenReturn(customer);
		Mockito.when(entityManager.createNamedQuery(CustomerRepository.CUSTOMER_BY_ID_QUERY, Customer.class)).thenReturn(typedQuery);
		Assert.assertEquals(customer, customerRepository.customerById(ID));
		
		Mockito.verify(typedQuery).setParameter(CustomerRepository.ID_PARAMETER, ID.get());
		Mockito.verify(entityManager).createNamedQuery(CustomerRepository.CUSTOMER_BY_ID_QUERY, Customer.class);
		Mockito.verify(typedQuery).getSingleResult();
	}

}
