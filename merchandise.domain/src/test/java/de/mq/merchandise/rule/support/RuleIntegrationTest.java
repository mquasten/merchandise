package de.mq.merchandise.rule.support;

import java.util.ArrayList;
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
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;



	
	
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class RuleIntegrationTest {
	
	@PersistenceContext()
	private EntityManager entityManager;
	
	
	private List<BasicEntity> waste = new ArrayList<>(); 
	
	@Before
	@After
	public final void clean() {
		for(final BasicEntity toBeRemoved : waste){
			entityManager.remove(toBeRemoved);
		}
		waste.clear();
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public final void store() {
		
		Person person = PersonConstants.person();
		EntityUtil.setId(person, 19680528L);
	
		person = entityManager.merge(person);
		final Customer customer = entityManager.merge(new CustomerImpl(person));
		
		final Rule rule = new RuleImpl(customer ,"testrule");
		final Rule result = entityManager.merge(rule);
	
		waste.add(result);
		waste.add(customer);
		waste.add(person);
		

		Assert.assertEquals(result, entityManager.find(RuleImpl.class, result.id()));
	}
	
	

}
