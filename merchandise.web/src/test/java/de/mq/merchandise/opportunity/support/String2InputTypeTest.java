package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.opportunity.support.Condition.InputType;


public class String2InputTypeTest {
	
	private final Converter<String, InputType>  converter = new String2InputType(); 
	
	@Test
	public final void toEnum() {
		Assert.assertEquals(InputType.User, converter.convert(InputType.User.name()));
	}
	
	@Test
	public final void toEnumNull() {
		Assert.assertNull(converter.convert(null));
	}

}
