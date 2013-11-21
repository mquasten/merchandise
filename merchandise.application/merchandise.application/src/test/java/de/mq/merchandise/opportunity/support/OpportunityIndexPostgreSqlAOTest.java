package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;

public class OpportunityIndexPostgreSqlAOTest {
	
private static final String KEYWORD = "Begleitung";

private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	final Customer customer = Mockito.mock(Customer.class);
	
	private final Opportunity opportunity = new OpportunityImpl(customer, "Pet Store", "Here special Services", Opportunity.Kind.Offer);
	
	
	private Person person = Mockito.mock(Person.class);
	
	private Address address = Mockito.mock(Address.class);
	
	private Coordinates coordinates = Mockito.mock(Coordinates.class);
	
	
	@Test
	public final void toAO() {
		Mockito.when(customer.person()).thenReturn(person);
		Mockito.when(person.name()).thenReturn("Nicoles Escort Argentur");
		
		opportunity.assignKeyWord(KEYWORD);
		Mockito.when(address.coordinates()).thenReturn(coordinates);
		Mockito.when(coordinates.longitude()).thenReturn(44.5858333);
		Mockito.when(coordinates.latitude()).thenReturn(48.8047222);
		opportunity.assign(address);
		
		 final OpportunityIndexPostgreSqlAO ao = proxyFactory.createProxy(OpportunityIndexPostgreSqlAO.class, new ModelRepositoryBuilderImpl().withDomain(opportunity).withBeanResolver(beanResolver).build());
		 Assert.assertEquals(String.format("%s %s %s %s %s", opportunity.name(), opportunity.description(), opportunity.customer().person().name(), KEYWORD , opportunity.kind()), ao.getTS());
	     final Collection<String> points = ao.getPoints();
	     Assert.assertEquals(1, points.size());
	     Assert.assertEquals(String.format("POINT(%s %s)", coordinates.longitude(), coordinates.latitude()), points.iterator().next());
	}

}
