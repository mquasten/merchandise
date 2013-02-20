package de.mq.merchandise.customer.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.CustomerRepository;

@Service
@Transactional(propagation=Propagation.REQUIRED , readOnly=true)
class CustomerServiceImpl implements CustomerService {
	
	private final CustomerRepository customerRepository;
	
	@Autowired
	public CustomerServiceImpl(final CustomerRepository customerRepository) {
		this.customerRepository=customerRepository;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final void register(final Customer customer, final Person person) {
		if( customer.hasId() ) {
		   grantAndRegister(customerRepository.forId(customer.id()), person);
		   return;
		}
		grantAndRegister(new CustomerImpl(person), person);
		
	}

	private void grantAndRegister(final Customer changedCustomer, final Person person) {
		changedCustomer.grant(person,  CustomerRole.Opportunities, CustomerRole.Bids, CustomerRole.Demands);
		customerRepository.store(changedCustomer);
	}
	
	
	public final Customer customer(final Long id){
		final Customer customer =  customerRepository.forId(id);
		if ( customer == null){
			throw new InvalidDataAccessApiUsageException("Customer not found");
		}
		return customer;
		
	}

}
