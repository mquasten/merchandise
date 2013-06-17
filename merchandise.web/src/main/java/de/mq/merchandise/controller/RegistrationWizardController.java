package de.mq.merchandise.controller;

import javax.validation.ConstraintViolationException;

import org.primefaces.event.FlowEvent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.Registration;
import de.mq.merchandise.model.RegistrationImpl;

public interface RegistrationWizardController {
	
	@MethodInvocation(value={@ExceptionTranslation( resultExpression="#args[0].oldStep",  action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class , bundle="customer_not_found" ), 
			 @ExceptionTranslation(   resultExpression="#args[0].oldStep" , action = SimpleFacesExceptionTranslatorImpl.class, source = ConstraintViolationException.class  )}
			
			, clazz = RegistrationWizardControllerImpl.class, actions={@ActionEvent(params={@Parameter(clazz=FlowEvent.class , originIndex=0), @Parameter(clazz=Registration.class)})})
			String onFlowProcess(final FlowEvent event);
	
	
	  @MethodInvocation(value={
	            @ExceptionTranslation(  action = SimpleFacesExceptionTranslatorImpl.class, source = DataIntegrityViolationException.class  , bundle="register_dupplicate_login_contact" ),
	            @ExceptionTranslation(  action = SimpleFacesExceptionTranslatorImpl.class, source = IllegalArgumentException.class  , bundle="register_person_already_assigned" )
		
		
		},  clazz = RegistrationWizardControllerImpl.class, actions={@ActionEvent(params={@Parameter(clazz=RegistrationImpl.class , el="#arg.customer.customer", elResultType=Customer.class), @Parameter(clazz=RegistrationImpl.class, el="#arg.person.person" ,elResultType=Person.class )})})
		
		String  register();
	
	
	  @MethodInvocation(clazz = RegistrationWizardControllerImpl.class , actions={@ActionEvent()})
	  public void startConversation();

}
