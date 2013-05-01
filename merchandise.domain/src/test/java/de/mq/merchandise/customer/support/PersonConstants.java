package de.mq.merchandise.customer.support;

import java.util.GregorianCalendar;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;

public class PersonConstants {
	
	public static Person person() {
		final Person person = new NaturalPersonImpl("Kylie", "Minogue", new NativityBuilderFactoryImpl().nativityBuilder().withBirthDate(new GregorianCalendar(1968, 4, 28).getTime()).withBirthPlace("Melborne").build() );
	    person.digest().assignDigest("fever");
	    person.state().activate();
	    return person;
	}
	
	public static Customer customer() {
		final Customer customer = new CustomerImpl(person());
		customer.state().activate();
		return customer;
	}

}
