package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public class CommercialSubjectItemTest {
	
	private static final String CONDITION_VALUE = "> 500 000 000 $";

	private static final String COMMERCIAL_SUBJECT_ITEM_CONDITIONS_FIELDS = "commercialSubjectItemConditions";

	private static final String COMMERCIAL_SUBJET_FIELD = "commercialSubjet";

	private static final Long ID = 19680528L;

	private static final String ITEM_NAME = "Banking-Service";

	private final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	
	private final Subject subject = Mockito.mock(Subject.class);
	
	private  CommercialSubjectItem  commercialSubjectItem;
	
	private Condition condition = Mockito.mock(Condition.class);
	
	@Before
	public final void setup() {
		Mockito.when(subject.id()).thenReturn(Optional.of(ID));
		
		Collection<Condition> conditions = new ArrayList<>();
		conditions.add(condition);
		Mockito.when(subject.conditions()).thenReturn(conditions);
		Mockito.when(condition.id()).thenReturn(Optional.of(ID));
		Mockito.when(condition.subject()).thenReturn(subject);
		
		
		commercialSubjectItem = new CommercialSubjectItemImpl(ITEM_NAME, commercialSubject, subject, true);
	
	}
	
	@Test
	public final void create() {
	
		Assert.assertEquals(ITEM_NAME, commercialSubjectItem.name());
		Assert.assertEquals(subject, commercialSubjectItem.subject());
		Assert.assertEquals(commercialSubject, ReflectionTestUtils.getField(commercialSubjectItem, COMMERCIAL_SUBJET_FIELD));
		
		final Collection<CommercialSubjectItemConditionImpl> items = items();
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.stream().findAny().isPresent());
		Assert.assertEquals(condition, items.stream().findAny().get().condition());
		
	}

	@SuppressWarnings("unchecked")
	private Collection<CommercialSubjectItemConditionImpl> items() {
		return (Collection<CommercialSubjectItemConditionImpl>) ReflectionTestUtils.getField(commercialSubjectItem, COMMERCIAL_SUBJECT_ITEM_CONDITIONS_FIELDS );
	}
	
	@Test
	public final void conditionValues() {
		final Collection<CommercialSubjectItemConditionImpl> items = items();
		Assert.assertTrue(items.stream().findAny().isPresent());
	
		items.stream().findAny().get().assign(CONDITION_VALUE);
		
		final Collection<Entry<Condition,Collection<String>>> results = commercialSubjectItem.conditionValues();
		Assert.assertEquals(1, results.size());
		Assert.assertTrue(results.stream().findAny().isPresent());
		Assert.assertEquals(condition, results.stream().findAny().get().getKey());
		
		Assert.assertEquals(1,  results.stream().findAny().get().getValue().size());
		Assert.assertTrue( results.stream().findAny().get().getValue().stream().findAny().isPresent());
		Assert.assertEquals(CONDITION_VALUE, results.stream().findAny().get().getValue().stream().findAny().get());
		
	}
	
	
}
