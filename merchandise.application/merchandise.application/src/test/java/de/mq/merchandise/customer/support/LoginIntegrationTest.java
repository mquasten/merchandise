package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.Person;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/repositories.xml"})
@ActiveProfiles("db")

public class LoginIntegrationTest {
	
	@Autowired()
	private CustomerRepository customerRepository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	private List<BasicEntity> waste = new ArrayList<>();
	
	@After
	public final void clean() {
		for(BasicEntity entity : waste){
			final BasicEntity inDenStaub = entityManager.find(entity.getClass(), entity.id());
			if (inDenStaub== null) {
				continue;
			}
			entityManager.remove(inDenStaub);
		}
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public final void testLogin() {

	final Customer result = entityManager.merge(newCustomer());
	waste.add(result);
	waste.add(result.person());
	
	entityManager.flush();
	

	final Collection<Entry<Customer,Person>> results = customerRepository.forLogin("skype:pokerface");
	 
	 Assert.assertEquals(1, results.size());
	 Assert.assertEquals(result, results.iterator().next().getKey());
	 Assert.assertEquals(result.person(), results.iterator().next().getValue());
	}

	private Customer newCustomer() {
		final Person person = newPerson(); 
		person.assign(new ContactBuilderFactoryImpl().instantMessengerContactBuilder().withAccount("pokerface").withLogin().withProvider(InstantMessenger.Skype).build());
		person.state().activate();
		final Customer customer = new CustomerImpl(person);	
		customer.state().activate();
		customer.grant(person, CustomerRole.values());
		customer.state(person).activate();
		return customer;
	}

	private LegalPersonImpl newPerson() {
		return new LegalPersonImpl("LadyGaga","12345", new TradeRegisterBuilderFactoryImpl().tradeRegisterBuilder().withCity("city").withZipCode("zip").withReference("0815").build(), LegalForm.eK, new Date(), Locale.US);
	}
	
	
	

}
