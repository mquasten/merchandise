package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public class CommercialSubjectItemConditionTest {

	private static final String INPUT_VALUE = "inputValue";

	private static final long SUBJECT_ID = 4711L;

	private static final Long CONDITION_ID = 19680528L;

	private final CommercialSubjectItem commercialSubjectItem = Mockito.mock(CommercialSubjectItem.class);

	private final Condition condition = Mockito.mock(Condition.class);
	private final Condition otherCondition = Mockito.mock(Condition.class);

	private final Subject subject = Mockito.mock(Subject.class);

	private CommercialSubjectItemConditionImpl commercialSubjectItemCondition;

	private final InputValueImpl inputValue = Mockito.mock(InputValueImpl.class);

	@Before
	public void setup() {
		Mockito.when(inputValue.value()).thenReturn(Optional.of(INPUT_VALUE));
		Mockito.when(subject.id()).thenReturn(Optional.of(SUBJECT_ID));
		Mockito.when(condition.id()).thenReturn(Optional.of(CONDITION_ID));
		Mockito.when(condition.subject()).thenReturn(subject);
		Mockito.when(otherCondition.subject()).thenReturn(subject);
		Mockito.when(otherCondition.id()).thenReturn(Optional.of(CONDITION_ID));
		commercialSubjectItemCondition = new CommercialSubjectItemConditionImpl(commercialSubjectItem, condition);

	}

	@Test
	public final void create() {
		Assert.assertEquals(condition, commercialSubjectItemCondition.condition());
		Assert.assertEquals(commercialSubjectItem, commercialSubjectItemCondition.commercialSubjectItem());
		Assert.assertEquals(new UUID(SUBJECT_ID, CONDITION_ID).toString(), ReflectionTestUtils.getField(commercialSubjectItemCondition, "id"));

	}

	@Test
	public final void values() {
		final Collection<InputValueImpl> values = inputValues();
		values.add(inputValue);

		final Collection<String> results = commercialSubjectItemCondition.values();
		Assert.assertEquals(1, results.size());
		Assert.assertTrue(results.stream().findAny().isPresent());
		Assert.assertEquals(INPUT_VALUE, results.stream().findAny().get());

	}

	@SuppressWarnings("unchecked")
	private Collection<InputValueImpl> inputValues() {
		return (Collection<InputValueImpl>) ReflectionTestUtils.getField(commercialSubjectItemCondition, "inputValues");
	}

	@Test
	public final void assign() {
		final Collection<InputValueImpl> values = inputValues();
		Assert.assertTrue(values.isEmpty());
		commercialSubjectItemCondition.assign(INPUT_VALUE);
		Assert.assertEquals(1, values.size());
		Assert.assertTrue(values.stream().findAny().isPresent());
		Assert.assertTrue(values.stream().findFirst().get().value().isPresent());
		Assert.assertEquals(INPUT_VALUE, values.stream().findFirst().get().value().get());
	}

	@Test(expected = IllegalStateException.class)
	public final void assignSucks() {
		final Collection<InputValueImpl> values = inputValues();
		Assert.assertTrue(values.isEmpty());
		commercialSubjectItemCondition.assign(Mockito.mock(Number.class));
	}

	@Test
	public final void remove() {
		final Collection<InputValueImpl> values = inputValues();
		values.add(inputValue);
		Assert.assertEquals(1, values.size());
		commercialSubjectItemCondition.remove(INPUT_VALUE);
		Assert.assertTrue(values.isEmpty());

	}

	@Test
	public final void defaultConstructor() {
		Assert.assertTrue(BeanUtils.instantiateClass(CommercialSubjectItemConditionImpl.class) instanceof CommercialSubjectItemConditionImpl);
	}

	@Test
	public final void hash() {
		final CommercialSubjectItemConditionImpl invalid = BeanUtils.instantiateClass(CommercialSubjectItemConditionImpl.class);
		Assert.assertEquals(System.identityHashCode(invalid), invalid.hashCode());
		Assert.assertEquals(condition.hashCode() + commercialSubjectItem.hashCode(), commercialSubjectItemCondition.hashCode());

		ReflectionTestUtils.setField(commercialSubjectItemCondition, "condition", null);

		Assert.assertEquals(System.identityHashCode(commercialSubjectItemCondition), commercialSubjectItemCondition.hashCode());

	}

	@Test
	public final void equals() {
		Assert.assertTrue(commercialSubjectItemCondition.equals(new CommercialSubjectItemConditionImpl(commercialSubjectItem, condition)));
		Assert.assertFalse(commercialSubjectItemCondition.equals(new CommercialSubjectItemConditionImpl(Mockito.mock(CommercialSubjectItem.class), condition)));
		Assert.assertFalse(commercialSubjectItemCondition.equals(new CommercialSubjectItemConditionImpl(commercialSubjectItem, otherCondition)));

		CommercialSubjectItemConditionImpl invalid = BeanUtils.instantiateClass(CommercialSubjectItemConditionImpl.class);
		Assert.assertFalse(commercialSubjectItemCondition.equals(invalid));
		Assert.assertFalse(invalid.equals(commercialSubjectItemCondition));
		Assert.assertFalse(commercialSubjectItemCondition.equals(INPUT_VALUE));

	}
}
