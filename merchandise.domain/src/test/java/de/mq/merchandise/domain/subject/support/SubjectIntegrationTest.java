package de.mq.merchandise.domain.subject.support;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.domain.subject.support.SubjectImpl;
import de.mq.merchandise.support.Customer;
import de.mq.merchandise.support.CustomerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class SubjectIntegrationTest {
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private DataSource dataSource;
	
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
		
		final Customer customer = new CustomerImpl("Minogue-Music");
		
		entityManager.persist(customer);
		
		final Subject subject = new SubjectImpl(customer, "Dolls for you");
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

}
