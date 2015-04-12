package de.mq.merchandise.customer.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;

@Service
class CustomerServiceImpl implements CustomerService  {

	private final CustomerRepository customerRepository;
	
	@Autowired
	CustomerServiceImpl(final CustomerRepository customerRepository) {
		this.customerRepository=customerRepository;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.customer.support.CustomerService#customer(java.util.Optional)
	 */
	@Override
	public final Customer  customer(final Optional<Long> customerId){
		return customerRepository.customerById(customerId); 
	}
}
