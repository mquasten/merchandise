package de.mq.merchandise.contact;

import de.mq.merchandise.contact.support.AddressBuilder;

public interface ContactBuilderFactory {

	PhoneContactBuilder phoneContactBuilder();

	InstantMessengerContactBuilder instantMessengerContactBuilder();

	EMailContactBuilder eMailContactBuilder();

	PostBoxAddressBuilder postBoxAddressBuilder();

	AddressBuilder addressBuilder();

	CoordinatesBuilder coordinatesBuilder();

}