package de.mq.merchandise.subject.support;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/repositories.xml" })
public class CommercialSubjectRepositoryIntegrationTest {
	
	
	private static final long CUSTOMER_ID = 1L;
	private static final String ID_FIELD = "id";
	private static final String NAME_FIELD = "name";
	private static final String NAME = "Dolls4You";
	private static final String SUBJECT_NAME = NAME;
	private static final String RESULT = "Nicole";
	private static final String QUANTITY = "Quantity";
	private static final String UNIT = "Unit";
	private static final String QUALITY = "Quality";
	@PersistenceContext
	private EntityManager entityManager;
	
	private List<Entry<Long, Class<?>>> waste = new ArrayList<>();
	
	@Autowired
	private CommercialSubjectRepository commercialSubjectRepository;
	
	@Autowired
	@MapperQualifier(MapperType.CommercialSubject2QueryMap)
	private Mapper<CommercialSubject,Map<String,Object>> mapper; 
	
	@Before
	public final void setup() {
		create();
	}

	@After
	public final void cleanup() {
		waste.forEach(e -> {
			entityManager.remove(entityManager.find(e.getValue(), e.getKey())); });
		entityManager.flush();
	}
	
	
	
	
	public void create() {
	
	final Customer customer =entityManager.find(CustomerImpl.class, 1L);


	final Subject newSubject = new SubjectImpl(customer, SUBJECT_NAME);
	newSubject.add(QUALITY, ConditionDataType.String);
	newSubject.add(UNIT, ConditionDataType.String);
	newSubject.add(QUANTITY, ConditionDataType.IntegralNumber);

	final Long id = entityManager.merge(newSubject).id().get();
	final Subject subject = entityManager.find(SubjectImpl.class, id);
	

	Arrays.asList(RESULT ,"Carmit", "Ashley", "Jessica", "Melody", "Kimberly"   ).forEach(name -> {
		final CommercialSubject commercialSubjet = createCommercialSubject(customer, subject, name);
		waste.add(new AbstractMap.SimpleEntry<>(commercialSubjet.id().get(), commercialSubjet.getClass()));
	});
	

	

	waste.add(new AbstractMap.SimpleEntry<>(subject.id().get(), subject.getClass()));
	//waste.add(new AbstractMap.SimpleEntry<>(customer.id().get(), customer.getClass()));
	entityManager.flush();
	
	}

	private CommercialSubject createCommercialSubject(final Customer customer, final Subject subject, final String name) {
		final CommercialSubject commercialSubjet = new CommercialSubjectImpl(name, customer);
		commercialSubjet.assign(subject, name, true);
		commercialSubjet.assign(subject.condition(QUALITY), "Platinium");
		commercialSubjet.assign(subject.condition(UNIT), "Private Date");
		commercialSubjet.assign(subject.condition(UNIT), "Public Date");

		
		return entityManager.merge(commercialSubjet);
	
		
	}
	
	@Test()
	@Rollback(false)
	@Transactional()
	public final void commercialSubjects() {
		final ResultNavigation paging = Mockito.mock(ResultNavigation.class);
		Mockito.when(paging.firstRow()).thenReturn(Integer.valueOf(0));
		Mockito.when(paging.pageSize()).thenReturn(Integer.valueOf(25));
		final List<Order> orders = new ArrayList<>();
		orders.add(new Order(Direction.ASC, NAME_FIELD));
		orders.add(new Order(Direction.ASC, ID_FIELD));
		
		Mockito.when(paging.orders()).thenReturn(orders);
		final Customer customer = entityManager.find(CustomerImpl.class, 1L);
		entityManager.flush();
	
		
		final CommercialSubjectImpl commercialSubject = new CommercialSubjectImpl("", customer);
		final Subject subject =  BeanUtils.instantiateClass(SubjectImpl.class);
		ReflectionTestUtils.setField(subject, ID_FIELD, -1L);
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		commercialSubject.assign(subject, RESULT , true);
		
		final Collection<CommercialSubject> results = commercialSubjectRepository.commercialSubjectsForCustomer(mapper.mapInto(commercialSubject, new HashMap<>()), paging);
	
	   Assert.assertEquals(1, results.size());
	   Assert.assertTrue(results.stream().findFirst().isPresent());
	   Assert.assertEquals(RESULT, results.stream().findFirst().get().name());
	}
	
	@Test()
	@Rollback(false)
	@Transactional()
	public final void countCommercialSubjects() {
		final Customer customer = entityManager.find(CustomerImpl.class, CUSTOMER_ID);
		CommercialSubjectImpl commercialSubject = new CommercialSubjectImpl("", customer);
		final Subject subject =  BeanUtils.instantiateClass(SubjectImpl.class);
		ReflectionTestUtils.setField(subject, ID_FIELD, -1L);
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		commercialSubject.assign(subject, RESULT , true);
		
		Assert.assertEquals(1, commercialSubjectRepository.countCommercialSubjectsForCustomer(mapper.mapInto(commercialSubject, new HashMap<>())).intValue());
	}

}
