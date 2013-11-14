package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class OpportunityIndexIntegrationTest {
	
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	
	
	
	@After
    public final void clean() {
    	for(final BasicEntity entity : waste){
    		
    		final BasicEntity find = entityManager.find(entity.getClass(), entity.id());
    		
    		if( find != null)
			entityManager.remove(find);
    		entityManager.flush();
    	}
    	
    	
    }
	
	@Test
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Rollback(false)
	public final void create() {
		
		final Customer customer  = entityManager.merge(PersonConstants.customer());
		final CommercialSubject commercialSubject = entityManager.merge(new CommercialSubjectImpl(customer, "EscortService++", null));
		final Opportunity opportunity = entityManager.merge(new OpportunityImpl(customer,"Nicole's special services" , "with prices and conditions", Kind.Tender));
		
		
	
		
		final OpportunityIndexImpl opportunityIndex = entityManager.merge(new OpportunityIndexImpl(opportunity));
		waste.add(opportunityIndex);
		waste.add(opportunity);
		waste.add(commercialSubject);
		waste.add(customer);
		waste.add(customer.person());
		
		
		final OpportunityIndexImpl result = entityManager.find(OpportunityIndexImpl.class, opportunityIndex.id());
		System.out.println(result);
		
		Assert.assertEquals(opportunity, result.opportunity());
		Assert.assertEquals(opportunityIndex, result);
		Assert.assertEquals(result.id(), result.opportunity().id());
	//entityManager.flush();
		
	}

}
