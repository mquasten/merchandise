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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/repositories.xml" })
public class SubjectRepositoryIntegrationTest {

	private static final String SUBJECT_NAME = "Pets4You";

	private static final long CUSTOMER_ID = 1L;

	private static final String KIND_PRIVATE = "private";

	private static final String KIND_PUBLIC = "public";

	private static final String QUALITY_PLATINUM = "platinum";

	private static final String QUALITY_GOLD = "gold";

	private static final String QUALITY_SILVER = "silver";

	private static final String KIND = "Kind";

	private static final String QUALITY = "Quality";

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
		subject.condition(QUALITY).add(QUALITY_SILVER);
		subject.condition(QUALITY).add(QUALITY_GOLD);
		subject.condition(QUALITY).add(QUALITY_PLATINUM);

		subject.condition(KIND).add(KIND_PUBLIC);
		subject.condition(KIND).add(KIND_PRIVATE);

		subjectRepository.save(subject);

		waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(), subject.getClass()));

	}

	@After
	public final void cleanup() {
		waste.forEach(e -> entityManager.remove(entityManager.find(e.getValue(), e.getKey())));
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Rollback(false)
	public final void read() {
		Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.of(CUSTOMER_ID));
		final Collection<Subject> results = subjectRepository.subjectsForCustomer(customer);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(SUBJECT_NAME, results.stream().findFirst().get().name());
		Assert.assertEquals(2, results.stream().findFirst().get().conditions().size());
		Assert.assertEquals(3, results.stream().findFirst().get().condition(QUALITY).values().size());
		Assert.assertTrue(results.stream().findFirst().get().condition(QUALITY).values().contains(QUALITY_SILVER));
		Assert.assertTrue(results.stream().findFirst().get().condition(QUALITY).values().contains(QUALITY_GOLD));
		Assert.assertTrue(results.stream().findFirst().get().condition(QUALITY).values().contains(QUALITY_PLATINUM));

		Assert.assertEquals(2, results.stream().findFirst().get().condition(KIND).values().size());
		Assert.assertTrue(results.stream().findFirst().get().condition(KIND).values().contains(KIND_PUBLIC));
		Assert.assertTrue(results.stream().findFirst().get().condition(KIND).values().contains(KIND_PRIVATE));
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Rollback(false)
	public final void delete() {
		Assert.assertEquals(1, waste.size());
		final Subject subject = (Subject) entityManager.find(waste.stream().findFirst().get().getValue(), waste.stream().findFirst().get().getKey());
		Assert.assertNotNull(subject);
		subjectRepository.remove(subject);

		Assert.assertNull(entityManager.find(waste.stream().findFirst().get().getValue(), waste.stream().findFirst().get().getKey()));
		waste.remove(0);
	}

}
