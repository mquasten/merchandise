package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	
	private static final String NAME = "testrule";


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
		
		final Rule result = entityManager.merge(new RuleImpl(customer ,NAME));
	
		waste.add(result);
		waste.add(customer);
		waste.add(person);
		

		Assert.assertEquals(result, entityManager.find(RuleImpl.class, result.id()));
		
		
		final TypedQuery<Rule> typedQuery = entityManager.createNamedQuery(RuleRepository.RULE_FOR_NAME_PATTERN, Rule.class);
		typedQuery.setParameter(RuleRepository.PARAMETER_CUSTOMER_ID, customer.id());
		typedQuery.setParameter(RuleRepository.PARAMETER_RULE_NAME, NAME);
		boolean likeAVirgin=true;
		for(final Rule rule: typedQuery.getResultList()){
			Assert.assertEquals(result, rule);
			likeAVirgin=false;
		}
		Assert.assertFalse(likeAVirgin);
	}
	
	
	

}
