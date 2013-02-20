package de.mq.merchandise.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.primefaces.event.FlowEvent;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.merchandise.controller.RegistrationWizardControllerImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerBuilder;
import de.mq.merchandise.customer.CustomerBuilderFactory;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerAO;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.NaturalPersonAO;
import de.mq.merchandise.model.RegistrationImpl;
import de.mq.merchandise.model.User;
import de.mq.merchandise.model.RegistrationImpl.Kind;
import de.mq.merchandise.model.support.FacesContextFactory;

public class RegistrationWizardControllerTest {

	private static final long ID = 19680528L;
	private static final String NEXT_STEP = "person";

	
	
	@Test
	public final void onFlowProzess() {
		final  RegistrationWizardControllerImpl registrationWizardController = new RegistrationWizardControllerImpl(Mockito.mock(CustomerService.class),Mockito.mock(CustomerBuilderFactory.class), Mockito.mock(FacesContextFactory.class));
		
		final FlowEvent flowEvent = Mockito.mock(FlowEvent.class);
		Mockito.when(flowEvent.getNewStep()).thenReturn(NEXT_STEP);
		
		Assert.assertEquals(NEXT_STEP, registrationWizardController.onFlowProcess(flowEvent));
		
	}
	
	
	@Test
	public final void register(){
		Customer customer = Mockito.mock(Customer.class);
		Person person = Mockito.mock(Person.class);
		final CustomerService customerService = Mockito.mock(CustomerService.class);
		final  RegistrationWizardControllerImpl registrationWizardController = new RegistrationWizardControllerImpl(customerService, Mockito.mock(CustomerBuilderFactory.class), Mockito.mock(FacesContextFactory.class));
		registrationWizardController.register(customer, person);
		Mockito.verify(customerService).register(customer, person);
	}
	
	
	@Test
	public final void initCustomer(){
		
		final CustomerService customerService = Mockito.mock(CustomerService.class);
		final  RegistrationWizardControllerImpl registrationWizardController = new RegistrationWizardControllerImpl(customerService, Mockito.mock(CustomerBuilderFactory.class), Mockito.mock(FacesContextFactory.class));
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(ID);
		final Customer result = Mockito.mock(Customer.class);
		Mockito.when(customerAO.getCustomer()).thenReturn(customer);
		Mockito.when(customer.hasId()).thenReturn(true);
		Mockito.when(customerService.customer(ID)).thenReturn(result);
		final RegistrationImpl registration = new RegistrationImpl(Mockito.mock(LegalPersonAO.class), Mockito.mock(NaturalPersonAO.class), Mockito.mock(User.class), customerAO);
		registration.setKind(Kind.User.name());
		
		
		registrationWizardController.initCustomer(registration);
		
		Mockito.verify(customerAO).setCustomer(result);
		
	}
	
	
	@Test
	public final void initCustomerNotFound(){
	
		final CustomerService customerService = Mockito.mock(CustomerService.class);
		final FacesContextFactory facesContextFactory = Mockito.mock(FacesContextFactory.class);
		final CustomerBuilderFactory customerBuilderFactory = Mockito.mock(CustomerBuilderFactory.class);
		final CustomerBuilder customerBuilder = Mockito.mock(CustomerBuilder.class);
		Mockito.when(customerBuilderFactory.customerBuilder()).thenReturn(customerBuilder);
		Mockito.when(customerBuilder.withId(ID)).thenReturn(customerBuilder);
		final Customer result = Mockito.mock(Customer.class);
		Mockito.when(customerBuilder.build()).thenReturn(result);
		Mockito.when(result.id()).thenReturn(ID);
		
		final  RegistrationWizardControllerImpl registrationWizardController = new RegistrationWizardControllerImpl(customerService,  customerBuilderFactory,  facesContextFactory);
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(ID);
		final FacesContext facesContext = Mockito.mock(FacesContext.class);
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		
		Mockito.when(customerAO.getCustomer()).thenReturn(customer);
		Mockito.when(customer.hasId()).thenReturn(true);
		Mockito.when(customerService.customer(ID)).thenThrow(new InvalidDataAccessApiUsageException("Customer not found"));
		final RegistrationImpl registration = new RegistrationImpl(Mockito.mock(LegalPersonAO.class), Mockito.mock(NaturalPersonAO.class), Mockito.mock(User.class), customerAO);
		registration.setKind(Kind.User.name());
		
		
		registrationWizardController.initCustomer(registration);
		
		Mockito.verify(facesContext).addMessage(Mockito.anyString(), Mockito.any(FacesMessage.class));
		ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerAO).setCustomer(argumentCaptor.capture());
		
		Assert.assertEquals(ID, argumentCaptor.getValue().id());
		
	}
	
	@Test
	public final void initCustomerNotUser(){
		
		final CustomerService customerService = Mockito.mock(CustomerService.class);
		final  RegistrationWizardControllerImpl registrationWizardController = new RegistrationWizardControllerImpl( customerService, Mockito.mock(CustomerBuilderFactory.class),  Mockito.mock(FacesContextFactory.class));
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		final RegistrationImpl registration = new RegistrationImpl(Mockito.mock(LegalPersonAO.class), Mockito.mock(NaturalPersonAO.class), Mockito.mock(User.class), customerAO);
	    registration.setKind(Kind.NaturalPerson.name());
	    registrationWizardController.initCustomer(registration);
	    
	    Mockito.verifyZeroInteractions(customerAO, customerService);
	   
	}
	
	@Test
	public final void initCustomerNewCustomer(){
		final CustomerService customerService = Mockito.mock(CustomerService.class);
		final  RegistrationWizardControllerImpl registrationWizardController = new RegistrationWizardControllerImpl( customerService,  Mockito.mock(CustomerBuilderFactory.class),  Mockito.mock(FacesContextFactory.class));
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		final RegistrationImpl registration = new RegistrationImpl(Mockito.mock(LegalPersonAO.class), Mockito.mock(NaturalPersonAO.class), Mockito.mock(User.class), customerAO);
	    registration.setKind(Kind.User.name());
	    final Customer customer = Mockito.mock(Customer.class);
	    Mockito.when(customerAO.getCustomer()).thenReturn(customer);
	    registrationWizardController.initCustomer(registration);
	    Mockito.verifyZeroInteractions(customerService);
	}
	
	
	

}
