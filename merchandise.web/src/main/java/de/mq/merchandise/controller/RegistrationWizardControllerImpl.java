package de.mq.merchandise.controller;



import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.validation.ConstraintViolationException;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.ContactModelImpl;
import de.mq.merchandise.model.Registration;
import de.mq.merchandise.model.Registration.Kind;
import de.mq.merchandise.util.ValidationService;


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
    private ValidationService validationService;
	
	RegistrationWizardControllerImpl(final CustomerService customerService, final ApplicationContext applicationContext, final ValidationService validationService){
		this.customerService=customerService;
		this.applicationContext=applicationContext;
		this.validationService=validationService;
	}
	
	@ExceptionTranslations(value={@ExceptionTranslation( resultExpression="#args[0].oldStep",  action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class , bundle="customer_not_found" ) ,
			                      @ExceptionTranslation( resultExpression="#args[0].oldStep" , action = SimpleFacesExceptionTranslatorImpl.class, source = ConstraintViolationException.class  )}, clazz = RegistrationWizardControllerImpl.class)
	public String onFlowProcess(final FlowEvent event) { 
		final Registration registration = applicationContext.getBean(Registration.class);	
	
		if (isGoToOverviewPage(event)) {
			validationService.validate(registration.getPerson());
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
	
	public void register(final Customer customer, final Person person) {
		System.out.println("****************************************");
		
		Iterator<String> it = FacesContext.getCurrentInstance().getClientIdsWithMessages();
		while( it.hasNext()){
			System.out.println(it.next());
		}
	   
		
		customerService.register(customer, person);
	} 

	


	

}
