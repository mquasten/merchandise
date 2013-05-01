package de.mq.merchandise.customer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.opportunity.support.CommercialSubject;

/**
 * A customer can buy things at the store. A customer is represented by a legal
 * or natural person
 * 
 * @author ManfredQuasten
 * 
 */
public interface Customer extends BasicEntity {

	/**
	 * A customer must be a natural or a legal person
	 * 
	 * @return the person that represents the customer
	 */
	Person person();

	/**
	 * The state of the customer. The state can be active or not
	 * 
	 * @return the state of the customer
	 */
	State state();

	/**
	 * Assign a person to a customer and the roles that the person has for the
	 * customer. The method is idempotent
	 * 
	 * @param person
	 *            the person that is assigned to the customer
	 * @param roles
	 *            the roles that will be assigned to the customer
	 */
	void grant(final Person person, final CustomerRole... roles);

	/**
	 * Revoke the roles from person for the customer The method is idempotent
	 * 
	 * @param person
	 *            the person for which the roles will be revoked
	 * @param roles
	 *            the roles that should be revoked for the person from customer
	 */
	void revoke(final Person person, final CustomerRole... roles);

	/**
	 * Checks, if the person has the role for the customer
	 * 
	 * @param person
	 *            the person for witch the check should be done
	 * @param role
	 *            the role that should be checked
	 * @return true if the role is granted for the person otherwise false
	 */
	boolean hasRole(final Person person, final CustomerRole role);

	/**
	 * Returns the active persons that are assigned to the customer  
	 * 
	 * @return a list of related persons with  state active 
	 */
	List<Person> activePersons();
	
	/**
	 * Returns a list of persons, that are inActive for the customer at the moment
	 * @return a list of persons that are not activated for the customer
	 */
	List<Person> inActivePersons();
	
	/**
	 * Returns a map with person as key and the state of the person for the customer as values
	 * @return map of persons and their states for the customer
	 */
	Map<Person,State> persons();

	/**
	 * REturns the roles for the given person
	 * 
	 * @param person
	 *            the person for which the roles should be selected
	 * @return the roles for the person given as parameter
	 */
	List<CustomerRole> roles(final Person person);
	
	/**
	 * The state of an userRelation.
	 * A person can be activated or deactived for a customer
	 * @param person the person for which the state of the userRelation should be returned.
	 * @return the state of the userRelation for the given person
	 */
	State state(final Person person);
	
	/**
	 * Check if a person is already assigned with a customer with an active or inactive user role
	 * @param person the person for that the check should be done
	 * @return true if the person have a role, false if not
	 */
    boolean hasUser(final Person person);

    /**
     * The commercial subjects related to a customer, products services etc
     * @return the products and services related to a customer
     */
	Collection<CommercialSubject> commercialSubjects();

	/**
	 * Adds a commercialSubject to a customer. An existing one that exists should be removed, the new one will be added
	 * @param commercialSubject the commercial subject that will be added.
	 */
	void assign(final CommercialSubject commercialSubject);

	/**
	 * Removes a commercialSubject from a customer
	 * @param commercialSubject the subject that should be removed
	 */
	void remove(final CommercialSubject commercialSubject);

}