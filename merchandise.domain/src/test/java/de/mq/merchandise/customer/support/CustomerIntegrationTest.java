package de.mq.merchandise.customer.support;

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
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class CustomerIntegrationTest {
	
	private static final String CUSTOMER_NAME = "Minogue-Music";
	private static final String CONDITION_TYPE = "date";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	final List<Entry<Long, Class<?>>> waste = new ArrayList<>();
	
	@Before
	@After
	@Transactional()
	@Rollback(false)
	public final void cleanup() throws SQLException {
		waste.forEach( e -> entityManager.remove(entityManager.find(e.getValue(), e.getKey())));
		waste.clear();

	    
	}
	
	@Transactional()
	@Rollback(false)
	@Test
	public final void persistCustomer() {
	
		final Customer customer = new CustomerImpl(CUSTOMER_NAME);
		customer.assignConditionType(CONDITION_TYPE);
		
		entityManager.persist(customer);
		entityManager.flush();
		Assert.assertTrue(customer.id().isPresent());
		waste.add(new AbstractMap.SimpleEntry<>(customer.id().get(), customer.getClass()));
		
		final Customer result = entityManager.find(CustomerImpl.class, customer.id().get());
		entityManager.refresh(result);
		
		Assert.assertEquals(1, result.conditionTypes().size());
		Assert.assertEquals(CONDITION_TYPE, result.conditionTypes().stream().findFirst().get());
		
	}

}
