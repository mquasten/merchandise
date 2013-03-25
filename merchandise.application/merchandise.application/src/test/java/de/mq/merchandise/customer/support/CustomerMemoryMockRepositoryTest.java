package de.mq.merchandise.customer.support;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.Person;

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
		return new CustomerImpl(new LegalPersonImpl("Knowless-Music", "1234", new TradeRegisterImpl("41844", "Wegberg", "0815" ), LegalForm.AG, new Date()));
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
	
	@Test
	public final void forLogin() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		
		final Collection<Entry<Customer,Person>> result = customerRepository.forLogin(CustomerMemoryReposioryMock.DEFAULT_LOGIN);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(customer, result.iterator().next().getKey());
		Assert.assertEquals(customer.person(), result.iterator().next().getValue());
	}
	
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forLoginInActiveCustomer() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		customer.state().deActivate();
		customerRepository.store(customer);
		
		customerRepository.forLogin(CustomerMemoryReposioryMock.DEFAULT_LOGIN);
		
	}
	
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forLoginInActiveRole() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		customer.state(customer.person()).deActivate();
		customerRepository.store(customer);
		
		customerRepository.forLogin(CustomerMemoryReposioryMock.DEFAULT_LOGIN);
		
	}
	
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forLoginInActivePerson() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		customer.person().state().deActivate();
		customerRepository.store(customer);
		
		customerRepository.forLogin(CustomerMemoryReposioryMock.DEFAULT_LOGIN);
		
		
		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forLoginNoLoginContact() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		
		LoginContact contact = (LoginContact) customer.person().contacts().iterator().next();
		ReflectionTestUtils.setField(contact, "isLogin", false);
		customerRepository.store(customer);
		
		customerRepository.forLogin(CustomerMemoryReposioryMock.DEFAULT_LOGIN);
		
		
		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forLoginNoLoginContactAware() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		
		customer.person().remove((LoginContact) customer.person().contacts().iterator().next());
		customer.person().assign(new ContactBuilderFactoryImpl().postBoxAddressBuilder().withBox("1234").withCity("city").withCountry(Locale.GERMANY).withZipCode("12345").build());
		customerRepository.store(customer);
		
		customerRepository.forLogin(CustomerMemoryReposioryMock.DEFAULT_LOGIN);
		
		
		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forLoginWrongLogin() {
		final CustomerRepository customerRepository = new CustomerMemoryReposioryMock();
		final Customer customer = customerRepository.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID);
		
		customerRepository.store(customer);
		
		Assert.assertTrue(customerRepository.forLogin("dontLetMeGetMe").isEmpty());;
		
		
		
	}
	
	
	

}
