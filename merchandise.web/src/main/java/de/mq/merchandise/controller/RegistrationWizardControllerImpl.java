package de.mq.merchandise.controller;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

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
import de.mq.merchandise.model.RegistrationImpl;


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
    
    @Autowired
    private Validator validator;
	
	RegistrationWizardControllerImpl(final CustomerService customerService, final ApplicationContext applicationContext, final Validator validator){
		this.customerService=customerService;
		this.applicationContext=applicationContext;
		this.validator=validator;
	}
	
	public String onFlowProcess(final FlowEvent event) { 
		final Registration registration = applicationContext.getBean(Registration.class);	
		if ((event.getNewStep().equalsIgnoreCase(OVERVIEW) ) && (! populateValidationExceptions(registration))) {
				return event.getOldStep();
		}
		
		if( event.getOldStep().equalsIgnoreCase(PERSON)&&(event.getNewStep().equalsIgnoreCase(OVERVIEW) ) &&  (registration.kind().equals(Kind.User ))) {
			return customerForUser(event, registration);
		}
		return event.getNewStep();
    }

	private boolean  populateValidationExceptions(final Registration registration) {
		final Set<ConstraintViolation<Object>> errors = validator.validate(registration.getPerson());
		for(ConstraintViolation<?> error : errors ){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(error.getMessage()));
		}
		return errors.isEmpty();
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
