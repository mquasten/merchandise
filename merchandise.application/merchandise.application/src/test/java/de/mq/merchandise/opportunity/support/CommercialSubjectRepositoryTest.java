package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.Parameter;
import de.mq.merchandise.util.ParameterImpl;

public class CommercialSubjectRepositoryTest {
	
	private static final long CUSTOMER_ID = 4711L;
	

	private static final String PATTERN = "pattern";

	private EntityManager entityManager = Mockito.mock(EntityManager.class); 
	
	private PagingUtil pagingUtil = Mockito.mock(PagingUtil.class);
	private final CommercialSubjectRepository  commercialSubjectRepository = new CommercialSubjectRepositoryImpl(entityManager, pagingUtil);
	
	private final Customer customer = Mockito.mock(Customer.class);
	
	@Test
	public final void subjectsWithPaging() {
		Mockito.when(customer.id()).thenReturn(CUSTOMER_ID);
		
		final List<CommercialSubject> results = new ArrayList<>();
		results.add(Mockito.mock(CommercialSubject.class));
		
		final Paging paging = Mockito.mock(Paging.class);
	
	    Mockito.when(pagingUtil.countAndQuery(entityManager, CommercialSubject.class, paging, CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN , new Parameter[] { new ParameterImpl<String>(CommercialSubjectRepositoryImpl.PARAMETER_SUBJECT_NAME , PATTERN), new ParameterImpl<Long>(CommercialSubjectRepositoryImpl.PARAMETER_CUSTOMER_ID , customer.id()) }) ).thenReturn(results);
		
		Assert.assertEquals(results, commercialSubjectRepository.forNamePattern(customer, PATTERN, paging));
		
		
		
	
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new CommercialSubjectRepositoryImpl());
	}
	
	
	@Test
	public final void clazz() {
		Assert.assertEquals(CommercialSubjectImpl.class, new CommercialSubjectRepositoryImpl().entityImplementationClass());
	}

}
