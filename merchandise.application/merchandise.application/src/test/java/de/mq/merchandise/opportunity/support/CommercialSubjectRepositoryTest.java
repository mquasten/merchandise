package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectRepository;
import de.mq.merchandise.opportunity.support.CommercialSubjectRepositoryImpl;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.Parameter;
import de.mq.merchandise.util.ParameterImpl;

public class CommercialSubjectRepositoryTest {
	
	private static final long CUSTOMER_ID = 4711L;
	private static final long ID = 19680528L;
	
	private static final long RESULT_COUNT = 1968L;
	private static final String PATTERN = "pattern";

	private EntityManager entityManager = Mockito.mock(EntityManager.class); 
	
	private PagingUtil pagingUtil = Mockito.mock(PagingUtil.class);
	private final CommercialSubjectRepository  commercialSubjectRepository = new CommercialSubjectRepositoryImpl(entityManager, pagingUtil);
	
	private final Customer customer = Mockito.mock(Customer.class);
	
	@Test
	public final void subjectsWithPaging() {
		Mockito.when(customer.id()).thenReturn(CUSTOMER_ID);
		
		List<CommercialSubject> results = new ArrayList<>();
		results.add(Mockito.mock(CommercialSubject.class));
		
		final Paging paging = Mockito.mock(Paging.class);
	System.out.println(results);
	
	
	System.out.println(entityManager);
	System.out.println(paging);
	Parameter[]  fuck = new Parameter[] { new ParameterImpl<String>(CommercialSubjectRepositoryImpl.PARAMETER_SUBJECT_NAME , PATTERN), new ParameterImpl<Long>(CommercialSubjectRepositoryImpl.PARAMETER_CUSTOMER_ID , customer.id()) };
		Mockito.when(pagingUtil.countAndQuery(entityManager, CommercialSubject.class, paging, CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN , fuck) ).thenReturn(results);
		
		System.out.println(commercialSubjectRepository.forNamePattern(customer, PATTERN, paging));
		
		
	
		//Mockito.verify(countQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_SUBJECT_NAME, PATTERN);
		//Mockito.verify(countQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_CUSTOMER_ID, CUSTOMER_ID);
		//Mockito.verify(paging).assignRowCounter(RESULT_COUNT);
		
	//	Mockito.verify(pageQuery).setFirstResult(paging.firstRow());
	//	Mockito.verify(pageQuery).setMaxResults(paging.pageSize());
	//	Mockito.verify(pageQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_SUBJECT_NAME, PATTERN);
	//	Mockito.verify(pageQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_CUSTOMER_ID, CUSTOMER_ID);
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new CommercialSubjectRepositoryImpl());
	}
	
	@Test
	public final void save() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(entityManager.merge(commercialSubject)).thenReturn(commercialSubject);
		Assert.assertEquals(commercialSubject, commercialSubjectRepository.save(commercialSubject));
		Mockito.verify(entityManager).merge(commercialSubject);
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public final void delete() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialSubject.hasId()).thenReturn(true);
		Mockito.when(commercialSubject.id()).thenReturn(ID);
		Mockito.when(entityManager.find((Class<CommercialSubject>)commercialSubject.getClass(),ID)).thenReturn(commercialSubject);
		
		commercialSubjectRepository.delete(commercialSubject);
		
		Mockito.verify(entityManager).remove(commercialSubject);
	}
	
	@Test
	public final void deleteNotFound() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialSubject.hasId()).thenReturn(true);
		Mockito.when(commercialSubject.id()).thenReturn(ID);
		
		commercialSubjectRepository.delete(commercialSubject);
		
		Mockito.verify(entityManager).find(commercialSubject.getClass(), ID);
		Mockito.verifyNoMoreInteractions(entityManager);
		
	}
	@Test(expected=IllegalArgumentException.class)
	public final void deleteNoId() {
		commercialSubjectRepository.delete(Mockito.mock(CommercialSubject.class));
	}
	
	@Test
	public final void forId() {
		final CommercialSubjectImpl result = Mockito.mock(CommercialSubjectImpl.class);
		Mockito.when(entityManager.find(CommercialSubjectImpl.class, ID)).thenReturn(result);
		Assert.assertEquals(result, commercialSubjectRepository.forId(ID));
		Mockito.verify(entityManager).find(CommercialSubjectImpl.class, ID);
	}

}
