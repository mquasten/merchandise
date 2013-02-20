package de.mq.merchandise.contact;

/**
 * Create a value object for InstandMessengerContacts
 * @author ManfredQuasten
 *
 */
public interface InstantMessengerContactBuilder  {

	/**
	 * Specifies the provide (Skype for example) of the InstandMessenger that should be created
	 * @param provider the provider  for the InstandMessengerObject 
	 * @return the builder itselves 
	 */
	InstantMessengerContactBuilder withProvider(final InstantMessenger provider);

	/**
	 * Specifies the Login/Account of the InstandMessenger that should be created
	 * @param account the account for the InstandMessengerObject 
	 * @return the builder itselves 
	 */
	InstantMessengerContactBuilder withAccount(final String account);
	
	/**
	 * The contact can be used as login
	 * @return the builder it selves
	 */
	InstantMessengerContactBuilder withLogin();
	
	/**
	 * Create the implementation of the contact that should be created
	 * @return the implementation of  the created contact 
	 */
	LoginContact build();

}