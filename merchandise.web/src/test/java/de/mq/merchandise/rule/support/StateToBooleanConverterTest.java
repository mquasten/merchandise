package de.mq.merchandise.rule.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.State;

public class StateToBooleanConverterTest {
	
	 private final Converter<State,Boolean>  converter = new StateToBooleanConverterImpl();
	 private final State state = Mockito.mock(State.class);
	 
	 @Test
	 public final void convert() {
		Mockito.when(state.isActive()) .thenReturn(true);
		Assert.assertTrue(converter.convert(state));
		Mockito.when(state.isActive()).thenReturn(false);
		Assert.assertFalse(converter.convert(state));
	 }

}
