package de.mq.merchandise.customer.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(readOnly=true)
	public  Customer  customer(final Optional<Long> customerId){
		final  Customer result = customerRepository.customerById(customerId); 
		result.conditionTypes().size();
		return result;
	}
	
	
	
	
}
