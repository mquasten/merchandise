package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;


public class SubjectRepositoryTest {
	
	private static final long COUNTER = 42L;

	private static final String DESCRIPTION_PATTERN = "Description";

	private static final String NAME_PATTERN = "Name";

	private static final String QUERY_STRING_FROM_NAMED_QUERY = "select s from Subject ...";

	private static final int PAGE_SIZE = 50;

	private static final int FIRST_ROW = 0;

	private static final Long ID = 19680528L;

	private final SubjectRepository  subjectRepository = new SubjectRepositoryImpl();
	
	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private final Subject subject = new SubjectImpl(Mockito.mock(Customer.class), "suject");
	
	final Subject searchCriteria = Mockito.mock(Subject.class);
	
	final Customer customer = Mockito.mock(Customer.class);
	final ResultNavigation paging = Mockito.mock(ResultNavigation.class) ;
	
	@SuppressWarnings("unchecked")
	final TypedQuery<Subject> typedQuery = Mockito.mock(TypedQuery.class);
	
	final Query hibernateQuery = Mockito.mock(Query.class);
	
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
		
		prepareSearch();
		
		final Collection<Subject> results =  subjectRepository.subjectsForCustomer(searchCriteria, paging);
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(searchCriteria, results.stream().findFirst().get());
		Mockito.verify(typedQuery).setFirstResult(FIRST_ROW);
		Mockito.verify(typedQuery).setMaxResults(PAGE_SIZE);
		Mockito.verify(typedQuery).setParameter(SubjectRepository.ID_PARAM_NAME, ID);
		
		Mockito.verify(typedQuery).setParameter(SubjectRepository.NAME_PARAM_NAME, "%");
		Mockito.verify(typedQuery).setParameter(SubjectRepository.DESC_PARAM_NAME, "%");
		
		Mockito.verify(entityManager).createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class);
	}


	@SuppressWarnings("unchecked")
	private void prepareSearch() {
		Mockito.when(customer.id()).thenReturn(Optional.of(ID));
		Mockito.when(searchCriteria.customer()).thenReturn(customer);
		
		Mockito.when(paging.firstRow()).thenReturn(FIRST_ROW);
		Mockito.when(paging.pageSize()).thenReturn(PAGE_SIZE);
	
		
		Mockito.when(hibernateQuery.getQueryString()).thenReturn(QUERY_STRING_FROM_NAMED_QUERY);
		Mockito.when(typedQuery.unwrap(Mockito.any())).thenReturn(hibernateQuery);
		
		Mockito.when(entityManager.createQuery(Mockito.anyString(),  (Class<Subject>) Mockito.any())).thenReturn(typedQuery);
		
		Mockito.when( entityManager.createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class)).thenReturn(typedQuery);
		final List<Subject> subjects = new ArrayList<>();
		subjects.add(searchCriteria);
		Mockito.when(typedQuery.getResultList()).thenReturn(subjects);
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void subjectsForCustomerWithCriteria() {
		prepareSearch();
		Mockito.when(searchCriteria.name()).thenReturn(NAME_PATTERN);
		Mockito.when(searchCriteria.description()).thenReturn(DESCRIPTION_PATTERN);
		final List<Order> orders = new ArrayList<>();
		final Order orderName = Mockito.mock(Order.class);
		Mockito.when(orderName.getProperty()).thenReturn(SubjectRepository.NAME_PARAM_NAME);
		Mockito.when(orderName.getDirection()).thenReturn(Direction.ASC);
		orders.add(orderName);
		final Order orderId = Mockito.mock(Order.class);
		Mockito.when(orderId.getProperty()).thenReturn(SubjectRepository.ID_PARAM_NAME);
		Mockito.when(orderId.getDirection()).thenReturn(Direction.ASC);
		orders.add(orderId);
		
		Mockito.when(paging.orders()).thenReturn(orders);
		final Collection<Subject> results =  subjectRepository.subjectsForCustomer(searchCriteria, paging);
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(searchCriteria, results.stream().findFirst().get());
		Mockito.verify(typedQuery).setFirstResult(FIRST_ROW);
		Mockito.verify(typedQuery).setMaxResults(PAGE_SIZE);
		Mockito.verify(typedQuery).setParameter(SubjectRepository.ID_PARAM_NAME, ID);
		
		Mockito.verify(typedQuery).setParameter(SubjectRepository.NAME_PARAM_NAME, NAME_PATTERN + "%");
		Mockito.verify(typedQuery).setParameter(SubjectRepository.DESC_PARAM_NAME, DESCRIPTION_PATTERN+ "%");
		
		Mockito.verify(entityManager).createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class);
		
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass( Class.class);
		
		final ArgumentCaptor<String> qlCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(entityManager).createQuery(qlCaptor.capture(), classCaptor.capture());
		
		Assert.assertEquals(String.format("%s order by %s ASC,%s ASC", QUERY_STRING_FROM_NAMED_QUERY, SubjectRepository.NAME_PARAM_NAME , SubjectRepository.ID_PARAM_NAME), qlCaptor.getValue());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void subjectsForCustomerCount() {
		
		prepareSearch();

		Mockito.when(((TypedQuery)typedQuery).getSingleResult()).thenReturn(COUNTER);
		Assert.assertEquals(COUNTER, subjectRepository.subjectsForCustomer(searchCriteria));
		
		Mockito.verify(typedQuery).setParameter(SubjectRepository.ID_PARAM_NAME, ID);
		
		Mockito.verify(typedQuery).setParameter(SubjectRepository.NAME_PARAM_NAME, "%");
		Mockito.verify(typedQuery).setParameter(SubjectRepository.DESC_PARAM_NAME, "%");
		
		Mockito.verify(entityManager).createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class);
		
		
		
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass( Class.class);
		
		final ArgumentCaptor<String> qlCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(entityManager).createQuery(qlCaptor.capture(), classCaptor.capture());
		
		Assert.assertTrue(qlCaptor.getValue().toLowerCase().startsWith("select count(s)"));
	}
	
	@Test
	public final void  subjectsForCustomerNotPersistent() {
		final Subject subject = Mockito.mock(Subject.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.empty());
		Mockito.when(subject.customer()).thenReturn(customer);
		final ResultNavigation paging = Mockito.mock(ResultNavigation.class) ;
		
		Assert.assertTrue(subjectRepository.subjectsForCustomer(subject, paging).isEmpty());
		
		Mockito.verifyZeroInteractions(entityManager);
	}
	
	@Test
	public final void  subjectsForCustomerCountNotPersistent() {
		final Subject subject = Mockito.mock(Subject.class);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.empty());
		Mockito.when(subject.customer()).thenReturn(customer);
	
		
		Assert.assertEquals(0, subjectRepository.subjectsForCustomer(subject));
		
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
	
	@Test
	public final void subject() {
		Mockito.when(entityManager.find( SubjectImpl.class, ID)).thenReturn( (SubjectImpl) subject);
		
		Assert.assertEquals(subject, subjectRepository.subject(ID));
	}
	
	@Test
	public final void subjects() {
		Mockito.when(customer.id()).thenReturn(Optional.of(ID));
		Mockito.when(entityManager.createNamedQuery(SubjectRepositoryImpl.SUBJECTS_MAP_FOR_CUSTOMER_QUERY,Subject.class)).thenReturn(typedQuery);
		final List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Mockito.when(typedQuery.getResultList()).thenReturn(subjects);
		
		final Collection<Subject> results = subjectRepository.subjectsForCustomer(customer);
		Assert.assertEquals(1, results.size());
		Assert.assertTrue(results.stream().findAny().isPresent());
		Assert.assertEquals(subject, results.stream().findAny().get());
		Mockito.verify(entityManager).createNamedQuery(SubjectRepositoryImpl.SUBJECTS_MAP_FOR_CUSTOMER_QUERY,Subject.class);
		Mockito.verify(typedQuery).setParameter(SubjectRepository.ID_PARAM_NAME, ID);
		Mockito.verify(typedQuery).getResultList();
	}
	

}
