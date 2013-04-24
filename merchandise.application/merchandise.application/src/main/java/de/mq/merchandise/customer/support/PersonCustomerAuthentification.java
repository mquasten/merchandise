package de.mq.merchandise.customer.support;

import java.util.Collection;

import org.springframework.security.core.Authentication;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;

/**
 * Specific authentification based on a person that can be user from one or more customers.
 * The current customer can be switched, without new log in.
 * UsersRelation to a customer are stored after login and will not be changed during the login
 * @author MQuasten
 *
 */
public interface PersonCustomerAuthentification extends Authentication {

	/**
	 * The current customer that will be used by the person.
	 * @param customer the customer that is used as the current customer
	 */
	@Override
	Customer getDetails();

	/**
	 * The person that is looged in and that is allredy Authentificated.
	 * @param customer the customer that is used as the current customer
	 */
	@Override
	Person getPrincipal();
	
	/**
	 * The customer that will be used. A user can be related to one or more customers.
	 * @param customer the customer to that will be schwitched.
	 */
	void setDetails(final Customer customer);
	
	/**
	 * All customers for that the person, can work as an user. To this list, the customer can switch without a new Login.
	 * @return a collection of customers for that the person can be an user
	 */
	Collection<Customer> getCustomers();

	
}