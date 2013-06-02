package de.mq.merchandise.controller;



import javax.validation.ConstraintViolationException;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.Registration;
import de.mq.merchandise.model.Registration.Kind;
import de.mq.merchandise.model.support.Conversation;
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
	private BeanResolver beanResolver;
	
	
    @Autowired
    private ValidationService validationService;
    
    @Autowired
    private Conversation conversation;
	
   
	RegistrationWizardControllerImpl(final CustomerService customerService, final BeanResolver beanResolver, final ValidationService validationService, final Conversation conversation){
		this.customerService=customerService;
		this.beanResolver=beanResolver;
		this.validationService=validationService;
		this.conversation=conversation;
	}
	
	@MethodInvocation(value={@ExceptionTranslation( resultExpression="#args[0].oldStep",  action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class , bundle="customer_not_found" ), 
	 @ExceptionTranslation(   resultExpression="#args[0].oldStep" , action = SimpleFacesExceptionTranslatorImpl.class, source = ConstraintViolationException.class  )}
	
	, clazz = RegistrationWizardControllerImpl.class)
	public String onFlowProcess(final FlowEvent event) { 
		final Registration registration = beanResolver.getBeanOfType(Registration.class);	
		
	
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
	
	
	@MethodInvocation(value={
            @ExceptionTranslation(  action = SimpleFacesExceptionTranslatorImpl.class, source = DataIntegrityViolationException.class  , bundle="register_dupplicate_login_contact" ),
            @ExceptionTranslation(  action = SimpleFacesExceptionTranslatorImpl.class, source = IllegalArgumentException.class  , bundle="register_person_already_assigned" )
	
	
	},  clazz = RegistrationWizardControllerImpl.class)
	
	public String  register(final Customer customer, final Person person) {
	
	  customerService.register(customer, person);
	  conversation.end();
	  return "login?faces-redirect=true";
	  
	} 

	
	public void startConversation() {
		if( ! conversation.isTransient()) {
			return;
		}
		conversation.begin();
	}

	

}
