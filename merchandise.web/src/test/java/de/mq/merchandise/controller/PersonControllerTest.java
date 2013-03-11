package de.mq.merchandise.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.GeocodingService;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.PostBox;
import de.mq.merchandise.contact.support.AddressTestConstants;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.ContactTestConstants;

public class PersonControllerTest {
	
	private static final String MAIL_ADDRESS = "kylie.minogue@fever.uk";

	
	private GeocodingService geocodingService;
	private Coordinates coordinates;
	
	@Before
	public final void setup() {
		coordinates =  Mockito.mock(Coordinates.class);
		geocodingService=Mockito.mock(GeocodingService.class);
	    Mockito.when(coordinates.longitude()).thenReturn(151d);
	    Mockito.when(coordinates.latitude()).thenReturn(60d);
		Mockito.when(geocodingService.coordinates(Mockito.any(CityAddress.class))).thenReturn(coordinates);
	}

	@Test
	public final void addAddress() {
		final PersonControllerImpl registrationWizardController = new PersonControllerImpl(geocodingService);
		final ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
		final Person person = Mockito.mock(Person.class);
		
		
		final Address  address = new ContactBuilderFactoryImpl().addressBuilder().withCountry(AddressTestConstants.COUNTRY).withCity(AddressTestConstants.CITY).withZipCode(AddressTestConstants.ZIP_CODE).withStreet( AddressTestConstants.STREET).withHouseNumber(AddressTestConstants.HOUSE_NUMBER).withCoordinates(Mockito.mock(Coordinates.class)).build();
		
		
	    registrationWizardController.addAddress(person, address);
	    
	    Mockito.verify(person).assign(addressCaptor.capture());
	    Assert.assertNotSame(address, addressCaptor.getValue());
	    
	    Assert.assertEquals(AddressTestConstants.CITY, addressCaptor.getValue().city());
	    Assert.assertEquals(AddressTestConstants.COUNTRY.toString(), addressCaptor.getValue().country().toString());
	    Assert.assertEquals(AddressTestConstants.ZIP_CODE, addressCaptor.getValue().zipCode());
	    Assert.assertEquals(AddressTestConstants.STREET, addressCaptor.getValue().street());
	    Assert.assertEquals(AddressTestConstants.HOUSE_NUMBER, addressCaptor.getValue().houseNumber());
	    
	   Assert.assertEquals(coordinates, addressCaptor.getValue().coordinates());
	   
	   
	}
	
	@Test
	public final void addAddressPostBox() {
		
		final PersonControllerImpl registrationWizardController = new PersonControllerImpl(geocodingService);
		
		final Person person = Mockito.mock(Person.class);
		final PostBox  address = new ContactBuilderFactoryImpl().postBoxAddressBuilder().withCountry(AddressTestConstants.COUNTRY).withCity(AddressTestConstants.CITY).withZipCode(AddressTestConstants.ZIP_CODE).withBox("123").build();
		registrationWizardController.addAddress(person, address);
		
		Mockito.verifyZeroInteractions(geocodingService);
		Mockito.verify(person).assign(Mockito.any(PostBox.class));
	
	}
	
	@Test
	public final void removeAddress() {
		final  PersonControllerImpl registrationWizardController = new PersonControllerImpl(geocodingService);
		final Person person = Mockito.mock(Person.class);
		final CityAddress address = Mockito.mock(CityAddress.class);
		
		registrationWizardController.removeAddress(person, address);
		
		Mockito.verify(person).remove(address);
		
		
	}
	
	
	@Test
	public final void addContact() {
		final PersonControllerImpl personController = new PersonControllerImpl(geocodingService);
		
		final Person person = Mockito.mock(Person.class);
		final LoginContact loginContact = new ContactBuilderFactoryImpl().eMailContactBuilder().withAccount( MAIL_ADDRESS).build();
		
		
		personController.addContact(person, loginContact);
		final ArgumentCaptor<LoginContact> argumentCaptor = ArgumentCaptor.forClass(LoginContact.class);
		Mockito.verify(person).assign(argumentCaptor.capture());
		Assert.assertEquals(MAIL_ADDRESS, argumentCaptor.getValue().contact());
		Assert.assertNotSame(loginContact, argumentCaptor.getValue());
		Assert.assertNull(loginContact.contact());
	}
	
	@Test
	public final void removeContact() {
		final PersonControllerImpl personController = new PersonControllerImpl(geocodingService);
		final LoginContact loginContact = Mockito.mock(LoginContact.class);
		final Person person = Mockito.mock(Person.class);
		personController.removeContact(person, loginContact);
		
		Mockito.verify(person).remove(loginContact);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void contacts() {
		final PersonControllerImpl personController = new PersonControllerImpl(geocodingService);
		final Person person = Mockito.mock(Person.class);
		@SuppressWarnings("rawtypes")
		final Set contacts= new HashSet<>();
		final Contact contact = Mockito.mock(Contact.class);
		contacts.add(contact);
		Mockito.when(contact.contact()).thenReturn(ContactTestConstants.EMAIL);
		Mockito.when(person.contacts()).thenReturn(Collections.unmodifiableSet(contacts));
		final  List<String> results = personController.contacts(person);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(ContactTestConstants.EMAIL, results.iterator().next());
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new PersonControllerImpl());
	}

}
