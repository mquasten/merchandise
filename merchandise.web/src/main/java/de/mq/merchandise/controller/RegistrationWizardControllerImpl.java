package de.mq.merchandise.controller;



import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.Registration;
import de.mq.merchandise.model.Registration.Kind;


public class RegistrationWizardControllerImpl   {
	
	
	
	static final String PERSON = "person";

	static final String OVERVIEW = "overview";
	
	static final String GENERAL = "general";

	protected  RegistrationWizardControllerImpl() {
	}
	
	@Autowired
	private CustomerService customerService;
	
    @Autowired
	private ApplicationContext applicationContext;
	
	RegistrationWizardControllerImpl(final CustomerService customerService, final ApplicationContext applicationContext){
		this.customerService=customerService;
		this.applicationContext=applicationContext;
	}
	
	public String onFlowProcess(final FlowEvent event) { 
		final Registration registration = applicationContext.getBean(Registration.class);	
		if( event.getOldStep().equalsIgnoreCase(PERSON)&&(event.getNewStep().equalsIgnoreCase(OVERVIEW) ) &&  (registration.kind().equals(Kind.User ))) {
			return customerForUser(event, registration);
		}
		return event.getNewStep();
    }

	String customerForUser(final FlowEvent event, final Registration registration) {
		
		final Customer customer = initCustomer(registration);
		if( customer == null) {
			return event.getOldStep();
		}
		registration.assign(customer);
		return event.getNewStep();
	}  
	
	
	public void register(final Customer customer, final Person person) {
		customerService.register(customer, person);
	} 

	
	@ExceptionTranslations(value={@ExceptionTranslation( action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class , bundle="customer_not_found" )}, clazz = RegistrationWizardControllerImpl.class)
	Customer  initCustomer(final Registration registration){
		return customerService.customer( registration.customer().id());
	}

	

}
