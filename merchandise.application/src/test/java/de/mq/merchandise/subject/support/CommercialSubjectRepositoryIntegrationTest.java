package de.mq.merchandise.subject.support;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectMapper.SubjectMapperType;
import de.mq.merchandise.support.Mapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/repositories.xml" })
public class CommercialSubjectRepositoryIntegrationTest {
	
	
	private static final String QUANTITY = "Quantity";
	private static final String UNIT = "Unit";
	private static final String QUALITY = "Quality";
	@PersistenceContext
	private EntityManager entityManager;
	
	private List<Entry<Long, Class<?>>> waste = new ArrayList<>();
	
	@Autowired
	private CommercialSubjectRepository commercialSubjectRepository;
	
	@Autowired
	@SubjectMapper(SubjectMapperType.CommercialSubject2QueryMap)
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


	final Subject newSubject = new SubjectImpl(customer, "Dolls4You");
	newSubject.add(QUALITY, ConditionDataType.String);
	newSubject.add(UNIT, ConditionDataType.String);
	newSubject.add(QUANTITY, ConditionDataType.IntegralNumber);

	final Long id = entityManager.merge(newSubject).id().get();
	final Subject subject = entityManager.find(SubjectImpl.class, id);
	

	Arrays.asList("Nicole" ,"Carmit", "Ashley", "Jessica", "Melody", "Kimberly"   ).forEach(name -> {
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
		Customer customer = entityManager.find(CustomerImpl.class, 1L);
		entityManager.flush();
		System.out.println(">>>" + mapper);
		System.out.println("***");
		System.out.println(commercialSubjectRepository.forCriteria(mapper.mapInto(new CommercialSubjectImpl("Nicole", customer), new HashMap<>())));
	}

}
