package de.mq.merchandise.customer.support;

import java.util.Collection;
import java.util.Map.Entry;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;

interface CustomerRepository {
	
	static final String PERSON_FOR_LOGIN = "personForLogin";
	static final String CUSTOMER_FOR_PERSON = "customerForPerson";
	
	Customer store(final Customer customer);

	Customer forId(final Long id);
	
	
	Collection<Entry<Customer, Person>> forLogin(final String login);

}
