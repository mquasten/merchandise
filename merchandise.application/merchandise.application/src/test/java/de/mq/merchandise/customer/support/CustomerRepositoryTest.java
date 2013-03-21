package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerRepository;
import de.mq.merchandise.customer.support.CustomerRepositoryImpl;

public class CustomerRepositoryTest {
	
	private static final String PERSON_ID_PARAMETER = "personId";
	private static final String LOGIN_PARAMETER = "login";
	private static final long BEYONCES_ID = 4711L;
	private static final String BEYONCES_LOGIN = "skype:singleLadies";


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
	

	@Test
	public final void register() {
		final EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		final TypedQuery<Person> typedPersonQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(entityManager.createNamedQuery(CustomerRepository.PERSON_FOR_LOGIN, Person.class)).thenReturn(typedPersonQuery);
		Mockito.when(typedPersonQuery.setParameter(LOGIN_PARAMETER, BEYONCES_LOGIN)).thenReturn(typedPersonQuery);
		final List<Person> logins = new ArrayList<>();
		final Person login = Mockito.mock(Person.class);
		Mockito.when(login.id()).thenReturn(BEYONCES_ID);
		logins.add(login);
		Mockito.when(typedPersonQuery.getResultList()).thenReturn(logins);
		@SuppressWarnings("unchecked")
		final TypedQuery<Customer> typedCustomerQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(entityManager.createNamedQuery(CustomerRepository.CUSTOMER_FOR_PERSON, Customer.class)).thenReturn(typedCustomerQuery);
		Mockito.when(typedCustomerQuery.setParameter(PERSON_ID_PARAMETER, BEYONCES_ID)).thenReturn(typedCustomerQuery);
		final List<Customer> customers = new ArrayList<>();
		final Customer customer = Mockito.mock(Customer.class);
		customers.add(customer);
		Mockito.when(typedCustomerQuery.getResultList()).thenReturn(customers);
		
		
		final CustomerRepository customerRepository = new  CustomerRepositoryImpl(entityManager);
		
		final Collection<Entry<Customer,Person>> results = customerRepository.forLogin(BEYONCES_LOGIN);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(customer, results.iterator().next().getKey());
		Assert.assertEquals(login, results.iterator().next().getValue());
		
		
		Mockito.verify(entityManager).createNamedQuery(CustomerRepository.PERSON_FOR_LOGIN, Person.class);
		Mockito.verify(typedPersonQuery).setParameter(LOGIN_PARAMETER, BEYONCES_LOGIN);
		Mockito.verify(typedPersonQuery).getResultList();
		
		Mockito.verify(entityManager).createNamedQuery(CustomerRepository.CUSTOMER_FOR_PERSON, Customer.class);
		Mockito.verify(typedCustomerQuery).setParameter(PERSON_ID_PARAMETER, BEYONCES_ID);
		Mockito.verify(typedCustomerQuery).getResultList();
	}
}
