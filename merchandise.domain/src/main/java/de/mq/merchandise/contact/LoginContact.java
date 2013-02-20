package de.mq.merchandise.contact;


/**
 * A Contact can be used as login for the system
 * @author ManfredQuasten
 *
 */
public interface LoginContact extends Contact  {

	/**
	 * The contact is used for login  
	 * @return true if it can be used as login, else false
	 */
	boolean isLogin();
	
	
}
