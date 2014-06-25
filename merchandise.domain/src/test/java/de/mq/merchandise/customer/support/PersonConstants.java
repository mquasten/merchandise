package de.mq.merchandise.customer.support;

import java.util.GregorianCalendar;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.LegalPerson;
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
	
	
	public static LegalPerson legalPerson() {
		final LegalPerson legalPerson = new LegalPersonImpl("PetStore", "0815",new TradeRegisterImpl("12345","Melborne","4711"), LegalForm.eK, new GregorianCalendar(1968, 4, 28).getTime());
		legalPerson.digest().assignDigest("lucky");
		legalPerson.state().activate();
		return legalPerson;
		
	}

}
