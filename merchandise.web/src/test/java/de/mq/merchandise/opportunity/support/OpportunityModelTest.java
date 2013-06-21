package de.mq.merchandise.opportunity.support;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;
import de.mq.merchandise.util.EntityUtil;


public class OpportunityModelTest {
	
private static final String PATTERN = "Pattern";

private static final long OPPORTUNITY_ID = 4711L;

private static final String OPPORTUNITY_DESCRIPTION = "Some special services";

private static final String OPPORTUNITY_NAME = "Nicoles Escortservice";

private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	
	@Test
	public final void model() {
		
		final PagingAO pagingAO = Mockito.mock(PagingAO.class);
		final OpportunityModelAO web = proxyFactory.createProxy(OpportunityModelAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withMapEntry("paging", pagingAO).build());
	    final Customer customer = EntityUtil.create(CustomerImpl.class);
	    EntityUtil.setId(customer, 19680528L);
		final Opportunity opportunity = new OpportunityImpl(customer,OPPORTUNITY_NAME , OPPORTUNITY_DESCRIPTION, Kind.Offer);
		EntityUtil.setId(opportunity, OPPORTUNITY_ID);
		final Set<Opportunity> opportunities = new HashSet<Opportunity>(); 
		opportunities.add(opportunity);
		web.setOpportunities(opportunities);
		
		Assert.assertEquals(1, web.getOpportunities().size());
		Assert.assertEquals(OPPORTUNITY_NAME, web.getOpportunities().get(0).getName());
		Assert.assertEquals(OPPORTUNITY_DESCRIPTION, web.getOpportunities().get(0).getDescription());
		Assert.assertEquals(Kind.Offer.name(), web.getOpportunities().get(0).getKind());
		Assert.assertEquals(""+ OPPORTUNITY_ID, web.getOpportunities().get(0).getId());
		
		Assert.assertNull(web.getSelected());
		
		final OpportunityAO opportunityAO = Mockito.mock(OpportunityAO.class);
		web.setSelected(opportunityAO);
		Assert.assertEquals(opportunityAO, web.getSelected());
		
		Assert.assertNull(web.getPattern());
		web.setPattern(PATTERN);
		
		Assert.assertEquals(PATTERN, web.getPattern());
		
		Assert.assertEquals(pagingAO, web.getPaging());
		
	
	}

}
