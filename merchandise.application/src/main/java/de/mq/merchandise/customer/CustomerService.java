package de.mq.merchandise.customer;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;

public interface CustomerService {
	 @Transactional(readOnly=true)
	 Customer customer(final Optional<Long> customerId);

}