package de.mq.merchandise.rule.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.State;

public class BooleanToStateConverterTest {
	
	private Converter<Boolean,State> converter = new BooleanToStateConverterImpl();
	
	@Test
	public final void convert() {
		Assert.assertTrue(converter.convert(true).isActive());
		Assert.assertFalse(converter.convert(false).isActive());
	}

}
