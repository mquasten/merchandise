package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectAO;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.util.Paging;

public class SubjectControllerTest {
	
	private static final String PATTERN = "%Escort%Service";

	private final CommercialSubjectService commercialSubjectService = Mockito.mock(CommercialSubjectService.class);
	
	private final SubjectControllerImpl subjectController = new SubjectControllerImpl(commercialSubjectService);
	
	private  CommercialSubjectsModelAO commercialSubjectsModel = Mockito.mock(CommercialSubjectsModelAO.class);

	private Customer customer = Mockito.mock(Customer.class);
	
	private PagingAO pagingAO = Mockito.mock(PagingAO.class);
	
	private Paging paging = Mockito.mock(Paging.class);
	
	final Collection<CommercialSubject> results = new ArrayList<>();
	final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	
	@Before
	public final void setup() {
		
	  
	    Mockito.when(commercialSubjectsModel.getPattern()).thenReturn(PATTERN); 
	    Mockito.when(pagingAO.getPaging()).thenReturn(paging);
	    Mockito.when(commercialSubjectsModel.getPaging()).thenReturn(pagingAO);
		results.add(commercialSubject);
		Mockito.when(commercialSubjectService.subjects(customer, PATTERN+ "%", paging)).thenReturn(results);
	}
	
	@Test
	public final void subjects() {
		subjectController.subjects(commercialSubjectsModel, customer);
		Mockito.verify(commercialSubjectsModel).setCommercialSubjects(results);
	}
	
	@Test
	public final void updateSelectionSelectionInResult() {
		final CommercialSubjectAO selectedAO = Mockito.mock(CommercialSubjectAO.class);
		Mockito.when(selectedAO.getCommercialSubject()).thenReturn(commercialSubject);
		Mockito.when(commercialSubjectsModel.getSelected()).thenReturn(selectedAO);
		final List<CommercialSubjectAO> results = new ArrayList<>();
		results.add(selectedAO);
		Mockito.when(commercialSubjectsModel.getCommercialSubjects()).thenReturn(results);
		subjectController.updateSelection(commercialSubjectsModel);
		
		
		Mockito.verify(commercialSubjectsModel, Mockito.never()).setSelected(null);
		Mockito.verify(commercialSubjectsModel).getCommercialSubjects();
		Mockito.verify(commercialSubjectsModel, Mockito.times(2)).getSelected();
		Mockito.verify(selectedAO, Mockito.times(2)).getCommercialSubject();
		
	}
	
	@Test
	public final void updateSelectionSelectionNotInResult() {
		final CommercialSubjectAO selectedAO = Mockito.mock(CommercialSubjectAO.class);
		Mockito.when(selectedAO.getCommercialSubject()).thenReturn(commercialSubject);
		Mockito.when(commercialSubjectsModel.getSelected()).thenReturn(selectedAO);
		final List<CommercialSubjectAO> results = new ArrayList<>();
		final CommercialSubjectAO result = Mockito.mock(CommercialSubjectAO.class);
		CommercialSubject domainResult = Mockito.mock(CommercialSubject.class);;
		Mockito.when(result.getCommercialSubject()).thenReturn(domainResult);
		results.add(result);
		Mockito.when(commercialSubjectsModel.getCommercialSubjects()).thenReturn(results);
		subjectController.updateSelection(commercialSubjectsModel);
		
		
		Mockito.verify(commercialSubjectsModel).setSelected(null);
		Mockito.verify(commercialSubjectsModel).getCommercialSubjects();
		Mockito.verify(commercialSubjectsModel, Mockito.times(2)).getSelected();
		Mockito.verify(selectedAO).getCommercialSubject();
		Mockito.verify(result).getCommercialSubject();
		
	}
	
	@Test
	public final void updateSelectionSelectionNotExists() {
		subjectController.updateSelection(commercialSubjectsModel);
		Mockito.verify(commercialSubjectsModel).getSelected();
		Mockito.verifyNoMoreInteractions(commercialSubjectsModel);
	}
	
	@Test
	public final void save() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		subjectController.save(commercialSubject,customer);
		
		
		
		Mockito.verify(commercialSubjectService).createOrUpdate(commercialSubject);
	
		
	}
	
	@Test
	public final void delete() {
		final CommercialSubjectAO commercialSubjectAO = Mockito.mock(CommercialSubjectAO.class);
		CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialSubjectAO.getCommercialSubject()).thenReturn(commercialSubject);
		subjectController.delete(commercialSubjectAO);
		Mockito.verify(commercialSubjectService).delete(commercialSubject);
	}
	
	@Test
	public final void deleteNullArgument() {
		subjectController.delete(null);
		Mockito.verifyNoMoreInteractions(commercialSubjectService);
	}
	
	@Test
	public final void change() {
		final Long id = 19680528L;
		final CommercialSubjectAO selected =  Mockito.mock(CommercialSubjectAO.class);
		Mockito.when(selected.getCommercialSubject()).thenReturn(commercialSubject);
		Mockito.when(commercialSubject.id()).thenReturn(id);
		final CommercialSubjectAO target = Mockito.mock(CommercialSubjectAO.class);
		Mockito.when(commercialSubjectService.subject(id)).thenReturn(commercialSubject);
		subjectController.openChangeDialog(selected, target);
		Mockito.verify(target).setCommercialSubject(commercialSubject);
	}
	
	@Test
	public final void changeNullArgumentSelected() {
		final CommercialSubjectAO target = Mockito.mock(CommercialSubjectAO.class);
		subjectController.openChangeDialog(null, target);
		Mockito.verifyNoMoreInteractions(target, commercialSubjectService);
	}

}
