package de.mq.merchandise.customer.support;

import java.util.Date;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;

import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.AddressImpl;
import de.mq.merchandise.contact.support.AddressTestConstants;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.LegalPersonImpl;
import de.mq.merchandise.customer.support.TradeRegisterBuilderFactoryImpl;
import de.mq.merchandise.customer.support.TradeRegisterImpl;
import de.mq.merchandise.model.PersonTestConstants;
import de.mq.merchandise.util.EntityUtil;

public class LegalPersonMappingTest {
	
	



	private static final Date FOUNDATION_DATE = new Date();
	private BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();

	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	@Test
	public final void toAO() {
		
		final TradeRegister tradeRegister =  new TradeRegisterBuilderFactoryImpl().tradeRegisterBuilder().withCity(PersonTestConstants.CITY).withZipCode(PersonTestConstants.ZIPCODE).withReference(PersonTestConstants.REFERENCE).withRegistrationDate(PersonTestConstants.REGISTRATION_DATE).build();
		
		
		
		final LegalPerson legalPerson = new LegalPersonImpl(PersonTestConstants.NAME,PersonTestConstants.TAX_ID,tradeRegister,LegalForm.AG, FOUNDATION_DATE, Locale.US);
		
		ReflectionTestUtils.setField(legalPerson, "password", PersonTestConstants.PASSWORD);
		final CityAddress address = EntityUtil.create(AddressImpl.class);
		ReflectionTestUtils.setField(address, "id", Long.valueOf(AddressTestConstants.ADDRESS_ID));
		legalPerson.assign(address);
		
		
		
		final LegalPersonAO web = proxyFactory.createProxy(LegalPersonAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(legalPerson).build());
		
		Assert.assertEquals(PersonTestConstants.NAME, web.getName());
		Assert.assertEquals(PersonTestConstants.TAX_ID, web.getTaxId());
		Assert.assertEquals(FOUNDATION_DATE, web.getFoundationDate());
		Assert.assertEquals(LegalForm.AG.name(), web.getLegalForm());
		Assert.assertEquals(PersonTestConstants.PASSWORD, web.getPassword());
		
		Assert.assertEquals(tradeRegister.reference(), web.getTradeRegister().getReference());
		Assert.assertEquals(tradeRegister.registrationDate(), web.getTradeRegister().getRegistrationDate());
		Assert.assertEquals(tradeRegister.city(), web.getTradeRegister().getCity());
		Assert.assertEquals(tradeRegister.zipCode(), web.getTradeRegister().getZipCode());
		
		Assert.assertEquals(legalPerson.locale().getCountry(), web.getCountry());
		Assert.assertEquals(legalPerson.locale().getLanguage(), web.getLanguage());
		Assert.assertEquals(1, web.getAddresses().size());
		Assert.assertEquals(AddressTestConstants.ADDRESS_ID, ((AddressAO)web.getAddresses().iterator().next()).getId());
		
		
	}
	
	@Test
	public final void toDomain() {
		final LegalPersonImpl domain = new LegalPersonImpl(null, null, EntityUtil.create(TradeRegisterImpl.class), null, null);
		
		
		final LegalPersonAO web = proxyFactory.createProxy(LegalPersonAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(domain).build()); 
		web.setName(PersonTestConstants.NAME);
		web.setFoundationDate(FOUNDATION_DATE);
		web.setLegalForm(LegalForm.AG.name());
		web.setTaxId(PersonTestConstants.TAX_ID);
		web.setPassword(PersonTestConstants.PASSWORD);
		web.setConfirmedPassword(PersonTestConstants.CONFIRMED_PASSWORD);
		web.setCountry(Locale.US.getCountry());
		web.setLanguage(Locale.US.getLanguage());
	
		web.getTradeRegister().setReference(PersonTestConstants.REFERENCE);
		web.getTradeRegister().setRegistrationDate(PersonTestConstants.REGISTRATION_DATE);
		web.getTradeRegister().setZipCode(PersonTestConstants.ZIPCODE);
		web.getTradeRegister().setCity(PersonTestConstants.CITY);
		
		Assert.assertEquals(PersonTestConstants.CONFIRMED_PASSWORD, web.getConfirmedPassword());
		
		Assert.assertEquals(PersonTestConstants.NAME, web.getPerson().name());
		Assert.assertEquals(PersonTestConstants.TAX_ID,  web.getPerson().taxId());
		Assert.assertEquals(FOUNDATION_DATE,  web.getPerson().foundationDate());
		Assert.assertEquals(LegalForm.AG,  web.getPerson().legalForm());
		Assert.assertEquals(PersonTestConstants.PASSWORD, ReflectionTestUtils.getField(web.getPerson(), "password"));
		Assert.assertEquals(Locale.US, web.getPerson().locale());
		
		
		Assert.assertEquals(PersonTestConstants.REFERENCE, web.getPerson().tradeRegister().reference());
		Assert.assertEquals(PersonTestConstants.REGISTRATION_DATE, web.getPerson().tradeRegister().registrationDate());
		Assert.assertEquals(PersonTestConstants.ZIPCODE, web.getPerson().tradeRegister().zipCode());
		Assert.assertEquals(PersonTestConstants.CITY, web.getPerson().tradeRegister().city());
		
		
	}
	
	

}
