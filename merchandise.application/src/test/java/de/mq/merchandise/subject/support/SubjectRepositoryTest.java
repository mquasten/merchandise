package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;


public class SubjectRepositoryTest {
	
	private static final int PAGE_SIZE = 50;

	private static final int FIRST_ROW = 0;

	private static final Long ID = 19680528L;

	private final SubjectRepository  subjectRepository = new SubjectRepositoryImpl();
	
	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private final Subject subject = new SubjectImpl(Mockito.mock(Customer.class), "suject");
	
	@Before
	public final void setup() {
		ReflectionTestUtils.setField(subjectRepository, "entityManager", entityManager);
		ReflectionTestUtils.setField(subject, "id", ID);
		
	}
	
	
	@Test
	public final void save() {
		Mockito.when(entityManager.merge(subject)).thenReturn(subject);
		subjectRepository.save(subject);
		Assert.assertTrue(subject.id().isPresent());
		Assert.assertEquals(ID, subject.id().get());
		Mockito.verify(entityManager).merge(subject);
	}
	
	
	@Test
	public final void subjectsForCustomer() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.of(ID));
		
		final ResultNavigation paging = Mockito.mock(ResultNavigation.class) ;
		Mockito.when(paging.firstRow()).thenReturn(FIRST_ROW);
		Mockito.when(paging.pageSize()).thenReturn(PAGE_SIZE);
		@SuppressWarnings("unchecked")
		final TypedQuery<Subject> typedQuery = Mockito.mock(TypedQuery.class);
		Mockito.when( entityManager.createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class)).thenReturn(typedQuery);
		final List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Mockito.when(typedQuery.getResultList()).thenReturn(subjects);
		
		final Collection<Subject> results =  subjectRepository.subjectsForCustomer(customer, paging);
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(subject, results.stream().findFirst().get());
		Mockito.verify(typedQuery).setFirstResult(FIRST_ROW);
		Mockito.verify(typedQuery).setMaxResults(PAGE_SIZE);
		Mockito.verify(typedQuery).setParameter(SubjectRepository.ID_PARAM_NAME, ID);
		Mockito.verify(entityManager).createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class);
	}
	
	@Test
	public final void  subjectsForCustomerNotPersistent() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.empty());
		final ResultNavigation paging = Mockito.mock(ResultNavigation.class) ;
		
		Assert.assertTrue(subjectRepository.subjectsForCustomer(customer, paging).isEmpty());
		
		Mockito.verifyZeroInteractions(entityManager);
	}
	
	@Test
	public final void remove() {
		Mockito.when(entityManager.find(SubjectImpl.class, ID)).thenReturn((SubjectImpl) subject);
		subjectRepository.remove(subject);
		
		Mockito.verify(entityManager).find(SubjectImpl.class, ID);
		Mockito.verify(entityManager).remove(subject);
	}
	
	@Test
	public final void removeNotPersistent() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(subject.id()).thenReturn(Optional.empty());
		subjectRepository.remove(subject);
		Mockito.verifyNoMoreInteractions(entityManager);
	}
	
	@Test
	public final void removeNotExists() {
		subjectRepository.remove(subject);
		Mockito.verify(entityManager).find(SubjectImpl.class, ID);
		Mockito.verify(entityManager, Mockito.times(0)).remove(Mockito.any(Subject.class));
		
	}
	
	

}
