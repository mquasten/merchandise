package de.mq.merchandise.customer.support;

import java.util.Optional;

import de.mq.merchandise.customer.Customer;

/**
 * Datenbankzugriffe auf Customer
 * @author Admin
 *
 */
interface CustomerRepository {

	
	static final String CUSTOMER_BY_ID_QUERY = "customerById";
	static final String ID_PARAMETER = "id";
	
	/**
	 * Einen Customer ueber seine id lesen.
	 * Ist die Id nicht vorhanden, gibt es eine RuntimeException, 
	 * wird kein Kunde fuer die id gefunden, gibt es ebenfalls eine RuntimeException
	 * @param id die Id des Customers
	 * @return Customer passend zur id
	 */
	Customer customerById(final Optional<Long> id);

}