package de.mq.merchandise.controller;

import javax.faces.application.FacesMessage;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerBuilderFactory;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.RegistrationImpl;
import de.mq.merchandise.model.RegistrationImpl.Kind;
import de.mq.merchandise.model.support.FacesContextFactory;

@Component("registrationWizardController")
public class RegistrationWizardControllerImpl  {
	
	
	
	private final CustomerService customerService;
	
	private final FacesContextFactory facesContextFactory;
	
	private final CustomerBuilderFactory customerBuilderFactory;
	
	@Autowired
	public  RegistrationWizardControllerImpl(final CustomerService customerService, final CustomerBuilderFactory customerBuilderFactory, final FacesContextFactory facesContextFactory){
		this.customerService=customerService;
		this.facesContextFactory=facesContextFactory;
		this.customerBuilderFactory=customerBuilderFactory;
	}
	
	public final String onFlowProcess(final FlowEvent event) {  
		
			return event.getNewStep();
    }  
	
	
	public final void register(final Customer customer, final Person person) {
		customerService.register(customer, person);
	}

	
	
	public final void initCustomer(final RegistrationImpl registration){
		
		
		if(! registration.getKind().equals(Kind.User.name())) {
			return;
		}
		
		if ( ! registration.getCustomer().getCustomer().hasId()) {
			return;
		}
		
		try {
		
		
		
		     /* like a virgin, not a blessed one ... */
		
	         registration.getCustomer().setCustomer(customerService.customer( registration.getCustomer().getCustomer().id()));
		} catch (final InvalidDataAccessApiUsageException ex) {
			
			facesContextFactory.facesContext().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "Customer not Found",""+ registration.getCustomer().getCustomer().id()));
		
			registration.getCustomer().setCustomer(customerBuilderFactory.customerBuilder().withId(registration.getCustomer().getCustomer().id()).build());
		}
		
	}

	

}
