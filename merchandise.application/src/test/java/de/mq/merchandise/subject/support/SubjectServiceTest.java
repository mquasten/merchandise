
package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;


public class SubjectServiceTest {
	
	private static final long ID = 19680528L;

	private static final Integer ROW_COUNTER = Integer.valueOf(42);

	private final SubjectRepository subjectRepository = Mockito.mock(SubjectRepository.class);
	
	private SubjectService subjectService = new SubjectServiceImpl(subjectRepository);
	private final Subject subject = Mockito.mock(Subject.class);
	
	@Test
	public final void subjects() {
		
		final ResultNavigation paging = Mockito.mock(ResultNavigation.class);
		final Collection<Subject> results = new ArrayList<>(); 
		results.add(subject);
		Mockito.when(subjectRepository.subjectsForCustomer(subject,paging)).thenReturn(results);
		Assert.assertEquals(results, subjectService.subjects(subject, paging));
		
		Mockito.verify(subjectRepository).subjectsForCustomer(subject, paging);
	}
	
	@Test
	public final void subjectsCount() {
	
		Mockito.when(subjectRepository.subjectsForCustomer(subject)).thenReturn(ROW_COUNTER);
		Assert.assertEquals(ROW_COUNTER, subjectService.subjects(subject));
		
	}
	
	@Test
	public final void save() {
		
		subjectService.save(subject);
		
		Mockito.verify(subjectRepository).save(subject);
	}
	
	@Test
	public final void  remove() {
		
		
		subjectService.remove(subject);
		
		Mockito.verify(subjectRepository).remove(subject);
	}
	
	@Test
	public final void subject() {
		Mockito.when(subjectRepository.subject(ID)).thenReturn(subject);
		
		Assert.assertEquals(subject, subjectService.subject(19680528L));
	}
	
	
	@Test
	public final void  subjects4Customer() {
		final Customer customer = Mockito.mock(Customer.class);
		final Collection<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Mockito.when(subjectRepository.subjectsForCustomer(customer)).thenReturn(subjects);
		
		final Collection<Subject> results = subjectService.subjects(customer);
		Assert.assertEquals(1, results.size());
		
		Assert.assertTrue(results.stream().findAny().isPresent());
		Assert.assertEquals(subject, results.stream().findAny().get());
	}

}
