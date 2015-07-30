package de.mq.merchandise.subject.support;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/emf.xml" })
public class CommercialSubjectIntegrationTest {

	private static final String CONDITION_QUANTITY = "Quantity";
	private static final String PUBLIC_DATE = "Public-Date";
	private static final String PRIVATE_DATE = "Private-Date";
	private static final String CONDITION_UNIT = "Unit";
	private static final String CONDITION_QUALITY_VALUE = "Platinium";
	private static final String CONDITION_QUALITY = "Quality";
	@PersistenceContext
	private EntityManager entityManager;
	private static final String CUSTOMER_NAME = "Minogue-Music";

	private static final String SUBJECT_NAME = "Dolls for you";

	private final List<Entry<Long, Class<?>>> waste = new ArrayList<>();

	@Before
	@After
	@Transactional()
	@Rollback(false)
	public final void cleanup() throws SQLException {
		waste.forEach(e -> entityManager.remove(entityManager.find(e.getValue(), e.getKey())));
		waste.clear();
	}

	@Test
	@Transactional()
	@Rollback(false)
	public final void create() {

		final Customer customer = new CustomerImpl(CUSTOMER_NAME);

		entityManager.persist(customer);

		final Subject newSubject = new SubjectImpl(customer, SUBJECT_NAME);
		newSubject.add(CONDITION_QUALITY, ConditionDataType.String);
		newSubject.add(CONDITION_UNIT, ConditionDataType.String);
		newSubject.add(CONDITION_QUANTITY, ConditionDataType.IntegralNumber);

		final Long id = entityManager.merge(newSubject).id().get();
		final Subject subject = entityManager.find(SubjectImpl.class, id);

		final CommercialSubject commercialSubjet = new CommercialSubjectImpl("Platinum Escort", customer);

		commercialSubjet.assign(subject, "escort service", true);
		commercialSubjet.assign(subject.condition(CONDITION_QUALITY), CONDITION_QUALITY_VALUE);
		commercialSubjet.assign(subject.condition(CONDITION_UNIT), PRIVATE_DATE);
		commercialSubjet.assign(subject.condition(CONDITION_UNIT), PUBLIC_DATE);

		entityManager.persist(commercialSubjet);

		waste.add(new AbstractMap.SimpleEntry<>(commercialSubjet.id().get(), commercialSubjet.getClass()));

		waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(), subject.getClass()));
		waste.add(new AbstractMap.SimpleEntry<>(customer.id().get(), customer.getClass()));
		entityManager.flush();

		final CommercialSubject result = entityManager.find(CommercialSubjectImpl.class, commercialSubjet.id().get());
		entityManager.refresh(result);
		result.conditionValues(subject).forEach(e -> {
			Assert.assertEquals(subject.condition(e.getKey().conditionType()).id().get(), e.getKey().id().get());
			Assert.assertEquals(subject, e.getKey().subject());
			if (e.getKey().conditionType().equals(CONDITION_QUALITY)) {

				Assert.assertTrue(e.getValue().stream().findFirst().isPresent());
				Assert.assertEquals(CONDITION_QUALITY_VALUE, e.getValue().stream().findFirst().get());

			} else if (e.getKey().conditionType().equals(CONDITION_UNIT)) {
				Assert.assertTrue(e.getValue().contains(PRIVATE_DATE));
				Assert.assertTrue(e.getValue().contains(PUBLIC_DATE));
				Assert.assertEquals(2, e.getValue().size());
			} else if (e.getKey().conditionType().equals(CONDITION_QUANTITY)) {
				Assert.assertTrue(e.getValue().isEmpty());
			} else {
				Assert.fail("Wrong conditionType: " + e.getKey().conditionType());
			}

		});
		
		
	

	}

}
