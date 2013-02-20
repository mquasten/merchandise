package de.mq.merchandise.contact.support;

import java.util.Locale;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Coordinates;

public interface AddressBuilder {

	AddressBuilder withCountry(Locale country);

	AddressBuilder withZipCode(String zipCode);

	AddressBuilder withCity(String city);

	AddressBuilder withStreet(String street);

	AddressBuilder withHouseNumber(String houseNumber);

	Address build();

	AddressBuilder withCoordinates(final Coordinates coordinates);

}