package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.ResultNavigation;

public class CommercialSubjectRepositoryTest {
	private static final long ID = 19680528L;
	private static final int ROW_COUNTER = 42;
	private static final String ID_FIELD = "id";
	private static final int PAGE_SIZE = 50;
	private static final int FIRST_PAGE = 10;
	private static final String NAMED_QUERY_TEXT = "Select distinct cs from CommercialSubject cs";
	private static final String ENTITY_MANAGER_FIELD = "entityManager";
	private static final String NAME_FIELD_VALUE = "Name";
	private static final String NAME_FIELD = "name";
	private final CommercialSubjectRepository commercialSubjectRepository = new CommercialSubjectRepositoryImpl();
	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	private final ResultNavigation resultNavigation = Mockito.mock(ResultNavigation.class);
	@SuppressWarnings("unchecked")
	private final TypedQuery<CommercialSubject> typedQuery = Mockito.mock(TypedQuery.class);
	
	private CommercialSubject commercialSubject = BeanUtils.instantiateClass(CommercialSubjectImpl.class);

	private final Map<String, Object> criteria = new HashMap<>();

	private List<Order> orders = new ArrayList<>();

	private Order order = Mockito.mock(Order.class);

	private Order defaultOrder = Mockito.mock(Order.class);

	@Before
	public final void setup() {

		Mockito.when(order.getProperty()).thenReturn(NAME_FIELD);
		Mockito.when(order.getDirection()).thenReturn(Direction.ASC);

		Mockito.when(defaultOrder.getProperty()).thenReturn(ID_FIELD);
		Mockito.when(defaultOrder.getDirection()).thenReturn(Direction.ASC);

		orders.add(order);
		orders.add(defaultOrder);
		Mockito.when(resultNavigation.orders()).thenReturn(orders);
		ReflectionTestUtils.setField(commercialSubjectRepository, ENTITY_MANAGER_FIELD, entityManager);
		Mockito.when(resultNavigation.firstRow()).thenReturn(FIRST_PAGE);
		Mockito.when(resultNavigation.pageSize()).thenReturn(PAGE_SIZE);
		criteria.put(NAME_FIELD, NAME_FIELD_VALUE);
	}

	@Test
	public final void commercialSubjectsForCustomer() {

		org.hibernate.Query query = Mockito.mock(org.hibernate.Query.class);
		Mockito.when(typedQuery.unwrap(org.hibernate.Query.class)).thenReturn(query);
		Mockito.when(query.getQueryString()).thenReturn(NAMED_QUERY_TEXT);
		Mockito.when(entityManager.createNamedQuery(CommercialSubjectRepository.COMMERCIAL_SUBJECT_BY_CRITERIA, CommercialSubject.class)).thenReturn(typedQuery);
		Mockito.when(entityManager.createQuery(order(), CommercialSubject.class)).thenReturn(typedQuery);

		commercialSubjectRepository.commercialSubjectsForCustomer(criteria, resultNavigation);

		Mockito.verify(entityManager).createQuery(order(), CommercialSubject.class);
		Mockito.verify(typedQuery).setParameter(NAME_FIELD, NAME_FIELD_VALUE);

		Mockito.verify(typedQuery).setFirstResult(FIRST_PAGE);
		Mockito.verify(typedQuery).setMaxResults(PAGE_SIZE);
	}

	private String order() {
		return String.format("%s order by %s %s,%s %S", NAMED_QUERY_TEXT, NAME_FIELD, Direction.ASC, ID_FIELD, Direction.ASC);
	}
	
	
	@Test
	public final void  countCommercialSubjectsForCustomer() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Number> counterQuery = Mockito.mock(TypedQuery.class);
		org.hibernate.Query query = Mockito.mock(org.hibernate.Query.class);
		Mockito.when(query.getQueryString()).thenReturn(NAMED_QUERY_TEXT);
		Mockito.when(entityManager.createNamedQuery(CommercialSubjectRepository.COMMERCIAL_SUBJECT_BY_CRITERIA, CommercialSubject.class)).thenReturn(typedQuery);
		Mockito.when(typedQuery.unwrap(org.hibernate.Query.class)).thenReturn(query);
		Mockito.when(counterQuery.getSingleResult()).thenReturn(ROW_COUNTER);
		Mockito.when(entityManager.createQuery(NAMED_QUERY_TEXT.replaceFirst("distinct[ ]+cs", "count(distinct cs)"), Number.class)).thenReturn(counterQuery);
		Assert.assertEquals(ROW_COUNTER, commercialSubjectRepository.countCommercialSubjectsForCustomer(criteria));
		
		
		Mockito.verify(counterQuery).setParameter(NAME_FIELD, NAME_FIELD_VALUE);
	}
	
	@Test
	public final void save() {
		final CommercialSubject mergedCommercialSubject = BeanUtils.instantiateClass(CommercialSubjectImpl.class);
		ReflectionTestUtils.setField(mergedCommercialSubject, ID_FIELD, ID);
		
		Mockito.when(entityManager.merge(commercialSubject)).thenReturn(mergedCommercialSubject);
		
		commercialSubjectRepository.save(commercialSubject);
		Mockito.verify(entityManager).merge(commercialSubject);
		Assert.assertEquals(Optional.of(ID), commercialSubject.id());
		
	}
	
	@Test
	public final void  commercialSubject() {
		Mockito.when(entityManager.find(CommercialSubjectImpl.class, ID)).thenReturn((CommercialSubjectImpl)commercialSubject);
		Assert.assertEquals(commercialSubject, commercialSubjectRepository.commercialSubject(ID));
	}
	
	@Test
	public final void remove() {
		Mockito.when(entityManager.find(CommercialSubjectImpl.class, ID)).thenReturn((CommercialSubjectImpl)commercialSubject);
		ReflectionTestUtils.setField(commercialSubject, ID_FIELD, ID);
		
		commercialSubjectRepository.remove(commercialSubject);
		
		Mockito.verify(entityManager).remove(commercialSubject);
	}
	
	@Test
	public final void removeAlreadyDeleted() {
		ReflectionTestUtils.setField(commercialSubject, ID_FIELD, ID);
		
		commercialSubjectRepository.remove(commercialSubject);
		
		Mockito.verify(entityManager, Mockito.times(1)).find(CommercialSubjectImpl.class, ID);
		Mockito.verify(entityManager, Mockito.times(0)).remove(Mockito.any());
		
	}
	
	@Test
	public final void removeNotPersistent() {
		
		commercialSubjectRepository.remove(commercialSubject);
		Mockito.verifyZeroInteractions(entityManager);
		
	}
}
