package de.mq.merchandise.opportunity.support;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.util.EntityUtil;

public class OpportunityIndexTest {
	
	private final Opportunity opportunity = Mockito.mock(Opportunity.class); 
	
	
	private Address address = Mockito.mock(Address.class);
	
	@Before
	public final void prepare() {
		Mockito.when(opportunity.id()).thenReturn(19680528L);
		Mockito.when(address.id()).thenReturn(4711L);
	}
	
	@Test
	public final void createTS() {
		final OpportunityIndex opportunityIndex = new OpportunityFullTextSearchIndexImpl(opportunity);
		Assert.assertEquals(opportunity, opportunityIndex.opportunity());
		Assert.assertEquals(new UUID(opportunity.id(), 0L).toString(), opportunityIndex.id());
	}
	
	@Test
	public final void hash(){
		Assert.assertEquals(new UUID(opportunity.id(), 0L).toString().hashCode(),new OpportunityFullTextSearchIndexImpl(opportunity).hashCode());
	}
	
	@Test
	public final void equals() {
		final Opportunity anOtherOpportunity = Mockito.mock(Opportunity.class);
		Mockito.when(anOtherOpportunity.id()).thenReturn(4711L);
		
		Assert.assertTrue(new OpportunityFullTextSearchIndexImpl(opportunity).equals(new OpportunityFullTextSearchIndexImpl(opportunity)));
		Assert.assertFalse(new OpportunityFullTextSearchIndexImpl(opportunity).equals(new OpportunityFullTextSearchIndexImpl(anOtherOpportunity)));
	}
	
	@Test
	public final void defaultConstructorTS() {
		final OpportunityIndex opportunityIndex = EntityUtil.create(OpportunityFullTextSearchIndexImpl.class);
		Assert.assertNull(opportunityIndex.opportunity());
		Assert.assertNull(opportunityIndex.id());
	}
	
	@Test
	public final void createGIS() {
		final OpportunityGeoLocationIndexImpl opportunityIndex = new OpportunityGeoLocationIndexImpl(opportunity, address);
		Assert.assertEquals(opportunity, opportunityIndex.opportunity());
		Assert.assertEquals(address, opportunityIndex.address());
		Assert.assertEquals(new UUID(opportunity.id(), address.id()).toString(), opportunityIndex.id());
	}

	@Test
	public final void createGis() {
		final OpportunityGeoLocationIndexImpl opportunityIndex = EntityUtil.create(OpportunityGeoLocationIndexImpl.class);
		Assert.assertNull(opportunityIndex.opportunity());
		Assert.assertNull(opportunityIndex.id());
		Assert.assertNull(opportunityIndex.address());
	}
}
