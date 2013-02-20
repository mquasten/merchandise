package de.mq.merchandise.contact;

/**
 * Create a value object for PhoneContacts
 * @author ManfredQuasten
 *
 */
public interface PhoneContactBuilder  {

	/**
	 * Specifies the countryCode of the PhoneObject that should be created
	 * @param countryCode the countryCode for the PhoneObject 
	 * @return the builder itselves 
	 */
	PhoneContactBuilder withCountryCode(final String countryCode);

	/**
	 * Specifies the areaCode of the PhoneObject that should be created
	 * @param areaCode the areaCode for the PhoneObject 
	 * @return the builder itselves 
	 */
	PhoneContactBuilder withAreaCode(final String areaCode);

	/**
	 * Specifies the subscriberNumber of the PhoneObject that should be created
	 * @param subscriberNumber the subscriberNumber for the PhoneObject 
	 * @return the builder itselves 
	 */
	PhoneContactBuilder withSubscriberNumber(final String subscriberNumber);
	
	/**
	 * The contact can be used as login
	 * @return the builder it selves
	 */
	PhoneContactBuilder withLogin();
	
	
	/**
	 * Create the implementation of the contact that should be created
	 * @return the implementation of  the created contact 
	 */
	LoginContact build();

}