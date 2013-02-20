package de.mq.merchandise.customer.support;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.NativityImpl;
import de.mq.merchandise.customer.support.NaturalPersonImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class CustomerIntegrationTest {


	
	@PersistenceContext
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	@Before()
	@After()
	public final void clean() {
		
		
		for(BasicEntity entity : waste){
			
			final BasicEntity inDenStaub = entityManager.find(entity.getClass(), entity.id());
			
			if ( inDenStaub==null){
				continue;
			}
			entityManager.remove(inDenStaub);
			
		}
		
		waste.clear();
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public final void createCustomer() {
		
		final Nativity nativity = newNativity();
				
				
		final Person person = new NaturalPersonImpl("Kylie", "Minogue", nativity);
		final Customer customer = new CustomerImpl(person);
		entityManager.persist(customer);
		waste.add(customer);
		waste.add(person);
		entityManager.flush();
		Assert.assertEquals(customer, entityManager.find(CustomerImpl.class, customer.id()));
		
	}

	private Nativity newNativity() {
		try {
			final Constructor<? extends Nativity> constructor = NativityImpl.class.getDeclaredConstructor(String.class, Date.class);
			constructor.setAccessible(true);
			return constructor.newInstance("Melborne", new GregorianCalendar(1968, 4, 28).getTime());
		} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
		
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public final void assignUser() {
		
		final Nativity nativity = newNativity();
		final Person person = new NaturalPersonImpl("Kylie", "Minogue", nativity);
		final Person contactPerson = new NaturalPersonImpl("Dannii", "Minogue", nativity);
		Customer customer = new CustomerImpl(person);
		//entityManager.persist(customer);
		//entityManager.persist(contactPerson);
		
		customer.grant(contactPerson, CustomerRole.Bids, CustomerRole.Demands);
		customer.state(contactPerson).activate();
		
		//customer=entityManager.merge(customer);
		entityManager.persist(customer);
		entityManager.flush();
		waste.add(customer);
		waste.add(person);
		waste.add(contactPerson);
		entityManager.refresh(contactPerson);
		
		
		final Customer result = entityManager.find(CustomerImpl.class, customer.id());
		Assert.assertEquals(customer, result);
		
		Assert.assertEquals(1, result.activePersons().size());
		
		Assert.assertEquals(contactPerson, result.activePersons().iterator().next());
		Assert.assertEquals(2, result.roles(contactPerson).size());
		Assert.assertTrue( result.roles(contactPerson).contains(CustomerRole.Bids));
		Assert.assertTrue( result.roles(contactPerson).contains(CustomerRole.Demands));
		
		
	
		final Person personResult = entityManager.find(NaturalPersonImpl.class, contactPerson.id());
		Assert.assertEquals(1, personResult.customers().size());
		Assert.assertEquals(customer, personResult.customers().iterator().next());
		
	}
	
}
