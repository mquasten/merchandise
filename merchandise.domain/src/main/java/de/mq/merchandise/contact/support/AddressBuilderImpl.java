package de.mq.merchandise.contact.support;

import java.util.Locale;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.util.EntityUtil;

class AddressBuilderImpl implements AddressBuilder {

	private Locale country = Locale.GERMANY;

	private String zipCode;

	private String city;

	private String street;

	private String houseNumber;
	
	private Coordinates coordinates;

	
	@Override
	public final AddressBuilder withCountry(final Locale country) {
		this.country = country;
		return this;
	}

	
	@Override
	public final AddressBuilder withZipCode(final String zipCode) {
		this.zipCode = zipCode;
		return this;
	}

	
	@Override
	public final AddressBuilder withCity(final String city) {
		this.city = city;
		return this;
	}

	
	@Override
	public final AddressBuilder withStreet(final String street) {
		this.street = street;
		return this;
	}

	
	@Override
	public final AddressBuilder withHouseNumber(final String houseNumber) {
		this.houseNumber = houseNumber;
		return this;
	}
	
	@Override
	public final AddressBuilder withCoordinates(final Coordinates coordinates) {
		this.coordinates=coordinates;
		return this;
	}

	
	@Override
	public final Address build() {
		EntityUtil.mandatoryGuard(zipCode, "zipCode");
		EntityUtil.mandatoryGuard(city, "city");
		EntityUtil.mandatoryGuard(street, "street");
		EntityUtil.mandatoryGuard(houseNumber, "houseNumber");
		EntityUtil.notNullGuard(coordinates, "coordinates");
		return new AddressImpl(country, zipCode, city, street, houseNumber, coordinates);
	}

}
