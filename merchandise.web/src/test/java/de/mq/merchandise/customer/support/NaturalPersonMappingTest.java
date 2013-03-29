package de.mq.merchandise.customer.support;

import java.util.Collection;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.AddressImpl;
import de.mq.merchandise.contact.support.AddressTestConstants;
import de.mq.merchandise.customer.NativityBuilder;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.support.Digest.Algorithm;
import de.mq.merchandise.model.PersonTestConstants;
import de.mq.merchandise.util.EntityUtil;


public class NaturalPersonMappingTest {
	

	private final AOProxyFactory proxyFactory =  new BeanConventionCGLIBProxyFactory();
	
	
	private final NativityBuilder nativityBuilder = new NativityBuilderFactoryImpl().nativityBuilder();
	private final  BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void toAO() {
		
		final NaturalPerson person = new NaturalPersonImpl(PersonTestConstants.FIRSTNAME, PersonTestConstants.LASTNAME, nativityBuilder.withBirthDate(PersonTestConstants.BIRTH_DATE).withBirthPlace(PersonTestConstants.BIRTH_PLACE).build(), Locale.US);
	    person.digest().assignDigest(PersonTestConstants.PASSWORD, Algorithm.UNCRYPTED);
	    
	    final Address address = EntityUtil.create(AddressImpl.class);
	    ReflectionTestUtils.setField(address, "id", Long.parseLong(AddressTestConstants.ADDRESS_ID));
	    ((Collection<CityAddress>) ReflectionTestUtils.getField(person, "addresses")).add(address);
		
		final NaturalPersonAO result = proxyFactory.createProxy(NaturalPersonAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(person).build());
	    Assert.assertEquals(PersonTestConstants.FIRSTNAME, result.getFirstName());
	    Assert.assertEquals(PersonTestConstants.LASTNAME, result.getLastName());
	    Assert.assertEquals(PersonTestConstants.PASSWORD, result.getDigest().getDigest());
	    Assert.assertEquals(PersonTestConstants.BIRTH_PLACE, result.getNativity().getBirthPlace());
	    Assert.assertEquals(PersonTestConstants.BIRTH_DATE, result.getNativity().getBirthDate());
	    Assert.assertEquals(person.locale().getCountry(), result.getCountry());
	    Assert.assertEquals(person.locale().getLanguage(), result.getLanguage());
	    Assert.assertEquals(1, result.getAddresses().size());
	    Assert.assertEquals(AddressTestConstants.ADDRESS_ID, (((AddressAO) result.getAddresses().iterator().next()).getId()));
	}
	
	@Test
	public final void toDomain() {
		final NaturalPerson domain = new NaturalPersonImpl(null, null, nativityBuilder.build());
		final NaturalPersonAO naturalPersonAO = proxyFactory.createProxy(NaturalPersonAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(domain).build());
		
		naturalPersonAO.setFirstName(PersonTestConstants.FIRSTNAME);
		naturalPersonAO.setLastName(PersonTestConstants.LASTNAME);
		naturalPersonAO.getDigest().setDigest(PersonTestConstants.PASSWORD);
		naturalPersonAO.setCountry(Locale.US.getCountry());
		naturalPersonAO.setLanguage(Locale.US.getLanguage());
		naturalPersonAO.getDigest().setConfirmedDigest(PersonTestConstants.CONFIRMED_PASSWORD);
		naturalPersonAO.getNativity().setBirthDate(PersonTestConstants.BIRTH_DATE);
		naturalPersonAO.getNativity().setBirthPlace(PersonTestConstants.BIRTH_PLACE);
		
		Assert.assertEquals(PersonTestConstants.CONFIRMED_PASSWORD, naturalPersonAO.getDigest().getConfirmedDigest());
		Assert.assertEquals(PersonTestConstants.FIRSTNAME, domain.firstname());
		Assert.assertEquals(PersonTestConstants.LASTNAME, domain.name());
		Assert.assertEquals(PersonTestConstants.PASSWORD, ReflectionTestUtils.getField(domain.digest(), "digest"));
		Assert.assertEquals(PersonTestConstants.BIRTH_PLACE, domain.nativity().birthPlace());
		Assert.assertEquals(PersonTestConstants.BIRTH_DATE, domain.nativity().birthDate());
		
		
	}

}
