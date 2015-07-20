package de.mq.merchandise.subject.support;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import de.mq.merchandise.subject.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
@Ignore
public class CommercialSubjectIntegrationTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	private static final String CUSTOMER_NAME = "Minogue-Music";
	
	private static final String SUBJECT_NAME = "Dolls for you";

	
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
		
		
		final CommercialSubjet commercialSubjet = new CommercialSubjectImpl("Platinum Escort", customer);
		
		commercialSubjet.assign(subject, "escort service", true);
		
		entityManager.persist(commercialSubjet);
		
		
		waste.add(new AbstractMap.SimpleEntry<>(commercialSubjet.id().get(),commercialSubjet.getClass()));
		
		waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(),subject.getClass()));
		waste.add(new AbstractMap.SimpleEntry<>(customer.id().get(),customer.getClass()));
		entityManager.flush();
	}

}
