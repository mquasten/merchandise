package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class String2ConditionTypeTest {
	
	private Converter<String, ConditionType> converter = new String2ConditionType(); 
	
	@Test
	public final void convert() {
		Assert.assertEquals(ConditionType.PricePerUnit, converter.convert(ConditionType.PricePerUnit.name()));
	}
	
	@Test
	public final void convertNull() {
		Assert.assertNull(converter.convert(null));
	}

}
