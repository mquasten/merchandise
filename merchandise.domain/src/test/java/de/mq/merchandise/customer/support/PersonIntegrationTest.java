package de.mq.merchandise.customer.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.ContactBuilderFactory;
import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.PersonRole;
import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.customer.support.LegalPersonImpl;
import de.mq.merchandise.customer.support.NativityImpl;
import de.mq.merchandise.customer.support.NaturalPersonImpl;
import de.mq.merchandise.customer.support.TradeRegisterImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/emf.xml"})
public class PersonIntegrationTest {
	
	private ContactBuilderFactory contactBuilderFactory = new ContactBuilderFactoryImpl();
		
		
	
	@PersistenceContext()
	private EntityManager entityManager;
	private final List<BasicEntity> waste  = new ArrayList<BasicEntity>();
	
	
	
	@After
	public final void setup() {
		for(final BasicEntity inDenStaub : waste){
			entityManager.remove(inDenStaub);
		}
	}

	@Test
	@Transactional
	@Rollback(false)
	public final void createNaturalPerson() {
		final Nativity nativity = newNativity();
				
		final NaturalPerson person = new NaturalPersonImpl("Kylie", "Minogue", nativity);
		final LoginContact mailContact =  contactBuilderFactory.eMailContactBuilder().withAccount("kylie@minogue.com").build();
		person.assign(mailContact);
		final LoginContact phoneContact = contactBuilderFactory.phoneContactBuilder().withCountryCode("61").withAreaCode("2").withSubscriberNumber("123456").build();
		person.assign(phoneContact);
		final LoginContact messengerContact = contactBuilderFactory.instantMessengerContactBuilder().withProvider(InstantMessenger.Skype).withAccount("kinkyKylie").build();
		person.assign(messengerContact);
		
		final Address address = contactBuilderFactory.addressBuilder().withCity("Wegberg").withZipCode("41844").withHouseNumber("4").withStreet("Am Telt").withCoordinates(contactBuilderFactory.coordinatesBuilder().withLongitude(6).withLatitude(54).build()).build();
		person.assign(address);
		person.assign(PersonRole.Catalogs);
		person.assign(PersonRole.States);
		person.assignPassword("kinkyKylie");
		
		entityManager.persist(person);
		waste.add(person);
		entityManager.flush();
		final NaturalPerson result = entityManager.find(NaturalPersonImpl.class, person.id());
		Assert.assertEquals(nativity, result.nativity());
		Assert.assertEquals(person, result);
		Assert.assertEquals(4, result.contacts().size());
		Assert.assertTrue(result.contacts().contains(mailContact));
		Assert.assertTrue(result.contacts().contains(phoneContact));
		Assert.assertTrue(result.contacts().contains(messengerContact));
		Assert.assertTrue(result.contacts().contains(address));
		
		Assert.assertEquals(2, result.roles().size());
		Assert.assertTrue(person.hasRole(PersonRole.Catalogs));
		Assert.assertTrue(person.hasRole(PersonRole.States));
		
	}
	
	private Nativity newNativity() {
		try {
			final Constructor<? extends Nativity> constructor = NativityImpl.class.getDeclaredConstructor(String.class, Date.class);
			constructor.setAccessible(true);
			return constructor.newInstance("Melborne", new GregorianCalendar(1968, 4, 28).getTime());
		} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
		
	}
	
	
	@Test
	@Transactional
	@Rollback(false)
	public final void createLegalPerson() {
		final Date date = new GregorianCalendar(1968, 4, 28).getTime();
		final TradeRegister register = newTradeRegister();
				
	    final String name = "Minogue-Music";
		final String taxId = "taxId";
		final LegalPerson person = new LegalPersonImpl(name, taxId, register, LegalForm.GbR, date);	
		person.assignPassword("kinkyKylie");
		entityManager.persist(person);
		waste.add(person);
		final LegalPerson result = entityManager.find(LegalPersonImpl.class, person.id());
		Assert.assertEquals(person, result);
		Assert.assertEquals(name, result.name());
		Assert.assertEquals(taxId, result.taxId());
		Assert.assertEquals(LegalForm.GbR, result.legalForm());
		Assert.assertEquals(date, result.foundationDate());
		Assert.assertEquals(register, result.tradeRegister());
		Assert.assertEquals(register.zipCode(), result.tradeRegister().zipCode());
		Assert.assertEquals(register.city(), result.tradeRegister().city());
		Assert.assertEquals(register.reference(), result.tradeRegister().reference());
	
	}
	
	private TradeRegister newTradeRegister() {
		try {
			final Constructor<? extends TradeRegister> constructor = TradeRegisterImpl.class.getDeclaredConstructor(String.class, String.class , String.class);
			constructor.setAccessible(true);
			return constructor.newInstance("41844","Wegberg", "12345/67890");
		} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
		
	}
	
	
	
}
