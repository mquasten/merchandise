package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.SecurityContextFactory;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.util.Paging;

public class SubjectControllerTest {
	
	private static final String PATTERN = "%Escort%Service";

	private final CommercialSubjectService commercialSubjectService = Mockito.mock(CommercialSubjectService.class);
	
	private final SecurityContextFactory securityContextFactory = Mockito.mock(SecurityContextFactory.class);
	
	private final SubjectControllerImpl subjectController = new SubjectControllerImpl(commercialSubjectService, securityContextFactory);
	
	private  CommercialSubjectsModelAO commercialSubjectsModel = Mockito.mock(CommercialSubjectsModelAO.class);
	
	private SecurityContext securityContext = Mockito.mock(SecurityContext.class);

	private Authentication authentication = Mockito.mock(Authentication.class);

	private Customer customer = Mockito.mock(Customer.class);
	
	private PagingAO pagingAO = Mockito.mock(PagingAO.class);
	
	private Paging paging = Mockito.mock(Paging.class);
	
	@Before
	public final void setup() {
		Mockito.when(securityContextFactory.securityContext()).thenReturn(securityContext);
	    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	    Mockito.when(authentication.getDetails()).thenReturn(customer);
	    Mockito.when(commercialSubjectsModel.getPattern()).thenReturn(PATTERN); 
	    Mockito.when(pagingAO.getPaging()).thenReturn(paging);
	    Mockito.when(commercialSubjectsModel.getPaging()).thenReturn(pagingAO);
	}
	
	@Test
	public final void subjects() {
		
		final Collection<CommercialSubject> results = new ArrayList<>();
		results.add(Mockito.mock(CommercialSubject.class));
		Mockito.when(commercialSubjectService.subjects(customer, PATTERN+ "%", paging)).thenReturn(results);
		
		subjectController.subjects(commercialSubjectsModel);
		
		Mockito.verify(commercialSubjectsModel).setCommercialSubjects(results);
		
	}

}
