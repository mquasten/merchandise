package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.ResultNavigation;

public class CommercialSubjectRepositoryTest {
	private static final String ENTITY_MANAGER_FIELD = "entityManager";
	private static final String NAME_FIELD_VALUE = "Name";
	private static final String NAME_FIELD = "name";
	private final CommercialSubjectRepository commercialSubjectRepository = new CommercialSubjectRepositoryImpl();
	private final EntityManager entityManager = Mockito.mock(EntityManager.class);
	private final ResultNavigation resultNavigation = Mockito.mock(ResultNavigation.class);
	
	private final Map<String,Object> criteria = new HashMap<>();
	
	private List<Order> orders = new ArrayList<>();
	
	private Order order = Mockito.mock(Order.class);
	
	@Before
	public final void setup() {
		orders.add(order);
		Mockito.when(resultNavigation.orders()).thenReturn(orders);
		ReflectionTestUtils.setField(commercialSubjectRepository, ENTITY_MANAGER_FIELD, entityManager);
		criteria.put(NAME_FIELD, NAME_FIELD_VALUE);
	}
	
	@Test
	public final void commercialSubjectsForCustomer() {
		commercialSubjectRepository.commercialSubjectsForCustomer(criteria, resultNavigation);
	}
}
