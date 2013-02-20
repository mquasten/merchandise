package de.mq.merchandise.contact.support;

import de.mq.merchandise.contact.ContactBuilderFactory;
import de.mq.merchandise.contact.CoordinatesBuilder;
import de.mq.merchandise.contact.EMailContactBuilder;
import de.mq.merchandise.contact.InstantMessengerContactBuilder;
import de.mq.merchandise.contact.PhoneContactBuilder;
import de.mq.merchandise.contact.PostBoxAddressBuilder;
import de.mq.merchandise.util.EntityUtil;

public class ContactBuilderFactoryImpl implements ContactBuilderFactory {
	
	
	@Override
	public final PhoneContactBuilder phoneContactBuilder() {
		return EntityUtil.create(PhoneContactBuilderImpl.class);
	}
	
	
	@Override
	public final InstantMessengerContactBuilder instantMessengerContactBuilder() {
		return EntityUtil.create(InstantMessengerContactBuilderImpl.class);
	}
	
	
	@Override
	public final EMailContactBuilder eMailContactBuilder() {
		return EntityUtil.create(EMailContactBuilderImpl.class);
	}
	
	
	@Override
	public final PostBoxAddressBuilder postBoxAddressBuilder() {
		return EntityUtil.create(PostBoxAddressBuilderImpl.class);
	}
	
	
	@Override
	public final AddressBuilder addressBuilder() {
		return EntityUtil.create(AddressBuilderImpl.class);
	}
	
	@Override
	public final CoordinatesBuilder coordinatesBuilder() {
		return EntityUtil.create(CoordinatesBuilderImpl.class);
	}

}
