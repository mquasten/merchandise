package de.mq.merchandise.opportunity.support;

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
import de.mq.merchandise.util.EntityUtil;

public class CommercialSubjectMappingTest {
	
	private static final String DESCRIPTION = "Nicole special Services";

	private static final String NAME = "Escort-Service";

	private static final CustomerImpl customer = EntityUtil.create(CustomerImpl.class);

	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	@Test
	public final void toWeb() {
		final CommercialSubject commercialSubject = new CommercialSubjectImpl(customer, NAME, DESCRIPTION);
		EntityUtil.setId(commercialSubject, 19680528L);
		final CommercialSubjectAO web = proxyFactory.createProxy(CommercialSubjectAO.class, new ModelRepositoryBuilderImpl().withDomain(commercialSubject).withBeanResolver(beanResolver).build());
		Assert.assertEquals(commercialSubject.name(), web.getName());
		Assert.assertEquals(commercialSubject.description(), web.getDescription());
		Assert.assertEquals(""+commercialSubject.id(), web.getId());
		Assert.assertEquals(commercialSubject, web.getCommercialSubject());
		Assert.assertEquals(customer, web.getCustomer().getCustomer());
	}
	
	@Test
	public final void toDomain() {
		final CommercialSubject commercialSubject =  new CommercialSubjectImpl( Mockito.mock(Customer.class) ,null, null);
		final CommercialSubjectAO web = proxyFactory.createProxy(CommercialSubjectAO.class, new ModelRepositoryBuilderImpl().withDomain(commercialSubject).withBeanResolver(beanResolver).build());
	    web.setName(NAME);
	    web.setDescription(DESCRIPTION);
	   
	    Assert.assertEquals(NAME,  commercialSubject.name());
	    Assert.assertEquals(DESCRIPTION,  commercialSubject.description());
	    Assert.assertEquals(commercialSubject, web.getCommercialSubject()) ; 
	   
	    final CommercialSubject newCommercialSubject =  EntityUtil.create(CommercialSubjectImpl.class);
	    web.setCommercialSubject(newCommercialSubject);
	    Assert.assertEquals(newCommercialSubject, web.getCommercialSubject());
	}

}
