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

public class CommercialSubjectRepositoryTest {
	
	private static final long CUSTOMER_ID = 4711L;
	private static final long ID = 19680528L;
	//private static final String PARAMETER_NAME = "name";
	private static final long RESULT_COUNT = 1968L;
	private static final String PATTERN = "pattern";
	private static final String QUERY = "select s from CommercialSubject s where ...";
	private EntityManager entityManager = Mockito.mock(EntityManager.class); 
	private final CommercialSubjectRepository  commercialSubjectRepository = new CommercialSubjectRepositoryImpl(entityManager);
	
	private final Customer customer = Mockito.mock(Customer.class);
	
	@Test
	public final void subjectsWithPaging() {
		Mockito.when(customer.id()).thenReturn(CUSTOMER_ID);
		
		List<CommercialSubject> results = new ArrayList<>();
		results.add(Mockito.mock(CommercialSubject.class));
		
		final Paging paging = Mockito.mock(Paging.class);
		Mockito.when(paging.firstRow()).thenReturn(1900);
		Mockito.when(paging.sortHint()).thenReturn(CommercialSubjectRepositoryImpl.PARAMETER_SUBJECT_NAME);
		Mockito.when(paging.pageSize()).thenReturn(100);
		
		final Query query = Mockito.mock(Query.class);
		Mockito.when(entityManager.createNamedQuery(CommercialSubjectRepository.SUBJECT_FOR_NAME_PATTERN)).thenReturn(query);
		
		org.hibernate.Query hibernateQuery = Mockito.mock(org.hibernate.Query.class);
		Mockito.when(hibernateQuery.getQueryString()).thenReturn(QUERY);
		Mockito.when(query.unwrap(org.hibernate.Query.class)).thenReturn(hibernateQuery);
		
		@SuppressWarnings("unchecked")
		final TypedQuery<Number> countQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(entityManager.createQuery(QUERY.replaceFirst("s[ ]", "count(s) ") , Number.class)).thenReturn(countQuery);
		Mockito.when(countQuery.getSingleResult()).thenReturn(RESULT_COUNT);
		
		@SuppressWarnings("unchecked")
		TypedQuery<CommercialSubject> pageQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(entityManager.createQuery(QUERY + " order by " + paging.sortHint() , CommercialSubject.class)).thenReturn(pageQuery);
		Mockito.when(pageQuery.getResultList()).thenReturn(results);
		
		Assert.assertEquals(results, commercialSubjectRepository.forNamePattern(customer, PATTERN, paging));
		
		
		Mockito.verify(countQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_SUBJECT_NAME, PATTERN);
		Mockito.verify(countQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_CUSTOMER_ID, CUSTOMER_ID);
		Mockito.verify(paging).assignRowCounter(RESULT_COUNT);
		
		Mockito.verify(pageQuery).setFirstResult(paging.firstRow());
		Mockito.verify(pageQuery).setMaxResults(paging.pageSize());
		Mockito.verify(pageQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_SUBJECT_NAME, PATTERN);
		Mockito.verify(pageQuery).setParameter(CommercialSubjectRepositoryImpl.PARAMETER_CUSTOMER_ID, CUSTOMER_ID);
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
