package de.mq.merchandise.order.support;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectImpl;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityImpl;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
@Ignore
public class ItemSetIntegrationTest {
	
	private static final double AMOUNT = 47.11;

	@PersistenceContext
	private EntityManager entityManager;
	
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	@Before
	@After
	public void cleanup() {
		for(final BasicEntity whereTheWildRosesGrow : waste ){
			final BasicEntity inDenStaub = entityManager.find(whereTheWildRosesGrow.getClass(), whereTheWildRosesGrow.id());
			if( inDenStaub==null){
				continue;
			}
			entityManager.remove(inDenStaub);
		}
	}

	@Test
	@Transactional()
	@Rollback(false)
	public final void store() {
		
		final Customer tradingPartner  = entityManager.merge(PersonConstants.customer());
		final Customer customer =  entityManager.merge(new CustomerImpl(PersonConstants.legalPerson()));
		final CommercialSubject subject = entityManager.merge(new CommercialSubjectImpl(customer, "Pets4You", "Nicole's special Services"));
		final Opportunity newOpportunity = new OpportunityImpl(customer, "PetStore");
		final Opportunity opportunity =  entityManager.merge( newOpportunity);
		
		final ItemSet order = new ItemSetImpl(tradingPartner, opportunity);
		final Item item = new ItemImpl(order, subject);
		ReflectionTestUtils.setField(item, "pricePerUnit", new MoneyImpl(AMOUNT));
		
		order.assign(item);
		ItemSet itemSet = entityManager.merge(order);
		waste.add(itemSet);
		waste.add(opportunity);
		waste.add(subject);
		waste.add(tradingPartner);
		waste.add(customer);
		waste.add(tradingPartner.person());
		waste.add(customer.person());
	

		entityManager.flush();
		entityManager.clear();
		
		
		final ItemSet result = entityManager.find(ItemSetImpl.class, itemSet.id());
		Assert.assertEquals(opportunity, result.opportunity());
		Assert.assertEquals(opportunity.id(), result.opportunity().id());
		Assert.assertEquals(tradingPartner, result.tradingPartner());
		Assert.assertEquals(tradingPartner.id(), result.tradingPartner().id());
		Assert.assertEquals(itemSet.created(), result.created());
		Assert.assertEquals(itemSet, result);
		Assert.assertEquals(itemSet.id(), result.id());
		
		
		Assert.assertEquals(itemSet.items().size(), result.items().size());
		for(final Item itemResult : result.items()){
			Assert.assertEquals(item.itemId(), itemResult.itemId());
			Assert.assertNotNull(itemResult.id());
			Assert.assertEquals(itemSet, itemResult.itemSet());
			Assert.assertEquals(subject, itemResult.subject());
			Assert.assertEquals(subject.id(), itemResult.subject().id());
			Assert.assertEquals(new MoneyImpl(AMOUNT), itemResult.pricePerUnit());
		}
		
		
		
		
	}

	
}
