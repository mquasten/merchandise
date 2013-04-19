package de.mq.merchandise.model;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerAO;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.NaturalPersonAO;
import de.mq.merchandise.model.RegistrationImpl;
import de.mq.merchandise.model.User;
import de.mq.merchandise.model.Registration.Kind;

public class RegistrationTest {
	


	private static final String LANGUAGE = "language";

	@Test
	public final void personMap() {
		
		final LegalPersonAO  legalPersonAO = Mockito.mock(LegalPersonAO.class);
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		final User user = Mockito.mock(User.class);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		final RegistrationImpl registration= new RegistrationImpl(legalPersonAO, naturalPersonAO,user, customer);
		
		Assert.assertEquals(legalPersonAO, registration.person(Kind.LegalPerson));
		Assert.assertEquals(naturalPersonAO, registration.person(Kind.NaturalPerson));
		Assert.assertEquals(naturalPersonAO, registration.person(Kind.User));
	}
	
	@Test
	public final void kindString() {
		final LegalPersonAO  legalPersonAO = Mockito.mock(LegalPersonAO.class);
		final User user = Mockito.mock(User.class);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		final RegistrationImpl registration= new RegistrationImpl(legalPersonAO, naturalPersonAO, user, customer);
		Assert.assertEquals(Kind.NaturalPerson.name(), registration.getKind());
		registration.setKind(Kind.LegalPerson.name());
		Assert.assertEquals(Kind.LegalPerson.name(), registration.getKind());
		
		registration.setKind(null);
		Assert.assertEquals(Kind.LegalPerson.name(), registration.getKind());
		
		
	}
	
	
	
	
	
	@Test
	public final void person() {
		final LegalPersonAO  legalPersonAO = Mockito.mock(LegalPersonAO.class);
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		final User user = Mockito.mock(User.class);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		final RegistrationImpl registration= new RegistrationImpl(legalPersonAO, naturalPersonAO,user, customer);
		
		Assert.assertEquals(naturalPersonAO, registration.getPerson());
		registration.setKind(Kind.LegalPerson.name());
		
		Assert.assertEquals(legalPersonAO, registration.getPerson());
	}
	
	@Test
	public final void Language() {
		final LegalPersonAO  legalPersonAO = Mockito.mock(LegalPersonAO.class);
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		final User user = Mockito.mock(User.class);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Mockito.when(naturalPersonAO.getLanguage()).thenReturn(LANGUAGE);
		final RegistrationImpl registration= new RegistrationImpl(legalPersonAO, naturalPersonAO,user, customer);
		
		Assert.assertEquals(LANGUAGE, registration.getLanguage());
		
		
	}
	
	@Test
	public final void assignLanguage() {
		final LegalPersonAO  legalPersonAO = Mockito.mock(LegalPersonAO.class);
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		final User user = Mockito.mock(User.class);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		final RegistrationImpl registration= new RegistrationImpl(legalPersonAO, naturalPersonAO,user, customer);
		registration.setLanguage(LANGUAGE);
		
		Mockito.verify(user).setLanguage(LANGUAGE);
		Mockito.verify(naturalPersonAO).setLanguage(LANGUAGE);
		
	}
	
	@Test
	public final void customer() {
		final LegalPersonAO  legalPersonAO = Mockito.mock(LegalPersonAO.class);
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		final User user = Mockito.mock(User.class);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		final RegistrationImpl registration= new RegistrationImpl(legalPersonAO, naturalPersonAO,user, customer);
		Assert.assertEquals(customer, registration.getCustomer());
	}
	
	@Test
	public final void customerDomain() {
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customerAO.getCustomer()).thenReturn(customer);
		final RegistrationImpl registration= new RegistrationImpl(Mockito.mock(LegalPersonAO.class), Mockito.mock(NaturalPersonAO.class),Mockito.mock(User.class), customerAO);
		Assert.assertEquals(customer, registration.customer());
	}
	
	@Test
	public final void assignCustomer() {
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		final Customer customer = Mockito.mock(Customer.class);
	
		final RegistrationImpl registration= new RegistrationImpl(Mockito.mock(LegalPersonAO.class), Mockito.mock(NaturalPersonAO.class),Mockito.mock(User.class), customerAO);
		
		registration.assign(customer);
		Mockito.verify(customerAO).setCustomer(customer);
	}
	
	@Test
	public final void kind() {
		final RegistrationImpl registration= new RegistrationImpl(Mockito.mock(LegalPersonAO.class), Mockito.mock(NaturalPersonAO.class),Mockito.mock(User.class), Mockito.mock(CustomerAO.class));
	    registration.setKind(Kind.User.name());
	    Assert.assertEquals(Kind.User, registration.kind());
	}
	

}
