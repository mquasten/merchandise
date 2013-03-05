package de.mq.merchandise.controller;

import javax.validation.Validator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.event.FlowEvent;
import org.springframework.context.ApplicationContext;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.Registration;
import de.mq.merchandise.model.Registration.Kind;

public class RegistrationWizardControllerTest {

	private static final long ID = 19680528L;
	private Customer customer = Mockito.mock(Customer.class);
	private CustomerService customerService = Mockito.mock(CustomerService.class);
    private  RegistrationWizardControllerImpl registrationWizardController;
	private FlowEvent flowEvent;
	private ApplicationContext applicationContext;
	private Registration registration = Mockito.mock(Registration.class);
	private Validator validator;
	
	@Before
	public final void setup() {
		validator=Mockito.mock(Validator.class);
		customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(ID);
		customerService = Mockito.mock(CustomerService.class);
		
		applicationContext = Mockito.mock(ApplicationContext.class);
		registrationWizardController = new RegistrationWizardControllerImpl(customerService, applicationContext, validator);
		flowEvent = Mockito.mock(FlowEvent.class);
		registration = Mockito.mock(Registration.class);
		Mockito.when(registration.kind()).thenReturn(Registration.Kind.User);
		Mockito.when(registration.customer()).thenReturn(customer);
		Mockito.when(applicationContext.getBean(Registration.class)).thenReturn(registration);
	}
	
	@Test
	public final void onFlowProzessNotUserRegistration() {
		
		//registrationWizardController = new RegistrationWizardControllerImpl(Mockito.mock(CustomerService.class), Mockito.mock(ApplicationContext.class));
		Mockito.when(flowEvent.getNewStep()).thenReturn(RegistrationWizardControllerImpl.PERSON);
		Mockito.when(flowEvent.getOldStep()).thenReturn(RegistrationWizardControllerImpl.GENERAL);
		
		Assert.assertEquals(RegistrationWizardControllerImpl.PERSON, registrationWizardController.onFlowProcess(flowEvent));
		
		Mockito.when(flowEvent.getNewStep()).thenReturn(RegistrationWizardControllerImpl.GENERAL);
		Mockito.when(flowEvent.getOldStep()).thenReturn(RegistrationWizardControllerImpl.PERSON);
		Assert.assertEquals(RegistrationWizardControllerImpl.GENERAL, registrationWizardController.onFlowProcess(flowEvent));
	} 
	
	@Test
	public final void onFlowProzessUserRegistrationNewUser() {
		Mockito.when(customerService.customer(ID)).thenReturn(customer);
		
		
		Mockito.when(flowEvent.getNewStep()).thenReturn(RegistrationWizardControllerImpl.OVERVIEW);
		Mockito.when(flowEvent.getOldStep()).thenReturn(RegistrationWizardControllerImpl.PERSON);
		
		Assert.assertEquals(RegistrationWizardControllerImpl.OVERVIEW, registrationWizardController.onFlowProcess(flowEvent));
	}
	
	@Test
	public final void onFlowProzessNotUserRegistrationLastStep() {
		//Mockito.when(customerService.customer(ID)).thenReturn(customer);
		Mockito.when(registration.kind()).thenReturn(Kind.LegalPerson);
		
		Mockito.when(flowEvent.getNewStep()).thenReturn(RegistrationWizardControllerImpl.OVERVIEW);
		Mockito.when(flowEvent.getOldStep()).thenReturn(RegistrationWizardControllerImpl.PERSON);
		
		Assert.assertEquals(RegistrationWizardControllerImpl.OVERVIEW, registrationWizardController.onFlowProcess(flowEvent));
		Mockito.verifyNoMoreInteractions(customerService);
		
	}
	
	@Test
	public final void onFlowProzessUserRegistrationNewUserCustomerNotFound() {
		Mockito.when(flowEvent.getNewStep()).thenReturn(RegistrationWizardControllerImpl.OVERVIEW);
		Mockito.when(flowEvent.getOldStep()).thenReturn(RegistrationWizardControllerImpl.PERSON);
		
		Assert.assertEquals(RegistrationWizardControllerImpl.PERSON, registrationWizardController.onFlowProcess(flowEvent));
	}
	
	
	@Test
	public final void register(){
		
		final Person person = Mockito.mock(Person.class);
		registrationWizardController.register(customer, person);
		Mockito.verify(customerService).register(customer, person);
	}
	

	@Test
	public final void defaultConstructoer() {
		Assert.assertNotNull(new RegistrationWizardControllerImpl());
	}

}
