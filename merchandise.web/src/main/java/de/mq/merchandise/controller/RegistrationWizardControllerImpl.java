package de.mq.merchandise.controller;



import javax.validation.ConstraintViolationException;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.Registration;
import de.mq.merchandise.model.Registration.Kind;
import de.mq.merchandise.model.support.Conversation;
import de.mq.merchandise.util.ValidationService;


@SuppressWarnings("unused")
public class RegistrationWizardControllerImpl   {
	
	
	
	static final String PERSON = "person";

	static final String OVERVIEW = "overview";
	
	static final String GENERAL = "general";

	private CustomerService customerService;
	
	//private BeanResolver beanResolver;
	
	
    private ValidationService validationService;
    
    private Conversation conversation;
	
   
	RegistrationWizardControllerImpl(final CustomerService customerService,  final ValidationService validationService, final Conversation conversation){
		this.customerService=customerService;
		this.validationService=validationService;
		this.conversation=conversation;
	}
	
	
	String onFlowProcess(final FlowEvent event, final Registration registration) { 
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
	
	
  
	
	String  register(final Customer customer, final Person person) {
	  customerService.register(customer, person);
	  conversation.end();
	  return "login?faces-redirect=true";
	  
	} 

	
	void startConversation() {
		if( ! conversation.isTransient()) {
			return;
		}
		conversation.begin();
	}

	

}
