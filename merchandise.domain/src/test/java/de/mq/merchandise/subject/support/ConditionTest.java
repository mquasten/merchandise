package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public class ConditionTest {

	private static final String OTHER_CONDITION_TYPE = "other";

	private static final String CONDITION_VALUE_PUBLIC = "public";

	private static final String CONDITION_VALUE_PRIVATE = "private";

	private Subject subject = Mockito.mock(Subject.class);

	private static final String CONDITION_TYPE = "date";

	@Test
	public final void create() {
		final Condition<String> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);

		Assert.assertEquals(subject, condition.subject());
		Assert.assertEquals(CONDITION_TYPE, condition.conditionType());
		Assert.assertEquals(ConditionDataType.String, condition.conditionDataType());
	}

	@Test
	public final void add() {
		final Condition<String> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);

		condition.add(CONDITION_VALUE_PRIVATE);

		Collection<InputValue> results = values(condition);
		Assert.assertEquals(1, results.size());
		Assert.assertTrue(results.stream().findFirst().get().value().isPresent());
	}

	@SuppressWarnings("unchecked")
	private Collection<InputValue> values(final Condition<String> condition) {
		return (Collection<InputValue>) ReflectionTestUtils.getField(condition, "values");
	}

	@Test(expected = IllegalStateException.class)
	public final void addSucks() {
		final Condition<String> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.IntegralNumber);
		condition.add(CONDITION_VALUE_PRIVATE);
	}

	@Test
	public final void remove() {
		final Condition<String> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);
		final Collection<InputValue> results = values(condition);
		final InputValue inputValue = Mockito.mock(InputValue.class);
		Mockito.when(inputValue.value()).thenReturn(Optional.of(CONDITION_VALUE_PRIVATE));
		results.add(inputValue);
		Assert.assertEquals(1, results.size());
		condition.remove(CONDITION_VALUE_PRIVATE);
		Assert.assertEquals(0, results.size());
	}

	@Test
	public final void removeNotAware() {
		final Condition<String> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);
		final Collection<InputValue> results = values(condition);
		final InputValue inputValue = Mockito.mock(InputValue.class);
		Mockito.when(inputValue.value()).thenReturn(Optional.of(CONDITION_VALUE_PRIVATE));
		results.add(inputValue);
		Assert.assertEquals(1, results.size());
		condition.remove(CONDITION_VALUE_PUBLIC);
		Assert.assertEquals(1, results.size());
	}

	@Test
	public final void clear() {
		final Condition<String> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);
		final Collection<InputValue> results = values(condition);
		results.add(Mockito.mock(InputValueImpl.class));
		Assert.assertEquals(1, results.size());
		condition.clear();
		Assert.assertEquals(0, results.size());

	}

	@Test
	public final void values() {
		final Condition<String> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);
		final Collection<InputValue> results = values(condition);
		final InputValue inputValue = Mockito.mock(InputValue.class);
		Mockito.when(inputValue.value()).thenReturn(Optional.of(CONDITION_VALUE_PRIVATE));
		results.add(inputValue);

		final Collection<String> values = condition.values();
		Assert.assertEquals(1, values.size());

		Assert.assertEquals(CONDITION_VALUE_PRIVATE, values.stream().findFirst().get());

	}

	@Test
	public final void hash() {
		final Condition<?> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);
		ReflectionTestUtils.setField(condition, "conditionType", null);
		Assert.assertEquals(System.identityHashCode(condition), condition.hashCode());
		ReflectionTestUtils.setField(condition, "conditionType", CONDITION_TYPE);
		ReflectionTestUtils.setField(condition, "subject", null);
		Assert.assertEquals(System.identityHashCode(condition), condition.hashCode());
		ReflectionTestUtils.setField(condition, "subject", subject);
		Assert.assertEquals(CONDITION_TYPE.hashCode() + subject.hashCode(), condition.hashCode());
	}

	@Test
	public final void equals() {
		final Condition<?> condition = new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String);
		ReflectionTestUtils.setField(condition, "subject", null);
		Assert.assertFalse(condition.equals(new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String)));
		Assert.assertFalse(new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String).equals(condition));
		Assert.assertTrue(condition.equals(condition));
		Assert.assertFalse(new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String).equals(CONDITION_TYPE));
		Assert.assertTrue(new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String).equals(new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String)));
		Assert.assertFalse(new ConditionImpl<>(subject, OTHER_CONDITION_TYPE, ConditionDataType.String).equals(new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String)));
		Assert.assertFalse(new ConditionImpl<>(Mockito.mock(Subject.class), CONDITION_TYPE, ConditionDataType.String).equals(new ConditionImpl<>(subject, CONDITION_TYPE, ConditionDataType.String)));
	}

}
