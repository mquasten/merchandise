package de.mq.merchandise.contact.support;

import java.util.Locale;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.util.EntityUtil;

public class AddressTestConstants {
	public final static Locale  COUNTRY = Locale.GERMANY;
	public final static String CITY = "Wegberg";
	public final static String ZIP_CODE = "41844";
	public final static String STREET = "Am Telt";
	public final static String HOUSE_NUMBER = "4";
	public final static String ADDRESS_ID = "19680528";
	public final static String POSTBOX = "12345";
	
	public final static Class<? extends Address> ADDRESS_CLASS = EntityUtil.create(AddressImpl.class).getClass();
	

}
