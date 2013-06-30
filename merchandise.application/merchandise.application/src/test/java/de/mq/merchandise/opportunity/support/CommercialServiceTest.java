package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.util.Paging;

public class CommercialServiceTest {
	
	
	private static final long ID = 19680528L;

	private  final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);

	private static final String NAME_PATTERN = "patternForName";

	private final CommercialSubjectRepository commercialSubjectRepository = Mockito.mock(CommercialSubjectRepository.class);
	
	private final CommercialSubjectService commercialSubjectService = new CommercialSubjectServiceImpl(commercialSubjectRepository);
	
	private final Paging paging = Mockito.mock(Paging.class);
	
	private final Customer customer = Mockito.mock(Customer.class);
		
	
	@Test
	public final void subjects() {
		Mockito.when(customer.id()).thenReturn(ID);
		final List<CommercialSubject> subjects = new ArrayList<>();
		subjects.add(commercialSubject);
		Mockito.when(commercialSubjectRepository.forNamePattern(customer, NAME_PATTERN, paging)).thenReturn(subjects);
		
		final Collection<CommercialSubject> results = commercialSubjectService.subjects(customer, NAME_PATTERN, paging);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(commercialSubject, results.iterator().next());
		
		Mockito.verify(commercialSubjectRepository).forNamePattern(customer, NAME_PATTERN, paging);
	}
	
	
}
