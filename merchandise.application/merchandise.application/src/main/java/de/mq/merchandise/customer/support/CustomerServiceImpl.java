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
	
	private final boolean activateNewPersons=true;
	
	private final boolean activateNewRole=true;
	
	private final boolean activateNewCustomer=true;
	
	@Autowired
	public CustomerServiceImpl(final CustomerRepository customerRepository) {
		this.customerRepository=customerRepository;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public final void register(final Customer customer, final Person person) {
		if( activateNewPersons){
		  person.state().activate();
		}
		
		if( customer.hasId() ) {
		   grantAndRegister(customerRepository.forId(customer.id()), person, false, CustomerRole.Opportunities, CustomerRole.Bids, CustomerRole.Demands);
		   return;
		}
		final Customer newCustomer = new CustomerImpl(person);
		
		if( activateNewCustomer){
		  newCustomer.state().activate();
		}
		
		grantAndRegister(newCustomer, person, activateNewRole, CustomerRole.values());
		
	}

	private void grantAndRegister(final Customer changedCustomer, final Person person, final boolean isActive, CustomerRole ... customerRoles) {
		changedCustomer.grant(person, customerRoles);
		if( isActive){
			
			changedCustomer.state(person).activate();	
			
		}
		
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
