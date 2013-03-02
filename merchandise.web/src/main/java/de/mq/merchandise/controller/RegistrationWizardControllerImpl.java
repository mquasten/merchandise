package de.mq.merchandise.controller;



import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.RegistrationImpl;
import de.mq.merchandise.model.RegistrationImpl.Kind;


public class RegistrationWizardControllerImpl   {
	
	
	
	protected  RegistrationWizardControllerImpl() {
	}
	
	@Autowired
	private CustomerService customerService;
	
    @Autowired
	private RegistrationImpl registration;
	
	RegistrationWizardControllerImpl(final CustomerService customerService, final RegistrationImpl registration){
		this.customerService=customerService;
		this.registration=registration;
	}
	
	public String onFlowProcess(final FlowEvent event) { 
		
		if( event.getOldStep().equalsIgnoreCase("person")&&(event.getNewStep().equalsIgnoreCase("overview") ) &&  (registration.getKind().equals(Kind.User.name() ))) {
			return customerForUser(event, registration);
		}
		return event.getNewStep();
    }

	String customerForUser(final FlowEvent event, final RegistrationImpl registration) {
		
		final Customer customer = initCustomer(registration);
		if( customer == null) {
			return event.getOldStep();
		}
		registration.getCustomer().setCustomer(customer);
		return event.getNewStep();
	}  
	
	
	public void register(final Customer customer, final Person person) {
		customerService.register(customer, person);
	} 

	
	@ExceptionTranslations(value={@ExceptionTranslation( action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class , bundle="customer_not_found" )}, clazz = RegistrationWizardControllerImpl.class)
	public Customer  initCustomer(final RegistrationImpl registration){
		return customerService.customer( registration.getCustomer().getCustomer().id());
	}

	

}
