package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public class CommercialSubjectItemTest {

	private static final String CONDTION_TYPE = "CondtionType";

	private static final String CONDITION_VALUE = "> 500 000 000 $";

	private static final String COMMERCIAL_SUBJECT_ITEM_CONDITIONS_FIELDS = "commercialSubjectItemConditions";

	private static final String COMMERCIAL_SUBJET_FIELD = "commercialSubjet";

	private static final Long ID = 19680528L;

	private static final String ITEM_NAME = "Banking-Service";

	private final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);

	private final Subject subject = Mockito.mock(Subject.class);

	private CommercialSubjectItem commercialSubjectItem;

	private Condition condition = Mockito.mock(Condition.class);

	@Before
	public final void setup() {
		Mockito.when(subject.id()).thenReturn(Optional.of(ID));

		Collection<Condition> conditions = new ArrayList<>();
		conditions.add(condition);
		Mockito.when(subject.conditions()).thenReturn(conditions);
		Mockito.when(condition.id()).thenReturn(Optional.of(ID));
		Mockito.when(condition.subject()).thenReturn(subject);

		Mockito.when(condition.conditionType()).thenReturn(CONDTION_TYPE);

		commercialSubjectItem = new CommercialSubjectItemImpl(ITEM_NAME, commercialSubject, subject);

	}

	@Test
	public final void create() {

		Assert.assertEquals(ITEM_NAME, commercialSubjectItem.name());
		Assert.assertTrue(commercialSubjectItem.mandatory());
		Assert.assertEquals(subject, commercialSubjectItem.subject());
		Assert.assertEquals(commercialSubject, ReflectionTestUtils.getField(commercialSubjectItem, COMMERCIAL_SUBJET_FIELD));

		final Collection<CommercialSubjectItemConditionImpl> items = items();
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.stream().findAny().isPresent());
		Assert.assertEquals(condition, items.stream().findAny().get().condition());
		Assert.assertEquals(commercialSubject, commercialSubjectItem.commercialSubject());

	}

	@Test
	public final void createDefault() {
		Assert.assertTrue(BeanUtils.instantiateClass(CommercialSubjectItemImpl.class) instanceof CommercialSubjectItemImpl);
	}

	@SuppressWarnings("unchecked")
	private Collection<CommercialSubjectItemConditionImpl> items() {
		return (Collection<CommercialSubjectItemConditionImpl>) ReflectionTestUtils.getField(commercialSubjectItem, COMMERCIAL_SUBJECT_ITEM_CONDITIONS_FIELDS);
	}

	@Test
	public final void conditionValues() {
		final Collection<CommercialSubjectItemConditionImpl> items = items();
		Assert.assertTrue(items.stream().findAny().isPresent());

		items.stream().findAny().get().assign(CONDITION_VALUE);

		final Collection<Entry<Condition, Collection<String>>> results = commercialSubjectItem.conditionValues();
		Assert.assertEquals(1, results.size());
		Assert.assertTrue(results.stream().findAny().isPresent());
		Assert.assertEquals(condition, results.stream().findAny().get().getKey());

		Assert.assertEquals(1, results.stream().findAny().get().getValue().size());
		Assert.assertTrue(results.stream().findAny().get().getValue().stream().findAny().isPresent());
		Assert.assertEquals(CONDITION_VALUE, results.stream().findAny().get().getValue().stream().findAny().get());

	}

	@Test
	public void assign() {
		final Collection<CommercialSubjectItemConditionImpl> items = items();
		Assert.assertTrue(items.stream().findAny().isPresent());
		Assert.assertTrue(items.stream().findAny().get().values().isEmpty());
		commercialSubjectItem.assign(CONDTION_TYPE, CONDITION_VALUE);
		Assert.assertEquals(1, items.stream().findAny().get().values().size());
		Assert.assertTrue(items.stream().findAny().get().values().stream().findAny().isPresent());
		Assert.assertEquals(CONDITION_VALUE, items.stream().findAny().get().values().stream().findAny().get());

	}

	@Test
	public void remove() {
		final Collection<CommercialSubjectItemConditionImpl> items = items();
		Assert.assertTrue(items.stream().findAny().isPresent());
		items.stream().findAny().get().assign(CONDITION_VALUE);
		Assert.assertEquals(1, items.stream().findAny().get().values().size());
		commercialSubjectItem.remove(CONDTION_TYPE, CONDITION_VALUE);
		Assert.assertTrue(items.stream().findAny().get().values().isEmpty());

	}

	@Test
	public void hash() {
		Assert.assertEquals(subject.hashCode() + commercialSubject.hashCode(), commercialSubjectItem.hashCode());
		final CommercialSubjectItem invalid = BeanUtils.instantiateClass(CommercialSubjectItemImpl.class);
		Assert.assertEquals(System.identityHashCode(invalid), invalid.hashCode());

		final CommercialSubjectItem otherInvalid = new CommercialSubjectItemImpl(ITEM_NAME, commercialSubject, subject);
		ReflectionTestUtils.setField(otherInvalid, COMMERCIAL_SUBJET_FIELD, null);
		Assert.assertEquals(System.identityHashCode(otherInvalid), otherInvalid.hashCode());

	}

	@Test
	public final void equals() {
		final Subject otherSubject = Mockito.mock(Subject.class);
		Mockito.when(otherSubject.id()).thenReturn(Optional.of(ID));
		final CommercialSubjectItem invalid = BeanUtils.instantiateClass(CommercialSubjectItemImpl.class);
		Assert.assertTrue(invalid.equals(invalid));
		Assert.assertFalse(invalid.equals(BeanUtils.instantiateClass(CommercialSubjectItemImpl.class)));
		Assert.assertFalse(commercialSubjectItem.equals(CONDITION_VALUE));

		Assert.assertFalse(commercialSubjectItem.equals(invalid));

		Assert.assertTrue(commercialSubjectItem.equals(new CommercialSubjectItemImpl(ITEM_NAME, commercialSubject, subject)));
		Assert.assertFalse(commercialSubjectItem.equals(new CommercialSubjectItemImpl(ITEM_NAME, Mockito.mock(CommercialSubject.class), subject)));

		Assert.assertFalse(commercialSubjectItem.equals(new CommercialSubjectItemImpl(ITEM_NAME, commercialSubject, otherSubject)));
	}

}
