package de.mq.merchandise.contact;



/**
 * Create a value object for EMailContacts
 * @author ManfredQuasten
 *
 */
public interface EMailContactBuilder  {

	/**
	 * Specifies the eMailAddress of the EMailObject that should be created
	 * @param mailAddress the emailAddress for the MailObject 
	 * @return the builder itselves 
	 */
	EMailContactBuilder withAccount(String mailAddress);
	
	/**
	 * The contact can be used as login
	 * @return the builder it selves
	 */
	EMailContactBuilder withLogin();
	
	/**
	 * Create the implementation of the contact that should be created
	 * @return the implementation of  the created contact 
	 */
	LoginContact build();
	
	

}