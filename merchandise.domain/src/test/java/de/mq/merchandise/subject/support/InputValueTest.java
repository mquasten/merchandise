package de.mq.merchandise.subject.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

public class InputValueTest {
	
	private static final String STRING_VALUE_FIELD = "stringValue";
	private static final String STRING_VALUE = "47.11";
	private static final long LONG_VALUE = 4711L;
	private static final double DOUBLE_VALUE = 47.11;

	@Test
	public final void createDouble() {
	
		final InputValue inputValue = new InputValueImpl(DOUBLE_VALUE);
		Assert.assertTrue(inputValue.value().isPresent());
		Assert.assertEquals(DOUBLE_VALUE, inputValue.value().get());
	}
	
	@Test
	public final void createLong() {
		final InputValue inputValue = new InputValueImpl(LONG_VALUE);
		Assert.assertTrue(inputValue.value().isPresent());
		Assert.assertEquals(LONG_VALUE, inputValue.value().get());
	}
	
	@Test
	public final void createString() {
		final InputValue inputValue = new InputValueImpl(STRING_VALUE);
		Assert.assertTrue(inputValue.value().isPresent());
		Assert.assertEquals(STRING_VALUE, inputValue.value().get());
	}
	@Test
	public final void emptyValue() {
		final InputValue inputValue = new InputValueImpl(STRING_VALUE);
		ReflectionTestUtils.setField(inputValue, STRING_VALUE_FIELD, null);
		Assert.assertFalse(inputValue.value().isPresent());
	}
	
	@Test
	public final void hash() {
		final InputValue inputValue = new InputValueImpl(STRING_VALUE);
		Assert.assertEquals(STRING_VALUE.hashCode(), inputValue.hashCode());
		ReflectionTestUtils.setField(inputValue, STRING_VALUE_FIELD, null);
		Assert.assertEquals(System.identityHashCode(inputValue), inputValue.hashCode());
	}
	
	@Test
	public final void equals() {
		final InputValue inputValue = new InputValueImpl(STRING_VALUE);
		final InputValue empty = new InputValueImpl(STRING_VALUE);
		ReflectionTestUtils.setField(empty, STRING_VALUE_FIELD, null);
		Assert.assertTrue(inputValue.equals(new InputValueImpl(STRING_VALUE)));
		Assert.assertFalse(inputValue.equals(new InputValueImpl(DOUBLE_VALUE)));
		Assert.assertFalse(inputValue.equals(empty));
		Assert.assertFalse(empty.equals(inputValue));
		Assert.assertFalse(inputValue.equals(STRING_VALUE));
		
	}
	
	@Test
	public final void defaultConstructor() {
		
		Assert.assertTrue(  BeanUtils.instantiateClass(InputValueImpl.class) instanceof InputValue);
	}
	

}
