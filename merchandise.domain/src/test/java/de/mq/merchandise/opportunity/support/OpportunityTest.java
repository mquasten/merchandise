package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;

public class OpportunityTest {
	
	private static final String WEB_LINK = "kylie.com";
	private static final String IMAGE = "kylie.jpg";
	private static final String KEYWORD = "keyword";
	
	private static final long ID = 19680528L;
	private static final String DESCRIPTION = "Description for artists";
	private static final String NAME = "Artists";
	
	private final Customer customer = Mockito.mock(Customer.class);

	@Test
	public final void constructorWithDescription() {
		final Opportunity opportunity = new OpportunityImpl(customer,NAME , DESCRIPTION, Kind.Tender); 
		Assert.assertEquals(customer, opportunity.customer());
		Assert.assertEquals(NAME, opportunity.name());
		Assert.assertEquals(DESCRIPTION, opportunity.description());
		Assert.assertEquals(Kind.Tender, opportunity.kind());
	}
	
	@Test
	public final void constructorWithOutDescription() {
		final Opportunity opportunity = new OpportunityImpl(customer,NAME); 
		Assert.assertEquals(customer, opportunity.customer());
		Assert.assertEquals(NAME, opportunity.name());
		Assert.assertNull(opportunity.description());
		Assert.assertEquals(Kind.ProductOrService, opportunity.kind());
	}
	
	@Test
	public final void idExists() {
		final Opportunity opportunity = new OpportunityImpl();
		ReflectionTestUtils.setField(opportunity, "id", ID);
		Assert.assertTrue(opportunity.hasId());
		Assert.assertEquals(ID, opportunity.id());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNotExists() {
		final Opportunity opportunity = new OpportunityImpl();
		Assert.assertFalse(opportunity.hasId());
		opportunity.id();
	}
	
	@Test
	public final void equals() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		Assert.assertTrue(opportunity.equals(opportunity));
		Assert.assertTrue(opportunity.equals(new OpportunityImpl(customer , NAME)));
		Assert.assertFalse(opportunity.equals(new OpportunityImpl(customer , "dontLetMeGetMe")));
	}
	
	@Test
	public final void hash() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		Assert.assertEquals(customer.hashCode() + opportunity.name().hashCode() , opportunity.hashCode());
	}
	@Test
	public final void documents() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		opportunity.assignWebLink(WEB_LINK);
		Assert.assertEquals(1, opportunity.documents().size());
		Assert.assertEquals(WEB_LINK, opportunity.documents().keySet().iterator().next());
		Assert.assertEquals(String.format(OpportunityImpl.WWW_URL,WEB_LINK), new String(opportunity.documents().values().iterator().next()));
		opportunity.removeDocument(WEB_LINK);
		Assert.assertTrue(opportunity.documents().isEmpty());
	}
	
	@Test
    public final void productClassifications() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final ProductClassification procuctClassification = Mockito.mock(ProductClassification.class);
		opportunity.assignClassification(procuctClassification);
		Assert.assertEquals(1, opportunity.productClassifications().size());
		Assert.assertEquals(procuctClassification, opportunity.productClassifications().iterator().next());
		opportunity.removeClassification(procuctClassification);
		Assert.assertTrue(opportunity.productClassifications().isEmpty());
    }
	
	@Test
    public final void activityClassifications() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final ActivityClassification activityClassification = Mockito.mock(ActivityClassification.class);
		opportunity.assignClassification(activityClassification);
		Assert.assertEquals(1, opportunity.activityClassifications().size());
		Assert.assertEquals(activityClassification, opportunity.activityClassifications().iterator().next());
		opportunity.removeClassification(activityClassification);
		Assert.assertTrue(opportunity.activityClassifications().isEmpty());
	}
	
	@Test
	public final void keyWords() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		opportunity.assignKeyWord(KEYWORD);
		Assert.assertEquals(1, opportunity.keyWords().size());
		Assert.assertEquals(KEYWORD, opportunity.keyWords().iterator().next());
		opportunity.removeKeyWord(KEYWORD);
		Assert.assertTrue(opportunity.keyWords().isEmpty());
	}
	
	@Test
	public final void opportunityTypes() {
		for(final Opportunity.Kind kind : Opportunity.Kind.values() ){
			Assert.assertEquals(kind, Opportunity.Kind.valueOf(kind.name()));
		}
	}
	
	@Test
	public final void assignConditions() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.PricePerUnit);
		opportunity.assignConditions(commercialSubject, condition);
		
		
		final Collection<CommercialRelation> results = opportunity.commercialRelations();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(commercialSubject, results.iterator().next().commercialSubject());
		Assert.assertEquals(1, results.iterator().next().conditions().size());
		Assert.assertEquals(ConditionType.PricePerUnit, results.iterator().next().conditions().keySet().iterator().next());
		Assert.assertEquals(condition, results.iterator().next().conditions().values().iterator().next());
	}
	
	
	
	@Test
	public final void assignConditionsRelationExists() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialRelation.commercialSubject()).thenReturn(commercialSubject);
		@SuppressWarnings("unchecked")
		final Collection<CommercialRelation> relations = (Collection<CommercialRelation>) ReflectionTestUtils.getField(opportunity, "commercialRelations");
		relations.add(commercialRelation);
		
		Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.Quantity);
		
		opportunity.assignConditions(commercialSubject, condition);
		
		
		Mockito.verify(commercialRelation).assign(condition);
		
		
	}
	
	@Test
	public final void assignConditionsRelationCreateNewOtherSubject() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final CommercialRelation existingRelation = Mockito.mock(CommercialRelation.class);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(existingRelation.commercialSubject()).thenReturn(commercialSubject);
		
		final CommercialRelation newRelation = Mockito.mock(CommercialRelation.class);
		final CommercialSubject newCommercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(newRelation.commercialSubject()).thenReturn(newCommercialSubject);
		
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.Quantity);
		@SuppressWarnings("unchecked")
		final Collection<CommercialRelation> relations = (Collection<CommercialRelation>) ReflectionTestUtils.getField(opportunity, "commercialRelations");
		
		relations.add(existingRelation);
		
		
		opportunity.assignConditions(newCommercialSubject,condition );
		
		
		
		Assert.assertEquals(2, relations.size());
		
		for(final CommercialRelation commercialRelation : opportunity.commercialRelations()){
			if(commercialRelation.commercialSubject().equals(newCommercialSubject)){
				Assert.assertEquals(condition, commercialRelation.conditions().values().iterator().next());
			} else {
				Assert.assertEquals(0,commercialRelation.conditions().size());
			}
		}
		
		
	}
	
	@Test
	public final void removeCommercialSubject() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialRelation.commercialSubject()).thenReturn(commercialSubject);
		
		@SuppressWarnings("unchecked")
		final Collection<CommercialRelation> relations = (Collection<CommercialRelation>) ReflectionTestUtils.getField(opportunity, "commercialRelations");
		
		relations.add(commercialRelation);
		
		opportunity.remove(commercialSubject);
		
		Assert.assertEquals(0, relations.size());
	}
	
	
	@Test
	public final void removeCommercialSubjectNotFound() {
		
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialRelation.commercialSubject()).thenReturn(commercialSubject);
		
		@SuppressWarnings("unchecked")
		final Collection<CommercialRelation> relations = (Collection<CommercialRelation>) ReflectionTestUtils.getField(opportunity, "commercialRelations");
		
		relations.add(commercialRelation);
		
		
		opportunity.remove(Mockito.mock(CommercialSubject.class));
		Assert.assertEquals(1, relations.size());
		
	}
	
	@Test
	public final void removeCondition() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		final CommercialRelation relation = new CommercialRelationImpl(commercialSubject,opportunity);
		final Condition  condition =  Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.Quantity);
		relation.assign(condition);
		
		@SuppressWarnings("unchecked")
		final Collection<CommercialRelation> relations = (Collection<CommercialRelation>) ReflectionTestUtils.getField(opportunity, "commercialRelations");
		
		relations.add(relation);
		
		Assert.assertEquals(1, relations.size());
		Assert.assertEquals(1, relations.iterator().next().conditions().values().size());
		opportunity.remove(commercialSubject, ConditionType.Quantity);
		
		Assert.assertEquals(1, relations.size());
		Assert.assertEquals(0, relations.iterator().next().conditions().values().size());
		
		
		
	}
	
	@Test
	public final void removeConditionSubjectNotExists() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		final CommercialRelation relation = new CommercialRelationImpl(commercialSubject,opportunity);
		final Condition  condition =  Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.Quantity);
		relation.assign(condition);
		@SuppressWarnings("unchecked")
		final Collection<CommercialRelation> relations = (Collection<CommercialRelation>) ReflectionTestUtils.getField(opportunity, "commercialRelations");
		
		relations.add(relation);
		Assert.assertEquals(1, relations.size());
		Assert.assertEquals(1, relations.iterator().next().conditions().values().size());
		
		opportunity.remove(Mockito.mock(CommercialSubject.class), ConditionType.Quantity);
		
		Assert.assertEquals(1, relations.size());
		Assert.assertEquals(1, relations.iterator().next().conditions().values().size());
	}
	
	
	@Test
	public final void urlForName() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		ReflectionTestUtils.setField(opportunity, "id", ID);
		Map<String,String> docs = new HashMap<>();
		docs.put(IMAGE, String.format(OpportunityImpl.URL, ID, IMAGE));
		ReflectionTestUtils.setField(opportunity , "storedDocuments", docs);
		
		Assert.assertEquals(String.format(OpportunityImpl.URL, ID, IMAGE), opportunity.urlForName(IMAGE));
		
	}
	
	@Test
	public final void urlForNameNotFound() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		Assert.assertNull(opportunity.urlForName(IMAGE));
	}
	
	@Test
	public final void assignLink() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		ReflectionTestUtils.setField(opportunity, "id", ID);
		opportunity.assignDocument(IMAGE);
		
		Assert.assertEquals(1, opportunity.documents().size());
		Assert.assertEquals(IMAGE, opportunity.documents().keySet().iterator().next());
		Assert.assertEquals(String.format(OpportunityImpl.URL, ID, IMAGE), new String(opportunity.documents().values().iterator().next()));
	}
	
	@Test
	public final void assignWebLink() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		opportunity.assignWebLink(WEB_LINK);
		Assert.assertEquals(String.format(OpportunityImpl.WWW_URL, WEB_LINK), opportunity.urlForName(WEB_LINK));
		Assert.assertTrue(opportunity.urlForName("kylie.com").startsWith("http://"));
		
		System.out.println(opportunity.urlForName("kylie.com"));
	}
	
}
