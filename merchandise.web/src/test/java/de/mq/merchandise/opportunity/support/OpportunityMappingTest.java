package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.TreeNode;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.util.EntityUtil;



public class OpportunityMappingTest {
	
	private static final String P01 = "P01";

	private static final String ACTIVITY_ID = "AC01";

	private static final long CUSTOMER_ID = 4711L;

	private static final String KEY_WORD = "keyWord";

	private static final String DESCRIPTION = "Nicole Service  with Conditions";

	private static final String NAME = "Special Escort Service";

	private static final long ID = 19680528L;

	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl();
	
	private static final CustomerImpl customer = EntityUtil.create(CustomerImpl.class);
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	@Test
	public final void toWeb() {
		
		  EntityUtil.setId(customer, CUSTOMER_ID);
		  final Opportunity opportunity = new OpportunityImpl(customer, NAME, DESCRIPTION, Opportunity.Kind.Tender);
		  opportunity.assignKeyWord(KEY_WORD);
	      EntityUtil.setId(opportunity, ID);
	      final ActivityClassification activityClassification = new ActivityClassificationImpl();
	      final ProductClassification procuctClassification = new ProductClassificationImpl();
	      EntityUtil.setId(activityClassification, ACTIVITY_ID);
	      EntityUtil.setId(procuctClassification, P01);
	      opportunity.assignClassification(activityClassification);
	      opportunity.assignClassification(procuctClassification);
	      
	      
		  final OpportunityAO web = proxyFactory.createProxy(OpportunityAO.class, new ModelRepositoryBuilderImpl().withDomain(opportunity).withBeanResolver(beanResolver).build());

		  Assert.assertEquals(""+ ID, web.getId());
	      Assert.assertEquals(NAME, web.getName());
	      Assert.assertEquals(DESCRIPTION, web.getDescription());
	      Assert.assertEquals(1, ((Collection<?>)web.getKeyWords()).size());
	      Assert.assertEquals(KEY_WORD, web.getKeyWords().iterator().next());
	      Assert.assertEquals(customer, web.getCustomer().getCustomer());
	      Assert.assertEquals(""+CUSTOMER_ID, web.getCustomer().getId());
	      Assert.assertEquals(Opportunity.Kind.Tender.name(), web.getKind());
	      Assert.assertEquals(1, web.getActivityClassifications().size());
	      Assert.assertEquals(ACTIVITY_ID, web.getActivityClassifications().iterator().next().getId());
	      Assert.assertEquals(1, web.getProcuctClassifications().size());
	      Assert.assertEquals(opportunity, web.getOpportunity());
	      
	    
	}
	
	
	@Test
	public final void toDomain() {
		final Opportunity opportunity = new OpportunityImpl();
		final OpportunityAO web = proxyFactory.createProxy(OpportunityAO.class, new ModelRepositoryBuilderImpl().withDomain(opportunity).withBeanResolver(beanResolver).build());
	
	    web.setId(""+ ID); 
	    web.setName(NAME);
	    web.setDescription(DESCRIPTION);
	    Assert.assertEquals(opportunity, web.getOpportunity()); 
	    final TreeNode node = Mockito.mock(TreeNode.class);
	    ActivityClassification activityClassification = Mockito.mock(ActivityClassification.class);
	    Mockito.when(node.getData()).thenReturn(activityClassification);
	  
	     
	    Assert.assertEquals(ID, opportunity.id());
	    Assert.assertEquals(NAME, opportunity.name());
	    Assert.assertEquals(DESCRIPTION, opportunity.description());
	    Opportunity newppOpportunity =EntityUtil.create(OpportunityImpl.class);
	    web.setOpportunity(newppOpportunity);
	    Assert.assertEquals(newppOpportunity, web.getOpportunity());
	    
	
	}
	
	
}
