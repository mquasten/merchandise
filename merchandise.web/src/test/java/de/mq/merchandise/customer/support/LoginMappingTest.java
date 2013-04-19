package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.PersonTestConstants;
import de.mq.merchandise.model.support.HibernateProxyConverter;

public class LoginMappingTest {

	private static final long CUSTOMER_ID = 19680528L;
	private static final String LOGIN = "skype:KinkyKylie";
	private static final String PASSWORD = "fever";
	private final AOProxyFactory proxyFactory  = new  BeanConventionCGLIBProxyFactory();
	private final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	final Person person = new NaturalPersonImpl(PersonTestConstants.FIRSTNAME,PersonTestConstants.LASTNAME, new NativityImpl(PersonTestConstants.BIRTH_PLACE, PersonTestConstants.BIRTH_DATE));
	final Customer customer = new CustomerImpl(person);
	
	
	@Before
	public final void setup() {
		Mockito.when(beanResolver.getBeanOfType(HibernateProxyConverter.class)).thenReturn(new HibernateProxyConverter());
	    Mockito.when(beanResolver.getBeanOfType(PersonSelector.class)).thenReturn(new PersonSelector());
	    Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(proxyFactory);
		Mockito.when(beanResolver.getBeanOfType(NoConverter.class)).thenReturn(new NoConverter());
		Mockito.when(beanResolver.getBeanOfType(Number2StringConverter.class)).thenReturn(new Number2StringConverter());
		ReflectionTestUtils.setField(customer, "id", CUSTOMER_ID);
	}
	

	@Test
	public final void toDomain() {
		final LoginAO login = proxyFactory.createProxy(LoginAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).build() );
	    login.setPassword(PASSWORD) ;
	    login.setUser(LOGIN);
	    login.setCustomer(customer);
	    final List<Customer> customers = new ArrayList<>();
	    customers.add(customer);
	    login.setCustomers(customers);
	    login.setPerson(person);
	    Assert.assertEquals(PASSWORD, login.getMap().get("password"));
	    Assert.assertEquals(LOGIN, login.getMap().get("user"));
	    Customer  resultCustomer = (Customer) login.getMap().get("customer");
	    Assert.assertEquals(CUSTOMER_ID, resultCustomer.id() );
	    Assert.assertEquals(person, resultCustomer.person() );
		@SuppressWarnings("unchecked")
	    final List<Customer> resultCustomers = (List<Customer>) login.getMap().get("customers");
	    Assert.assertEquals(1, resultCustomers.size());
	    Assert.assertEquals(customer, resultCustomers.get(0));
	    Assert.assertEquals(person, login.getMap().get("person"));
	}
	
	
	
	@Test
	public final void toWeb() {
		final List<Customer> customers = new ArrayList<>();
		customers.add(customer);
		
		final LoginAO login = proxyFactory.createProxy(LoginAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withMapEntry("user", LOGIN).withMapEntry("password", PASSWORD).withMapEntry("person", person).withMapEntry("customer", customer).withMapEntry("customers", customers).build());
	    Assert.assertEquals(PASSWORD , login.getPassword());
	    Assert.assertEquals(LOGIN, login.getUser());
        final NaturalPersonAO naturalPerson = (NaturalPersonAO) login.getPerson();
        Assert.assertEquals(PersonTestConstants.FIRSTNAME, naturalPerson.getFirstName());
        Assert.assertEquals(PersonTestConstants.LASTNAME, naturalPerson.getLastName());
        Assert.assertEquals(PersonTestConstants.BIRTH_PLACE, naturalPerson.getNativity().getBirthPlace());
        Assert.assertEquals(PersonTestConstants.BIRTH_DATE, naturalPerson.getNativity().getBirthDate());
        Assert.assertEquals(String.valueOf(CUSTOMER_ID), login.getCustomer().getId());
        
        Assert.assertEquals(1, login.getCustomers().size());
        Assert.assertEquals(String.valueOf(CUSTOMER_ID), login.getCustomers().get(0).getId());
        
        
        
	}
	
}
