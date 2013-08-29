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
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.opportunity.support.DocumentsAware.DocumentType;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class OpportunityIntegrationTest {
	
	private static final String UNIT_DAY = "day";
	private static final String UNIT_HOUR = "hour";
	private static final String KEY_WORD = "EscortService";
	private static final String PRODUCT_ID = "P-01";
	private static final String ACTIVITY_ID = "A-01";
	private static final byte[] DOCUMENT_CONTENT = "nicoles pictures and videos".getBytes();
	private static final String LINK = "nicole";
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
		
		final ActivityClassification activityClassification = entityManager.find(ActivityClassificationImpl.class, ACTIVITY_ID);
		final ProductClassification procuctClassification =entityManager.find(ProductClassificationImpl.class, PRODUCT_ID);
		
		waste.add(customer);
		waste.add(commercialSubject);
		
		
		Opportunity opportunity = new OpportunityImpl(customer,"Nicole's special services" , "with prices and conditions", Kind.Tender);
       
		opportunity.assignClassification(activityClassification);
		opportunity.assignClassification(procuctClassification);
		opportunity.assignDocument(LINK, DocumentType.Link, DOCUMENT_CONTENT);
		opportunity.assignKeyWord(KEY_WORD);
		final List<String> values = new ArrayList<>();
		values.add(UNIT_HOUR);
		values.add(UNIT_DAY);
		
		
		opportunity.assignConditions(commercialSubject, new ConditionImpl(ConditionType.PricePerUnit, values));
		opportunity=entityManager.merge(opportunity);
		waste.add(opportunity);
		entityManager.flush();
		
		entityManager.refresh(opportunity);
		
		final Opportunity result = entityManager.find(OpportunityImpl.class, opportunity.id());
		
		Assert.assertEquals(opportunity, result);
		
		
		Assert.assertEquals(1, result.documents().size());
		Assert.assertEquals(LINK, result.documents().keySet().iterator().next());
		Assert.assertEquals(new String(DOCUMENT_CONTENT),  new String(result.documents().values().iterator().next()));
		
		Assert.assertEquals(1,result.activityClassifications().size());
		Assert.assertEquals(ACTIVITY_ID, result.activityClassifications().iterator().next().id());
		
		Assert.assertEquals(1,result.productClassifications().size());
		Assert.assertEquals(PRODUCT_ID, result.productClassifications().iterator().next().id());
		
		Assert.assertEquals(1,result.keyWords().size() ); 
		Assert.assertEquals(KEY_WORD, result.keyWords().iterator().next());
		
		Assert.assertEquals(1, result.commercialRelations().size());
		
		Assert.assertEquals(commercialSubject, result.commercialRelations().iterator().next().commercialSubject());
		
		Assert.assertEquals(opportunity,  result.commercialRelations().iterator().next().opportunity());
		
		Assert.assertEquals(1, result.commercialRelations().iterator().next().conditions().size());
		
		Assert.assertEquals(ConditionType.PricePerUnit, result.commercialRelations().iterator().next().conditions().keySet().iterator().next());
		Assert.assertEquals(2, result.commercialRelations().iterator().next().conditions().values().iterator().next().values().size());
		Assert.assertTrue(result.commercialRelations().iterator().next().conditions().values().iterator().next().values().contains(UNIT_HOUR));
		Assert.assertTrue(result.commercialRelations().iterator().next().conditions().values().iterator().next().values().contains(UNIT_DAY));
		
	}
	

}
