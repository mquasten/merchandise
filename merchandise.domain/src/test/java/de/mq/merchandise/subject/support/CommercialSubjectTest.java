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

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public class CommercialSubjectTest {

	private static final String NAME_FIELD = "name";
	private static final String CUSTOMER_FIELD = "customer";
	private static final String CONDITION_VALUE = "ConditionValue";
	private static final String CONDITION_TYPE = "ConditionType";
	private static final long SUBJECT_ID = 19680528L;
	private static final String ITEM_NAME = "GoldmanSachs-Infrastructure";
	private static final String NAME = "AddiesBankingService";
	private final Customer customer = Mockito.mock(Customer.class);
	private final CommercialSubject commercialSubject = new CommercialSubjectImpl(NAME, customer);
	private final Subject subject = Mockito.mock(Subject.class);

	private final Condition condition = Mockito.mock(Condition.class);

	private final CommercialSubjectItem item = Mockito.mock(CommercialSubjectItem.class);

	@Before
	public final void setup() {
		Mockito.when(subject.id()).thenReturn(Optional.of(SUBJECT_ID));
		Mockito.when(condition.conditionType()).thenReturn(CONDITION_TYPE);
		Mockito.when(condition.subject()).thenReturn(subject);
		Mockito.when(item.subject()).thenReturn(subject);
	}

	@Test
	public final void assign() {
		final Collection<CommercialSubjectItem> results = items();
		Assert.assertEquals(0, results.size());
		commercialSubject.assign(subject, ITEM_NAME, true);

		Assert.assertEquals(1, results.size());
		Assert.assertTrue(results.stream().findFirst().isPresent());
		Assert.assertEquals(subject, results.stream().findFirst().get().subject());
		Assert.assertEquals(ITEM_NAME, results.stream().findFirst().get().name());
		Assert.assertTrue(results.stream().findFirst().get().mandatory());

	}

	@Test
	public final void assignExisting() {
		final CommercialSubjectItem item = new CommercialSubjectItemImpl(null, commercialSubject, subject, false);
		final Collection<CommercialSubjectItem> items = items();
		items.add(item);
		Assert.assertEquals(1, items.size());
		Assert.assertNull(item.name());
		Assert.assertFalse(item.mandatory());
		commercialSubject.assign(subject, ITEM_NAME, true);
		Assert.assertEquals(1, items.size());

		Assert.assertEquals(subject, item.subject());
		Assert.assertEquals(ITEM_NAME, item.name());
		Assert.assertTrue(item.mandatory());

	}

	@SuppressWarnings("unchecked")
	private Collection<CommercialSubjectItem> items() {
		return (Collection<CommercialSubjectItem>) ReflectionTestUtils.getField(commercialSubject, "items");
	}

	@Test
	public final void remove() {

		final Collection<CommercialSubjectItem> items = items();
		items.add(item);
		Assert.assertEquals(1, items.size());
		commercialSubject.remove(subject);
		Assert.assertTrue(items.isEmpty());
	}

	@Test
	public final void subjects() {
		final Collection<CommercialSubjectItem> items = items();
		items.add(item);
		Assert.assertEquals(1, commercialSubject.subjects().size());
		Assert.assertTrue(commercialSubject.subjects().stream().findFirst().isPresent());
		Assert.assertEquals(subject, commercialSubject.subjects().stream().findFirst().get());

	}

	@Test
	public final void commercialSubjectItems() {
		final Collection<CommercialSubjectItem> items = items();
		items.add(item);
		Assert.assertEquals(1, commercialSubject.commercialSubjectItems().size());
		Assert.assertTrue(commercialSubject.commercialSubjectItems().stream().findFirst().isPresent());
		Assert.assertEquals(item, commercialSubject.commercialSubjectItems().stream().findFirst().get());

	}

	@Test
	public final void commercialSubjectItem() {
		final Collection<CommercialSubjectItem> items = items();
		items.add(item);

		final Optional<CommercialSubjectItem> results = commercialSubject.commercialSubjectItem(subject);
		Assert.assertTrue(results.isPresent());
		Assert.assertEquals(item, results.get());
		Assert.assertFalse(commercialSubject.commercialSubjectItem(Mockito.mock(Subject.class)).isPresent());
	}

	@Test
	public final void conditionValues() {
		final Collection<CommercialSubjectItem> items = items();
		items.add(item);

		@SuppressWarnings("unchecked")
		final Entry<Condition, Collection<Object>> value = Mockito.mock(Entry.class);
		final Collection<Entry<Condition, Collection<Object>>> values = new ArrayList<>();
		values.add(value);
		Mockito.when(item.conditionValues()).thenReturn(values);

		Assert.assertEquals(values, commercialSubject.conditionValues(subject));

	}

	@Test
	public final void assignConditionValue() {
		final Collection<CommercialSubjectItem> items = items();
		items.add(item);

		commercialSubject.assign(condition, CONDITION_VALUE);

		Mockito.verify(item, Mockito.times(1)).assign(CONDITION_TYPE, CONDITION_VALUE);

	}

	@Test
	public final void removeConditionValue() {
		final Collection<CommercialSubjectItem> items = items();
		items.add(item);

		commercialSubject.remove(condition, CONDITION_VALUE);
		Mockito.verify(item, Mockito.times(1)).remove(CONDITION_TYPE, CONDITION_VALUE);

	}

	@Test
	public final void create() {
		Assert.assertEquals(NAME, commercialSubject.name());
		Assert.assertEquals(customer, commercialSubject.customer());
	}

	@Test
	public final void hash() {
		final CommercialSubject result = BeanUtils.instantiateClass(CommercialSubjectImpl.class);
		Assert.assertEquals(System.identityHashCode(result), result.hashCode());
		ReflectionTestUtils.setField(result, CUSTOMER_FIELD, customer);
		Assert.assertEquals(System.identityHashCode(result), result.hashCode());
		ReflectionTestUtils.setField(result, NAME_FIELD, NAME);

		Assert.assertEquals(customer.hashCode() + NAME.hashCode(), result.hashCode());

	}

	@Test
	public final void equals() {
		final CommercialSubject invalid = BeanUtils.instantiateClass(CommercialSubjectImpl.class);
		Assert.assertTrue(invalid.equals(invalid));
		Assert.assertFalse(invalid.equals(BeanUtils.instantiateClass(CommercialSubjectImpl.class)));

		Assert.assertTrue(commercialSubject.equals(new CommercialSubjectImpl(NAME, customer)));
		Assert.assertFalse(commercialSubject.equals(new CommercialSubjectImpl(ITEM_NAME, customer)));
		Assert.assertFalse(commercialSubject.equals(new CommercialSubjectImpl(NAME, Mockito.mock(Customer.class))));

		Assert.assertFalse(commercialSubject.equals(NAME));
	}

}
