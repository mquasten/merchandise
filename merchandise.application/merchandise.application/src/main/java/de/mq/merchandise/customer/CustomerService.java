package de.mq.merchandise.customer;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;

/**
 * Services for the root entity customer.
 * A customer can be registered as a new Natural or legalPerson, or it can become a new person as user on an existing customer.
 * @author ManfredQuasten
 *
 */
@Transactional(propagation=Propagation.REQUIRED , readOnly=true)
public interface CustomerService {

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	/**
	 * A customer can be created as a new legal or natural person or
	 * an existing customer can be enhanced with persons as user.
	 * @param customer the customer that should be created new or enhanced with persons/users
	 * @param person the person that will be used to create the customer, or that will be added to an existing customer.
	 */
	void register(Customer customer, Person person);
	
   /**
    * Get a customer identified by its id.
    * If the id of the customer did not exists an InvaliddataAccessApiUsage Exception is thrown.	
    * @param id the id of the customer, that should be searched
    * @return the customer, identified by the given id
    */
   Customer customer(final Long id);

}