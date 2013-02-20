package de.mq.merchandise.customer.support;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;

import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.customer.support.CustomerAO;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.LegalPersonImpl;
import de.mq.merchandise.customer.support.NaturalPersonAO;
import de.mq.merchandise.customer.support.NaturalPersonImpl;
import de.mq.merchandise.util.EntityUtil;

public class CustomerMappingTest {
	
	private static final String TAX_ID = "taxId";
	private static final String LAST_NAME = "Minogue";
	private static final String FIRST_NAME = "Kylie";
	private static final String ID = "19680528";
	private final AOProxyFactory proxyFactory  = new  BeanConventionCGLIBProxyFactory();
	private final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
	
	@Test
	public final void toDomain() {
		final NaturalPerson person = EntityUtil.create(NaturalPersonImpl.class);
		final Customer customer = new CustomerImpl(person);
		
		
		final CustomerAO customerAO = proxyFactory.createProxy(CustomerAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(customer).build());
		customerAO.setId(ID);
		
		final NaturalPersonAO personAO = (NaturalPersonAO) customerAO.getPerson();
		personAO.setFirstName(FIRST_NAME);
		personAO.setLastName(LAST_NAME);
		
		Assert.assertEquals(ID,  customerAO.getId());
	//	Assert.assertEquals((Long) ID,  customer.id());
		Assert.assertEquals(customer, customerAO.getCustomer());
		
		Assert.assertEquals(LAST_NAME, person.name());
		Assert.assertEquals(FIRST_NAME, person.firstname());
	}
	
	@Test
	public final void toAO() {
		final LegalPerson person = new LegalPersonImpl(LAST_NAME, TAX_ID, Mockito.mock(TradeRegister.class), LegalForm.AG, new Date());
		final Customer customer = new CustomerImpl(person);
		ReflectionTestUtils.setField(customer, "id", Long.valueOf(ID));
		CustomerAO customerAO = proxyFactory.createProxy(CustomerAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(customer).build());
		Assert.assertEquals(ID, customerAO.getId());
		Assert.assertEquals(LAST_NAME, ((LegalPersonAO)customerAO.getPerson()).getName());
		Assert.assertEquals(TAX_ID, ((LegalPersonAO)customerAO.getPerson()).getTaxId());
	
	}

}
