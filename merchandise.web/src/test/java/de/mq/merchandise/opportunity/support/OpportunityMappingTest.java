package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.util.EntityUtil;



public class OpportunityMappingTest {
	
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
		  final OpportunityAO web = proxyFactory.createProxy(OpportunityAO.class, new ModelRepositoryBuilderImpl().withDomain(opportunity).withBeanResolver(beanResolver).build());

		  Assert.assertEquals(""+ ID, web.getId());
	      Assert.assertEquals(NAME, web.getName());
	      Assert.assertEquals(DESCRIPTION, web.getDescription());
	      Assert.assertEquals(1, web.getKeyWords().size());
	      Assert.assertEquals(KEY_WORD, web.getKeyWords().iterator().next());
	      Assert.assertEquals(customer, web.getCustomer().getCustomer());
	      Assert.assertEquals(""+CUSTOMER_ID, web.getCustomer().getId());
	      Assert.assertEquals(Opportunity.Kind.Tender.name(), web.getKind());
	}
}
