package de.mq.merchandise.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.opportunity.support.CommercialSubject;

public class AbstractPagingMemoryRepositoryTest {

	private static final String PARAM_NAME = "min";

	private static final long MIN_ID = 100;

	private static final Long ID = 42L;

	private final BasicRepository<Customer, Long> customerRepository = new AbstractPagingMemoryRepository<Customer>() {

		@Override
		protected boolean match(Customer value, Parameter<?>... params) {
			final Long min = super.param(params, PARAM_NAME, Long.class);
			if (value.id() > min) {
				return false;
			}

			return true;
		}

		@Override
		protected Comparator<Customer> comparator() {

			return new Comparator<Customer>() {

				@Override
				public int compare(Customer o1, Customer o2) {

					return (int) Math.signum(o1.id() - o2.id());
				}
			};
		}
	};

	@Test
	public final void save() {

		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.hasId()).thenReturn(true);
		Mockito.when(customer.id()).thenReturn(ID);
		final Customer result = customerRepository.save(customer);
		Assert.assertEquals(customer, result);
		Assert.assertEquals((long) ID, result.id());

		final Map<Long, Customer> results = storedValues();

		Assert.assertEquals(1, results.size());

		Assert.assertEquals(ID, results.keySet().iterator().next());
		Assert.assertEquals(customer, results.values().iterator().next());
		Assert.assertEquals(customer.id(), results.values().iterator().next().id());

	}

	@Test
	public final void saveNew() {
		final Customer customer = new CustomerImpl(Mockito.mock(Person.class));
		Assert.assertFalse(customer.hasId());

		final Customer result = customerRepository.save(customer);
		Assert.assertTrue(result.id() > 0L);
		Assert.assertEquals(customer, result);

		final Map<Long, Customer> results = storedValues();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(customer, results.get(result.id()));
	}

	@Test
	public final void search() {
		final Map<Long, Customer> results = storedValues();

		for (long i = 0; i < MIN_ID; i++) {
			final Customer commercialSubject = Mockito.mock(Customer.class);
			Mockito.when(commercialSubject.hasId()).thenReturn(true);
			Mockito.when(commercialSubject.id()).thenReturn(i + 1);

			results.put(i, commercialSubject);
		}
		final ArgumentCaptor<Long> counter = ArgumentCaptor.forClass(Long.class);
		final Paging paging = Mockito.mock(Paging.class);

		Mockito.when(paging.maxPages()).thenReturn(10);
		Mockito.when(paging.currentPage()).thenReturn(3);
		Mockito.when(paging.pageSize()).thenReturn(10);
		Mockito.when(paging.firstRow()).thenReturn(20);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Collection<Customer> customers = ((AbstractPagingMemoryRepository) customerRepository).forPattern(paging, new ParameterImpl<Long>(PARAM_NAME, MIN_ID));

		Mockito.verify(paging).assignRowCounter(counter.capture());
		Assert.assertEquals(paging.pageSize(), customers.size());
		int i = 20;
		for (final Customer commercialSubject : customers) {
			// Assert.assertEquals("product"+i , commercialSubject.name());
			Assert.assertEquals(i + 1, commercialSubject.id());
			i++;
		}
		Assert.assertEquals(MIN_ID, (long) counter.getValue());
	}

	@Test
	public final void searchMatch() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(19680528L);
		final Customer otherCustomer = Mockito.mock(Customer.class);
		Mockito.when(otherCustomer.id()).thenReturn(815L);

		final Map<Long, Customer> commercialSubjects = storedValues();
		final CommercialSubject notInResult = Mockito.mock(CommercialSubject.class);
		Mockito.when(notInResult.id()).thenReturn(1L);
		Mockito.when(notInResult.hasId()).thenReturn(true);
		Mockito.when(notInResult.name()).thenReturn("product");
		Mockito.when(notInResult.customer()).thenReturn(customer);

		final Customer inResult1 = Mockito.mock(Customer.class);
		Mockito.when(inResult1.id()).thenReturn(ID);
		Mockito.when(inResult1.hasId()).thenReturn(true);

		final Customer inResult2 = Mockito.mock(Customer.class);
		Mockito.when(inResult2.id()).thenReturn(1L);
		Mockito.when(inResult2.hasId()).thenReturn(true);

		final Customer wrongId = Mockito.mock(Customer.class);
		Mockito.when(wrongId.id()).thenReturn(101L);
		Mockito.when(wrongId.hasId()).thenReturn(true);

		commercialSubjects.put(inResult1.id(), inResult1);
		commercialSubjects.put(inResult2.id(), inResult2);
		commercialSubjects.put(wrongId.id(), wrongId);

		final Paging paging = Mockito.mock(Paging.class);

		Mockito.when(paging.maxPages()).thenReturn(Integer.MAX_VALUE);
		Mockito.when(paging.currentPage()).thenReturn(1);
		Mockito.when(paging.pageSize()).thenReturn(Integer.MAX_VALUE);
		Mockito.when(paging.firstRow()).thenReturn(0);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Collection<Customer> results = ((AbstractPagingMemoryRepository) customerRepository).forPattern(paging, new ParameterImpl<Long>(PARAM_NAME, MIN_ID));
		Assert.assertEquals(2, results.size());
		long lastId = 0;
		for (final Customer result : results) {
			Assert.assertTrue(lastId < result.id());
		}

	}

	@Test
	public final void delete() {

		final Map<Long, Customer> customers = storedValues();
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(ID);
		Mockito.when(customer.hasId()).thenReturn(true);

		customers.put(ID, customer);
		Assert.assertEquals(1, customers.size());

		customerRepository.delete(customer.id());

		Assert.assertEquals(0, customers.size());

	}

	@Test
	public final void deleteNonExistingId() {
		@SuppressWarnings("unchecked")
		final Map<Long, Customer> map = Mockito.mock(Map.class);
		ReflectionTestUtils.setField(customerRepository, "storedValues", map);

		customerRepository.delete(ID);
		Mockito.verify(map).containsKey(ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void invalidParameter() {
		((AbstractPagingMemoryRepository<Customer>) customerRepository).param(new Parameter[] { new ParameterImpl<Long>("id", 4711L) }, "don'tLetMeGetMe", Long.class);
	}
	
	@Test
	public final void forId() {
		final Map<Long, Customer> customers = storedValues();
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(ID);
		customers.put(ID, customer);
		
		Assert.assertEquals(customer, customerRepository.forId(ID));
	}

	@SuppressWarnings("unchecked")
	private Map<Long, Customer> storedValues() {
		return (Map<Long, Customer>) ReflectionTestUtils.getField(customerRepository, "storedValues");
	}
	

}