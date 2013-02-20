package de.mq.merchandise.contact.support;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.support.AddressBuilder;
import de.mq.merchandise.contact.support.AddressBuilderImpl;

public class AddressBuilderTest {

	private static final String HOUSE_NUMBER = "4";
	private static final String STREET = "Am Telt";
	private static final String CITY = "Wegberg";
	private static final String ZIP_CODE = "41844";
	private static final Coordinates COORDINATES = Mockito.mock(Coordinates.class);

	@Test
	public final void country() {
		final AddressBuilder builder = new AddressBuilderImpl();
		Assert.assertEquals(builder, builder.withCountry(Locale.GERMANY));
	}

	@Test
	public final void zipCode() {
		final AddressBuilder builder = new AddressBuilderImpl();
		Assert.assertEquals(builder, builder.withZipCode(ZIP_CODE));
	}

	@Test
	public final void zipCity() {
		final AddressBuilder builder = new AddressBuilderImpl();
		Assert.assertEquals(builder, builder.withCity(CITY));
	}

	@Test
	public final void street() {
		final AddressBuilder builder = new AddressBuilderImpl();
		Assert.assertEquals(builder, builder.withStreet(STREET));
	}

	@Test
	public final void houseNumber() {
		final AddressBuilder builder = new AddressBuilderImpl();
		Assert.assertEquals(builder, builder.withHouseNumber(HOUSE_NUMBER));
	}
	
	@Test
	public final void coordinates() {
		final AddressBuilder builder = new AddressBuilderImpl();
		Assert.assertEquals(builder, builder.withCoordinates(COORDINATES));
	}

	@Test
	public final void build() {
		final Address address = new AddressBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).withStreet(STREET).withHouseNumber(HOUSE_NUMBER).withCoordinates(COORDINATES).build();
		Assert.assertEquals(Locale.GERMANY, address.country());
		Assert.assertEquals(ZIP_CODE, address.zipCode());
		Assert.assertEquals(CITY, address.city());
		Assert.assertEquals(STREET, address.street());
		Assert.assertEquals(HOUSE_NUMBER, address.houseNumber());
	}

	@Test
	public final void buildLocale() {
		final Address address = new AddressBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).withStreet(STREET).withHouseNumber(HOUSE_NUMBER).withCoordinates(COORDINATES).withCountry(Locale.US).build();
		Assert.assertEquals(Locale.US, address.country());
		Assert.assertEquals(ZIP_CODE, address.zipCode());
		Assert.assertEquals(CITY, address.city());
		Assert.assertEquals(STREET, address.street());
		Assert.assertEquals(HOUSE_NUMBER, address.houseNumber());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void missingZipCode() {
		new AddressBuilderImpl().withCity(CITY).withStreet(STREET).withHouseNumber(HOUSE_NUMBER).withCoordinates(COORDINATES).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public final void missingCity() {
		new AddressBuilderImpl().withZipCode(ZIP_CODE).withStreet(STREET).withHouseNumber(HOUSE_NUMBER).withCoordinates(COORDINATES).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public final void missingStreet() {
		new AddressBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).withHouseNumber(HOUSE_NUMBER).withCoordinates(COORDINATES).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public final void missingHousNumber() {
		new AddressBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).withStreet(STREET).withCoordinates(COORDINATES).build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void missingCoordinates() {
		new AddressBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).withStreet(STREET).withHouseNumber(HOUSE_NUMBER).build();
	}
}
