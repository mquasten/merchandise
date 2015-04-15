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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.ConditionDataType;
import de.mq.merchandise.subject.support.SubjectImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
@Ignore
public class SubjectIntegrationTest {
	
	
	private static final String SUBJECT_NAME = "Dolls for you";

	private static final String CUSTOMER_NAME = "Minogue-Music";

	private static final String PUBLIC_DATE = "public";

	private static final String PRIVATE_DATE = "private";

	private static final String CONDITION_TYPE = "date";

	@PersistenceContext
	private EntityManager entityManager;
	

	
	private final List<Entry<Long,Class<?>>> waste = new ArrayList<>();
	
	@Before
	@After
	@Transactional()
	@Rollback(false)
	public final void cleanup() throws SQLException {
		waste.forEach( e -> entityManager.remove(entityManager.find(e.getValue(), e.getKey())));
		waste.clear();

	    
	}
	
	
	
	@Test
	@Transactional()
	@Rollback(false)
	public final void create() {
		
		final Customer customer = new CustomerImpl(CUSTOMER_NAME);
		
		entityManager.persist(customer);
		
		final Subject subject = new SubjectImpl(customer, SUBJECT_NAME);
		entityManager.persist(subject);
		
		Assert.assertTrue(subject.id().isPresent());
		waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(),subject.getClass()));
		waste.add(new AbstractMap.SimpleEntry<>(customer.id().get(),customer.getClass()));
		entityManager.flush();
		//entityManager.clear();
		final Customer result = entityManager.find(CustomerImpl.class, customer.id().get());
		entityManager.refresh(result);
		Assert.assertEquals(subject, result.subjects().stream().findFirst().get());
		Assert.assertTrue(result.subjects().stream().findFirst().isPresent());
		Assert.assertEquals(subject.id(), result.subjects().stream().findFirst().get().id());
		
		Assert.assertEquals(result, subject.customer());
		Assert.assertTrue(result.id().isPresent());
		Assert.assertEquals(result.id(), subject.customer().id());
	}
	
	
	private Long prepare() {
		final Customer customer = new CustomerImpl(CUSTOMER_NAME);
		
		entityManager.persist(customer);
		
		final Subject subject = new SubjectImpl(customer, SUBJECT_NAME);
		entityManager.persist(subject);
		waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(),subject.getClass()));
		waste.add(new AbstractMap.SimpleEntry<>(customer.id().get(),customer.getClass()));
		entityManager.flush();
		return subject.id().get();
	}
	
	@Transactional
	@Rollback(false)
	@Test
	public final void withConditions() {
		final Long subjectId = prepare();
		final Subject  subject = entityManager.find(SubjectImpl.class, subjectId);
	    entityManager.refresh(subject);
	    
	    subject.add(CONDITION_TYPE, ConditionDataType.String);
	   
		final Condition<String> condition = subject.condition(CONDITION_TYPE);
	    condition.add(PRIVATE_DATE);
	    condition.add(PUBLIC_DATE);
	    entityManager.merge(subject);
	    
	    entityManager.flush();
	   
	    final Subject  result = entityManager.find(SubjectImpl.class, subjectId);
	    entityManager.refresh(result);
	    Assert.assertEquals(1, result.conditions().size());
	    Assert.assertTrue(result.conditions().stream().findFirst().isPresent());
	    
	    Assert.assertTrue(result.conditions().stream().findFirst().get().id().isPresent());
	   
	   
		@SuppressWarnings( "unchecked")
		final List<String> values = (List<String>) result.conditions().stream().findFirst().get().values();
	 
		Assert.assertEquals(2, values.size());
		Assert.assertTrue(values.contains(PRIVATE_DATE));
		Assert.assertTrue(values.contains(PUBLIC_DATE));
		
	}

}
