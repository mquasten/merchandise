package de.mq.merchandise.contact;

import de.mq.merchandise.BasicEntity;


/**
 * ContactInformation for aa person, a customer and so on.  
 * The interface is for eMail, InstandMessengers,Phonenumber and so on.
 * @author ManfredQuasten
 *
 */
public interface Contact extends BasicEntity {
	
	/**
	 * The contact information given a string.
	 * This can be an EMail, aphone or informations about an instant messenger account
	 * @return the contact information
	 */
	String contact();
	
}
