package de.mq.merchandise.subject.support;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.Paging;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

public class SubjectServiceTest {
	
	final SubjectRepository subjectRepository = Mockito.mock(SubjectRepository.class);
	
	private SubjectService subjectService = new SubjectServiceImpl(subjectRepository);
	
	@Test
	public final void subjects() {
		final Customer customer = Mockito.mock(Customer.class);
		final Paging paging = Mockito.mock(Paging.class);
		subjectService.subjects(customer, paging);
		
		Mockito.verify(subjectRepository).subjectsForCustomer(customer, paging);
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
