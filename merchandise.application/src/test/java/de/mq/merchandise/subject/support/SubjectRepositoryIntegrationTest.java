package de.mq.merchandise.subject.support;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/repositories.xml" })
@Ignore
public class SubjectRepositoryIntegrationTest {

	private static final String SUBJECT_NAME = "Pets4You";

	private static final long CUSTOMER_ID = 1L;

	
	private List<Entry<Long, Class<?>>> waste = new ArrayList<>();

	@Autowired
	private SubjectRepository subjectRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Before
	public final void setup() {
		final Customer customer = entityManager.find(CustomerImpl.class, CUSTOMER_ID);
		final Subject subject = new SubjectImpl(customer, SUBJECT_NAME);
		customer.conditionTypes().forEach(ct -> subject.add(ct, ConditionDataType.String));
		

		subjectRepository.save(subject);

		
		waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(), subject.getClass()));
	
		

	}

	@After
	public final void cleanup() {
		waste.forEach(e -> entityManager.remove(entityManager.find(e.getValue(), e.getKey())));
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRED)
	@Rollback(false)
	public final void read() {
		
		final Subject subject = Mockito.mock(Subject.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.of(CUSTOMER_ID));
		Mockito.when(subject.customer()).thenReturn(customer);
		
		Mockito.when(subject.name()).thenReturn(SUBJECT_NAME);
		final ResultNavigation paging = Mockito.mock(ResultNavigation.class);
		Mockito.when(paging.firstRow()).thenReturn(Integer.valueOf(0));
		Mockito.when(paging.pageSize()).thenReturn(Integer.valueOf(25));
		final Collection<Subject> results = subjectRepository.subjectsForCustomer(subject, paging);
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(SUBJECT_NAME, results.stream().findFirst().get().name());
		Assert.assertEquals(2, results.stream().findFirst().get().conditions().size());
		
	}
	
	
	@Test
	@Transactional(propagation = Propagation.REQUIRED)
	@Rollback(false)
	public final void count() {
		
		final Subject subject = Mockito.mock(Subject.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.of(CUSTOMER_ID));
		Mockito.when(subject.customer()).thenReturn(customer);
		
		Mockito.when(subject.name()).thenReturn(SUBJECT_NAME);
	
		
	   Assert.assertEquals(1, subjectRepository.subjectsForCustomer(subject).intValue());
		
		
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Rollback(false)
	//@Ignore
	public final void delete() {
		Assert.assertEquals(1, waste.size());
		final Subject subject = (Subject) entityManager.find(waste.stream().findFirst().get().getValue(), waste.stream().findFirst().get().getKey());
		Assert.assertNotNull(subject);
		subjectRepository.remove(subject);

		Assert.assertNull(entityManager.find(waste.stream().findFirst().get().getValue(), waste.stream().findFirst().get().getKey()));
		waste.remove(0);
	}
	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Rollback(false)
	public final void subjects() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.of(CUSTOMER_ID));
		final Collection<Entry<Long,String>> results = subjectRepository.subjectMapForCustomer(customer);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(SUBJECT_NAME, results.stream().findFirst().get().getValue());
	}

}
