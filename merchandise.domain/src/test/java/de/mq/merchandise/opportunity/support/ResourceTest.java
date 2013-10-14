package de.mq.merchandise.opportunity.support;

import org.junit.Test;


import junit.framework.Assert;

public class ResourceTest {
	@Test
	public void resources() {
		Assert.assertEquals(2, Resource.values().length);
		boolean subject=false;
		boolean opportunity=false;
		for(final Resource resource : Resource.values()){
			if( resource == Resource.Opportunity){
				Assert.assertEquals(OpportunityImpl.class, resource.entityClass());
				Assert.assertEquals("opportunities", resource.urlPart());
				opportunity=true;
			}
			if( resource == Resource.Subject){
				Assert.assertEquals(CommercialSubjectImpl.class, resource.entityClass());
				Assert.assertEquals("subjects", resource.urlPart());
				subject=true;
			}
			Assert.assertEquals(resource, Resource.valueOf(resource.name()));
		}
		
		Assert.assertTrue(subject);
		Assert.assertTrue(opportunity);
	}

}
