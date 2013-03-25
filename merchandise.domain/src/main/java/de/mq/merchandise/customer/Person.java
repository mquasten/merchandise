package de.mq.merchandise.customer;

import java.util.Locale;
import java.util.Set;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.LoginContact;

/**
 * A Person can be a natural or a legalPerson.
 * Persons can have contacts and addresses
 * @author ManfredQuasten
 *
 */
public interface Person extends BasicEntity {
	 
	/**
	 * The name of a person.
	 * In case of a legalPerson it is the lastname
	 * @return
	 */
	String name();
	
	/**
	 * Add a loginContact information to a person.
	 * @param loginContact the loginContact that should be added to the person
	 */
	void assign(final LoginContact loginContact);
	
	/**
	 * The contacts that are assigned to a person 
	 * @return the contacts of a person
	 */
	Set<? extends Contact> contacts();
	
	/**
	 * Remove the LoginContact from the contacts of the person.
	 * The method is idempotent. 
	 * @param loginContact the loginContact that should be removed from the contacts of the person
	 */
	void remove(final LoginContact loginContact);
	
	/**
	 * Add an addressContact information to a person.
	 * @param cityAddress the addressContact that should be added to the person
	 */
	void assign(final CityAddress cityAddress); 
	
	/**
	 * Remove the addressContact from the contacts of the person.
	 * The method is idempotent. 
	 * @param addressContact the addressContact that should be removed from the contacts of the person
	 */
	void remove(final CityAddress cityAddress); 
	
	/**
	 * The state of a person.
	 * It can be activated or deactived.
	 * @return the state of the person;
	 */
	State state();
	
	/**
	 * grant a role to a person
	 * @param personRole the role that should be granted to the person
	 */
	void assign(final PersonRole personRole);
	
	/**
	 * A role that should be revoked from a person
	 * The method is idempotent.
	 * @param personRole the role that should be revokes
	 */
	void remove(final PersonRole personRole);
	
	/**
	 * Get the roles that are granted to the person
	 * @return the roles which are granted to the person
	 */
	Set<PersonRole> roles();
	
	/**
	 * Did the person has the role give as parameter
	 * @param role the role for which the permission will be checked
	 * @return true if the role is granted, else false
	 */
	boolean hasRole(final PersonRole role);

	/**
	 * The Customers to that the person is assigned.
	 * The person can work as user with this customers.
	 * @return the customer  to that the person is assigned 
	 */
	Set<Customer> customers();
	
	/**
	 * The country and the default language that will be used.
	 * The country is the address of service for the person
	 * @return the locale (country and ldefault language)
	 */
	Locale locale(); 
	
	void assignPassword(final String password);


}