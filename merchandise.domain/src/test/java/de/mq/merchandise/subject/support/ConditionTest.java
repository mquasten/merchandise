package de.mq.merchandise.subject.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public class ConditionTest {

	private static final String OTHER_CONDITION_TYPE = "other";
	private Subject subject = Mockito.mock(Subject.class);

	private static final String CONDITION_TYPE = "date";

	@Test
	public final void create() {
		final Condition condition = new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String);

		Assert.assertEquals(subject, condition.subject());
		Assert.assertEquals(CONDITION_TYPE, condition.conditionType());
		Assert.assertEquals(ConditionDataType.String, condition.conditionDataType());
	}



	

	

	@Test
	public final void hash() {
		final Condition condition = new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String);
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
		final Condition condition = new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String);
		ReflectionTestUtils.setField(condition, "subject", null);
		Assert.assertFalse(condition.equals(new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String)));
		Assert.assertFalse(new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String).equals(condition));
		Assert.assertTrue(condition.equals(condition));
		Assert.assertFalse(new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String).equals(CONDITION_TYPE));
		Assert.assertTrue(new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String).equals(new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String)));
		Assert.assertFalse(new ConditionImpl(subject, OTHER_CONDITION_TYPE, ConditionDataType.String).equals(new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String)));
		Assert.assertFalse(new ConditionImpl(Mockito.mock(Subject.class), CONDITION_TYPE, ConditionDataType.String).equals(new ConditionImpl(subject, CONDITION_TYPE, ConditionDataType.String)));
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertTrue(BeanUtils.instantiateClass(ConditionImpl.class) instanceof Condition);
	}

}
