package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.opportunity.support.CommercialSubject.DocumentType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class OpportunityIntegrationTest {
	
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	@After
    public final void clean() {
    	for(final BasicEntity entity : waste){
    		BasicEntity find = entityManager.find(entity.getClass(), entity.id());
    		System.out.println(find);
    		if( find != null)
			entityManager.remove(find);
    		entityManager.flush();
    	}
    	
    	
    }
	
	@Test
	@Transactional()
	@Rollback(false)
	public final void persitOpportunity() {
		final Customer customer  = entityManager.merge(PersonConstants.customer());
		final CommercialSubject commercialSubject = entityManager.merge(new CommercialSubjectImpl(customer, "EscortService++", null));
		
		final ActivityClassification activityClassification = entityManager.find(ActivityClassificationImpl.class, "A-01");
		final ProcuctClassification procuctClassification =entityManager.find(ProductClassificationImpl.class, "P-01");
		
		waste.add(customer);
		waste.add(commercialSubject);
		System.out.println(activityClassification.id());
		System.out.println(procuctClassification.id());
		
		final Opportunity opportunity = new OpportunityImpl(customer,"Nicole's special services" , "with prices and conditions");
       
		opportunity.assignClassification(activityClassification);
		opportunity.assignClassification(procuctClassification);
		opportunity.assignDocument("nicole", DocumentType.Link, "nicoles pictures and videos".getBytes());
		opportunity.assignKeyWord("EscortService");
		final List<String> values = new ArrayList<>();
		values.add("hour");
		values.add("day");
		
		
		opportunity.assignConditions(commercialSubject, new ConditionImpl(null, values));
		waste.add(entityManager.merge(opportunity));
		entityManager.flush();
		System.out.println("*******************************************************");
	}
	

}
