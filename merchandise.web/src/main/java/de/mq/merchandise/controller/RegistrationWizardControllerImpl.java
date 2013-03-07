package de.mq.merchandise.controller;



import java.util.HashSet;
import java.util.Set;

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
	
	@ExceptionTranslations(value={@ExceptionTranslation( resultExpression="#args[0].oldStep",  action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class , bundle="customer_not_found" ) ,
			                      @ExceptionTranslation( resultExpression="#args[0].oldStep" , action = SimpleFacesExceptionTranslatorImpl.class, source = ConstraintViolationException.class  )}, clazz = RegistrationWizardControllerImpl.class)
	public String onFlowProcess(final FlowEvent event) { 
		final Registration registration = applicationContext.getBean(Registration.class);	
		
		if (isGoToOverviewPage(event)) {
			validateBean(registration.getPerson());
		}
		
		if( isGoToOverViewPageForNewUserAndExistingCustomer(event, registration)) {
			registration.assign(customerService.customer(registration.customer().id()));
		}
		
		return event.getNewStep();
    }

	private boolean isGoToOverViewPageForNewUserAndExistingCustomer(final FlowEvent event, final Registration registration) {
		return event.getOldStep().equalsIgnoreCase(PERSON)&&isGoToOverviewPage(event) &&  (registration.kind().equals(Kind.User ));
	}

	private boolean isGoToOverviewPage(final FlowEvent event) {
		return event.getNewStep().equalsIgnoreCase(OVERVIEW);
	}

	

	// :ToDo Service in application 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void validateBean(final Object person) {
		final Set<ConstraintViolation<Object>> errors = validator.validate(person);
		if(errors.isEmpty()){
		   return;	
		}
		throw new ConstraintViolationException(new HashSet(errors));
	} 

  
	
	
	public void register(final Customer customer, final Person person) {
		customerService.register(customer, person);
	} 

	


	

}
