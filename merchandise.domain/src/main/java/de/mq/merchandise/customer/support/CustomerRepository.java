package de.mq.merchandise.customer.support;

import de.mq.merchandise.customer.Customer;

interface CustomerRepository {
	
	Customer store(final Customer customer);

	Customer forId(final Long id);

}
