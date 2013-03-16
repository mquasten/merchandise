package de.mq.merchandise.customer.support;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.util.EntityUtil;

@Repository
@Profile("mock")
public class CustomerMemoryReposioryMock implements CustomerRepository{
	
	public static final long DEFAULT_CUSTOMER_ID = 19680528L;
	private final Map<Long,Customer> index = new HashMap<>();
	
	public CustomerMemoryReposioryMock() {
		final Person person = new LegalPersonImpl("Minogue-Music","12345",new TradeRegisterBuilderImpl().withCity("London").withZipCode("12345").withReference("0815").build(),LegalForm.GmbHCoKG ,new GregorianCalendar(1968,04, 28).getTime());
		final Customer customer = new CustomerImpl(person);
		EntityUtil.setId(customer, DEFAULT_CUSTOMER_ID);
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

}
