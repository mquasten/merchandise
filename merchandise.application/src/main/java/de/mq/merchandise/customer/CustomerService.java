package de.mq.merchandise.customer;

import java.util.Optional;

import de.mq.merchandise.customer.Customer;

public interface CustomerService {

	 Customer customer(final Optional<Long> customerId);

}