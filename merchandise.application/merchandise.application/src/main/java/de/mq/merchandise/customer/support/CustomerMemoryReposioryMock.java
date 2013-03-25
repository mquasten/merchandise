package de.mq.merchandise.customer.support;

import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.util.EntityUtil;

@Repository
@Profile("mock")
public class CustomerMemoryReposioryMock implements CustomerRepository{
	
	public static final String DEFAULT_LOGIN = "kylie@minogue.net";
	public static final long DEFAULT_CUSTOMER_ID = 19680528L;
	private final Map<Long,Customer> index = new HashMap<>();
	
	public CustomerMemoryReposioryMock() {
		final Person person = new LegalPersonImpl("Minogue-Music","12345",new TradeRegisterBuilderImpl().withCity("London").withZipCode("12345").withReference("0815").build(),LegalForm.GmbHCoKG ,new GregorianCalendar(1968,04, 28).getTime());
		person.assign(new ContactBuilderFactoryImpl().eMailContactBuilder().withAccount(DEFAULT_LOGIN).withLogin().build());
		final Customer customer = new CustomerImpl(person);
		EntityUtil.setId(customer, DEFAULT_CUSTOMER_ID);
		customer.grant(person, CustomerRole.values());
		customer.state().activate();
		person.state().activate();
		customer.state(person).activate();
		store(customer);
	}

	@Override
	public Customer store(final Customer customer) {
		
		if (! customer.hasId()){
			EntityUtil.setId(customer,  Math.round(1e20 *Math.random()));
				
				
		}
		
		index.put(customer.id(), customer);
		
		
		
		return customer;
	}

	

	@Override
	public Customer forId(final Long id) {
		if( index.get(id) == null ){
			return null;
		}
		
		
		return EntityUtil.copy(index.get(id));
	}

	@Override
	public final  Collection<Entry<Customer,Person>> forLogin(String login) {
		final Map<Customer,Person> results = new HashMap<>();
		for(final Customer customer : index.values()){
			checkPersonsContacts(login, results, customer);
		}
		
		DataAccessUtils.requiredSingleResult(new HashSet<>(results.values()));;
		
		for(final Entry<Customer,Person> entry : Collections.unmodifiableSet(results.entrySet())){
			if( ! entry.getKey().state().isActive()){
				results.remove(entry.getKey());
			}
			if( ! entry.getKey().state(entry.getValue()).isActive()){
				results.remove(entry.getKey());
			}
		}
		
		if( results.size() < 1){
			throw new EmptyResultDataAccessException(1);
		}
		return  Collections.unmodifiableSet(results.entrySet());
	}

	

	private void checkPersonsContacts(String login, final  Map<Customer,Person> results, final Customer customer) {
		for(final Person person :customer.persons().keySet()) {
			
			
			if( ! person.state().isActive() ) {
					continue;
			}
			
			if( ! checkPersonsContacts(login, person) ) {
				continue;
			}
			
			results.put(customer, person);
				
				
		}
	}

	private boolean   checkPersonsContacts(final String login, final Person person) {
		for(final Contact contact :  person.contacts() ) {
			
			if (!(contact instanceof LoginContact)) {
				continue;
			}
			
			if( ! ((LoginContact) contact).isLogin()) {
		    	continue;
		    }
		    
		    if( ! contact.contact().equalsIgnoreCase(login)) {
		    	continue;
		    }
		    
		    return true; 
		   
		}
		
		return false; 
	}

}
