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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.ContactBuilderFactory;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.PersonConstants;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.support.RuleImpl;
import de.mq.merchandise.util.EntityUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class OpportunityIntegrationTest {
	
	private static final String PARAMETER_SUBJECT_VALUE = "19680528";
	private static final String PARAMETER_SUBJECT = "birthDate";
	private static final String PARAMETER_VALUE = "10";
	private static final String PARAMETER_NAME = "hotScore";
	private static final String UNIT_DAY = "day";
	private static final String UNIT_HOUR = "hour";
	private static final String KEY_WORD = "EscortService";
	private static final String PRODUCT_ID = "P-01";
	private static final String ACTIVITY_ID = "A-01";
	
	private static final String LINK = "nicole";
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	private ContactBuilderFactory contactBuilderFactory = new ContactBuilderFactoryImpl();
	
	@After
    public final void clean() {
    	for(final BasicEntity entity : waste){
    		final BasicEntity whereTheWildRosesGrow = entityManager.find(entity.getClass(), entity.id());
    		
    		if( whereTheWildRosesGrow != null){
    			entityManager.remove(whereTheWildRosesGrow);
    			entityManager.flush();
    		}
    	}
    	
    	
    }
	
	@Test
	@Transactional()
	@Rollback(false)
	public final void persitOpportunity() {
		
		
		
		final Customer customer  = entityManager.merge(PersonConstants.customer());
		
		Rule rule = EntityUtil.create(RuleImpl.class);
		ReflectionTestUtils.setField(rule, "customer", customer);
		ReflectionTestUtils.setField(rule, "name", "whereTheWildRosesGrow");
		rule=entityManager.merge(rule);
		
		
		Rule ruleForSubject = EntityUtil.create(RuleImpl.class);
		
		ReflectionTestUtils.setField(ruleForSubject, "customer", customer);
		ReflectionTestUtils.setField(ruleForSubject, "name", "downInTheWillowGarden");
		ruleForSubject=entityManager.merge(ruleForSubject);
	
		final CommercialSubject commercialSubject = entityManager.merge(new CommercialSubjectImpl(customer, "EscortService++", null));
		
		final ActivityClassification activityClassification = entityManager.find(ActivityClassificationImpl.class, ACTIVITY_ID);
		final ProductClassification procuctClassification =entityManager.find(ProductClassificationImpl.class, PRODUCT_ID);
		final Address address = contactBuilderFactory.addressBuilder().withCity("Wegberg").withZipCode("41844").withHouseNumber("4").withStreet("Am Telt").withCoordinates(contactBuilderFactory.coordinatesBuilder().withLongitude(6).withLatitude(54).build()).build();
		waste.add(customer);
		waste.add(commercialSubject);
		
		
		Opportunity opportunity = new OpportunityImpl(customer,"Nicole's special services" , "with prices and conditions", Kind.Tender);
       
		opportunity.assignClassification(activityClassification);
		opportunity.assignClassification(procuctClassification);
		opportunity.assignWebLink(LINK);
		opportunity.assignKeyWord(KEY_WORD);
		opportunity.assign(address);
		final List<String> values = new ArrayList<>();
		values.add(UNIT_HOUR);
		values.add(UNIT_DAY);
		
		
		
		
		
		
		final Condition condition = new ConditionImpl(ConditionType.PricePerUnit, values);
	
		condition.assign(rule, 4711);
	 
		
		final RuleInstance ruleInstance = condition.ruleInstance(rule);
		ruleInstance.assign(PARAMETER_NAME, PARAMETER_VALUE);
		
		opportunity.assignConditions(commercialSubject, condition);
		
		final CommercialRelation commercialRelation = opportunity.commercialRelation(commercialSubject);
		commercialRelation.assign(ruleForSubject, 4712);
		final RuleInstance ruleInstanceForSubject = commercialRelation.ruleInstance(ruleForSubject);
		ruleInstanceForSubject.assign(PARAMETER_SUBJECT, PARAMETER_SUBJECT_VALUE); 
		
		
		opportunity=entityManager.merge(opportunity);
		
		
		waste.add(opportunity);
		waste.add(ruleForSubject);
		waste.add(rule);
		
		entityManager.flush();
		
		entityManager.refresh(opportunity);
		
		final Opportunity result = entityManager.find(OpportunityImpl.class, opportunity.id());
		
		Assert.assertEquals(opportunity, result);
		
		
		Assert.assertEquals(1, result.documents().size());
		Assert.assertEquals(LINK, result.documents().keySet().iterator().next());
		Assert.assertEquals(String.format(OpportunityImpl.WWW_URL, LINK),  new String(result.documents().values().iterator().next()));
		
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
		
		Assert.assertEquals(1, result.addresses().size());
		Assert.assertEquals(address, result.addresses().iterator().next());
		
		Assert.assertEquals(1, result.condition(commercialSubject, ConditionType.PricePerUnit).ruleInstances().size());
		Assert.assertEquals(rule, result.condition(commercialSubject, ConditionType.PricePerUnit).ruleInstances().get(0).rule());
		Assert.assertEquals(PARAMETER_VALUE, result.condition(commercialSubject, ConditionType.PricePerUnit).ruleInstance(rule).parameter(PARAMETER_NAME));
		
		Assert.assertEquals(result.condition(commercialSubject, condition.conditionType()), ReflectionTestUtils.getField(result.condition(commercialSubject, ConditionType.PricePerUnit).ruleInstances().get(0), "condition"));
		
		
		Assert.assertNull(ReflectionTestUtils.getField(result.condition(commercialSubject, ConditionType.PricePerUnit).ruleInstances().get(0), "commercialRelation"));
		
		Assert.assertNotNull(result.addresses().iterator().next().id());
		
		
		Assert.assertEquals(1, result.commercialRelations().iterator().next().ruleInstances().size());
		Assert.assertEquals(ruleInstanceForSubject, result.commercialRelations().iterator().next().ruleInstance(ruleForSubject));
		Assert.assertEquals(result.commercialRelations().iterator().next(), ReflectionTestUtils.getField(result.commercialRelations().iterator().next().ruleInstance(ruleForSubject), "commercialRelation"));
		Assert.assertNull(ReflectionTestUtils.getField(result.commercialRelations().iterator().next().ruleInstance(ruleForSubject), "condition"));
		
		Assert.assertEquals(PARAMETER_SUBJECT_VALUE, result.commercialRelations().iterator().next().ruleInstance(ruleForSubject).parameter(PARAMETER_SUBJECT));
		
	}
	
	
	
	
	

}
