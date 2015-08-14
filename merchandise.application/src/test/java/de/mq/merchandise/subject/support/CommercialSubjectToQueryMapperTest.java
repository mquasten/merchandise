package de.mq.merchandise.subject.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

public class CommercialSubjectToQueryMapperTest {
	
	private static final int PARAM_COUNTER = 7;

	private static final String CUSTOMER_FIELD = "customer";

	private static final String SUBJECT_FIELD = "subject";

	private static final String ITEM_NAME_FIELD = "name";
	
	private static final String NAME_FIELD = "name";

	private static final long SUBJECT_ID = 4711L;

	private static final String SUBJECT_DESC = "Spezielle Dienstleistungen";

	private static final String SUBJECT_NAME = "EscortService";

	private static final String ITEM_NAME = "PetStore";

	private static final String CUSTOMER_NAME = "Kylie Minogue";

	private static final long CUSTOMER_ID = 19680528L;

	private static final String NAME = "Pets4You";

	private final CommercialSubjectToQueryMapper commercialSubjectToQueryMapper = new CommercialSubjectToQueryMapper();
	
	private Customer customer = Mockito.mock(Customer.class);
	
	private final Subject subject = Mockito.mock(Subject.class);
	
	private final CommercialSubject commercialSubject = new CommercialSubjectImpl(NAME, customer);
	
	
	
	@Before
	public void  setup() {
		
		Mockito.when(subject.name()).thenReturn(SUBJECT_NAME);
		Mockito.when(subject.description()).thenReturn(SUBJECT_DESC);
		
		Mockito.when(subject.id()).thenReturn(Optional.of(SUBJECT_ID));
		commercialSubject.assign(subject, ITEM_NAME, false);
		
		Mockito.when(customer.name()).thenReturn(CUSTOMER_NAME);
		
		Mockito.when(customer.id()).thenReturn(Optional.of(CUSTOMER_ID));
		
	}
	

	@Test
	public final void convert() {
		final Map<String,Object> target = new HashMap<>();
		commercialSubjectToQueryMapper.mapInto(commercialSubject, target);
		Assert.assertEquals(PARAM_COUNTER, target.size());
		
		Assert.assertEquals(String.format(CommercialSubjectToQueryMapper.SEARCH_PATTERN,NAME), target.get(CommercialSubjectRepository.NAME_PARAM));
		Assert.assertEquals(String.format(CommercialSubjectToQueryMapper.SEARCH_PATTERN,ITEM_NAME), target.get(CommercialSubjectRepository.ITEM_NAME_PARAM));
		Assert.assertEquals(String.format(CommercialSubjectToQueryMapper.SEARCH_PATTERN,SUBJECT_NAME), target.get(CommercialSubjectRepository.SUBJECT_NAME_PARAM));
		Assert.assertEquals(String.format(CommercialSubjectToQueryMapper.SEARCH_PATTERN,SUBJECT_DESC), target.get(CommercialSubjectRepository.SUBJECT_DESCRIPTION_PARAM));
		Assert.assertEquals(SUBJECT_ID, target.get(CommercialSubjectRepository.SUBJECT_ID_PARAM));
		Assert.assertEquals(String.format(CommercialSubjectToQueryMapper.SEARCH_PATTERN,CUSTOMER_NAME), target.get(CommercialSubjectRepository.CUSTOMER_NAME_PARAM));
		Assert.assertEquals(CUSTOMER_ID, target.get(CommercialSubjectRepository.CUSTOMER_ID_PARAM));
		
		
		
	}



	
	@Test
	public final void convertAllEmpty() {
		ReflectionTestUtils.setField(commercialSubject,NAME_FIELD, null);
		
		
		
		Mockito.when(subject.name()).thenReturn(null);
		Mockito.when(subject.description()).thenReturn(null);
		
	
		Mockito.when(subject.id()).thenReturn(Optional.empty());
		
		
		Mockito.when(customer.name()).thenReturn(null);
		
		Mockito.when(customer.id()).thenReturn(Optional.empty());
		
		ReflectionTestUtils.setField(commercialSubject.commercialSubjectItem(subject).get(),ITEM_NAME_FIELD , null );
	
		
		final Map<String,Object> target = new HashMap<>();
		
		commercialSubjectToQueryMapper.mapInto(commercialSubject, target);
		
		Assert.assertEquals(PARAM_COUNTER, target.size());
		target.values().stream().filter(v ->  !(v instanceof Number)).forEach(v -> Assert.assertEquals(CommercialSubjectToQueryMapper.WILDCARD_PATTERN, v));
		target.values().stream().filter(v ->  (v instanceof Number)).forEach(v -> Assert.assertEquals(CommercialSubjectToQueryMapper.NOT_EXISTING_ID, v));
	}
	
	
	@Test
	public final void convertWithOutIds() {
		ReflectionTestUtils.setField(commercialSubject, CUSTOMER_FIELD , null);
		ReflectionTestUtils.setField(commercialSubject.commercialSubjectItem(subject).get(),SUBJECT_FIELD, null);
		final Map<String,Object> target = new HashMap<>();
		
		commercialSubjectToQueryMapper.mapInto(commercialSubject, target);
		
		Assert.assertEquals(PARAM_COUNTER, target.size());
		Assert.assertEquals(String.format(CommercialSubjectToQueryMapper.SEARCH_PATTERN,NAME), target.get(CommercialSubjectRepository.NAME_PARAM));
		Assert.assertEquals(String.format(CommercialSubjectToQueryMapper.SEARCH_PATTERN,ITEM_NAME), target.get(CommercialSubjectRepository.ITEM_NAME_PARAM));
		Assert.assertEquals(CommercialSubjectToQueryMapper.WILDCARD_PATTERN,  target.get(CommercialSubjectRepository.SUBJECT_NAME_PARAM));
		Assert.assertEquals(CommercialSubjectToQueryMapper.WILDCARD_PATTERN, target.get(CommercialSubjectRepository.SUBJECT_DESCRIPTION_PARAM));
		Assert.assertEquals(CommercialSubjectToQueryMapper.NOT_EXISTING_ID, target.get(CommercialSubjectRepository.SUBJECT_ID_PARAM));
		Assert.assertEquals(CommercialSubjectToQueryMapper.WILDCARD_PATTERN, target.get(CommercialSubjectRepository.CUSTOMER_NAME_PARAM));
		Assert.assertEquals(CommercialSubjectToQueryMapper.NOT_EXISTING_ID, target.get(CommercialSubjectRepository.CUSTOMER_ID_PARAM));
		
		
	}
	
}
