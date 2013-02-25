package de.mq.merchandise.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.map.MapModel;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.customer.Person;

public class MapsControllerTest {
	
	final MapsControllerImpl mapsController = new MapsControllerImpl();
	
	/*51.0294554 2.3754688:Rue de Paris, Dunkerque 
	68.4384984 17.4272612:Norwegen, Narvik 
	48.7422 44.5376002:Мамаев курган, Wolgograd 
	30.82247 28.954309:Markaz Al Alamein, El-Alamein 
	52.5155098 13.3847539:Reichstagsufer 7-14, 10117 Berlin */

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void center() {
		final Person person = Mockito.mock(Person.class);
		final Set<Contact> contacts = new HashSet<>();
		contacts.add(createAddressMock("Rue de Paris, Dunkerque", 51.0294554, 2.3754688));
		contacts.add(createAddressMock("Norwegen, Narvik", 68.4384984, 17.4272612));
		contacts.add(createAddressMock("Мамаев курган,   Wolgograd", 48.7422, 44.5376002));
		contacts.add(createAddressMock("Markaz Al Alamein,   El-Alamein", 30.82247, 28.954309));
		contacts.add(createAddressMock("Reichstagsufer 7-14,   10117 Berlin", 52.5155098, 13.3847539));
		Mockito.when(person.contacts()).thenReturn((Set) Collections.unmodifiableSet(contacts));
		
		final String[] results = mapsController.center(person).split(",");
		Assert.assertEquals(2, results.length);
		Assert.assertEquals((68.4384984+30.82247)/2, Double.valueOf(results[0]));
		Assert.assertEquals((2.3754688+44.5376002)/2 , Double.valueOf(results[1]));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void centerWithoutAddresses() {
		final Person person = Mockito.mock(Person.class);
		mapsController.center(person);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void model() {
		final Person person = Mockito.mock(Person.class);
		final Set<Contact> contacts = new HashSet<>();
		final String city = "Magadan (Gulag)";
		final double latitude = 59.56468;
		double longitude = 150.79525;
		contacts.add(createAddressMock(city, latitude, longitude));
		CityAddress cityAddress = Mockito.mock(CityAddress.class);
		Mockito.when(cityAddress.contact()).thenReturn("magdan@kpdsu.ru");
		contacts.add(cityAddress);
		Mockito.when(person.contacts()).thenReturn((Set) Collections.unmodifiableSet(contacts));
		final MapModel result = mapsController.model(person);
		Assert.assertEquals(1, result.getMarkers().size());
		Assert.assertEquals(city, result.getMarkers().get(0).getTitle());
		Assert.assertEquals(latitude, result.getMarkers().get(0).getLatlng().getLat());
		Assert.assertEquals(longitude, result.getMarkers().get(0).getLatlng().getLng());
	}

	private Address createAddressMock(final String text, final double latitude, final double longitude) {
		final Address address = Mockito.mock(Address.class);
		Mockito.when(address.contact()).thenReturn(text);
		Coordinates coordinates = Mockito.mock(Coordinates.class);
		Mockito.when(coordinates.latitude()).thenReturn(latitude);
		Mockito.when(coordinates.longitude()).thenReturn(longitude);
		
		Mockito.when(address.coordinates()).thenReturn(coordinates);
		return address;
	}
}
