package de.mq.merchandise.opportunity.support;

import java.util.Collection;


import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.CommercialSubject.DocumentType;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;

public class OpportunityTest {
	
	private static final String KEYWORD = "keyword";
	private static final String DOCUMENT_NAME = "document";
	private static final byte[] DOCUMENT = "documentsInput".getBytes();
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
		opportunity.assignDocument(DOCUMENT_NAME, DocumentType.PDF, DOCUMENT);
		Assert.assertEquals(1, opportunity.documents().size());
		Assert.assertEquals(DocumentType.PDF.key(DOCUMENT_NAME), opportunity.documents().keySet().iterator().next());
		Assert.assertEquals(DOCUMENT, opportunity.documents().values().iterator().next());
		opportunity.removeDocument(DOCUMENT_NAME, DocumentType.PDF);
		Assert.assertTrue(opportunity.documents().isEmpty());
	}
	
	@Test
    public final void productClassifications() {
		final Opportunity opportunity = new OpportunityImpl(customer , NAME);
		final ProcuctClassification procuctClassification = Mockito.mock(ProcuctClassification.class);
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
}
