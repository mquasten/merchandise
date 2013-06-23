package de.mq.merchandise.util;

import org.junit.Assert;
import org.junit.Test;

public class ParameterTest {
	
	private static final String VALUE = "Kylie";
	private static final String NAME = "artist";

	@Test
	public final void create() {
		final Parameter<String> parameter = new ParameterImpl<String>(NAME, VALUE);
		Assert.assertEquals(NAME, parameter.name());
		Assert.assertEquals(VALUE, parameter.value());
	}
	
	@Test
	public final void parameterToString(){
		final Parameter<String> parameter = new ParameterImpl<String>(NAME, VALUE);
		Assert.assertEquals(String.format("%s=[%s]", NAME, VALUE), parameter.toString());
	}
	
	@Test
	public final void parameterHashCode() {
		final Parameter<String> parameter = new ParameterImpl<String>(NAME, VALUE);
		Assert.assertEquals(NAME.hashCode(), parameter.hashCode());
	}
	
	@Test
	public final void equals() {
		Assert.assertFalse(new ParameterImpl<String>(NAME, VALUE).equals(VALUE));
		Assert.assertTrue(new ParameterImpl<String>(NAME, VALUE).equals(new ParameterImpl<String>(NAME, "dontLetmeGetMe")));
	}

}
