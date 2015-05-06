
package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;


public class SubjectServiceTest {
	
	private static final Integer ROW_COUNTER = Integer.valueOf(42);

	private final SubjectRepository subjectRepository = Mockito.mock(SubjectRepository.class);
	
	private SubjectService subjectService = new SubjectServiceImpl(subjectRepository);
	
	@Test
	public final void subjects() {
		final Subject subject = Mockito.mock(Subject.class);
		final ResultNavigation paging = Mockito.mock(ResultNavigation.class);
		final Collection<Subject> results = new ArrayList<>(); 
		results.add(subject);
		Mockito.when(subjectRepository.subjectsForCustomer(subject,paging)).thenReturn(results);
		Assert.assertEquals(results, subjectService.subjects(subject, paging));
		
		Mockito.verify(subjectRepository).subjectsForCustomer(subject, paging);
	}
	
	@Test
	public final void subjectsCount() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(subjectRepository.subjectsForCustomer(subject)).thenReturn(ROW_COUNTER);
		Assert.assertEquals(ROW_COUNTER, subjectService.subjects(subject));
		
	}
	
	@Test
	public final void save() {
		final Subject subject = Mockito.mock(Subject.class);
		subjectService.save(subject);
		
		Mockito.verify(subjectRepository).save(subject);
	}
	
	@Test
	public final void  remove() {
		final Subject subject = Mockito.mock(Subject.class);
		
		subjectService.remove(subject);
		
		Mockito.verify(subjectRepository).remove(subject);
	}
	

}
