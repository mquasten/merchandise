package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.opportunity.support.Opportunity.Kind;

public class String2OpportunityKindConverterTest {
	
	private Converter<String,Kind> string2OpportunityKindConverter = new String2OpportunityKindConverter();
	
	@Test
	public final void convert() {
	   for(final Kind kind : Kind.values()){
	      Assert.assertEquals(kind, string2OpportunityKindConverter.convert(kind.name()));
	   }
	}
	
	@Test
	public final void convertNull() {
		Assert.assertNull(string2OpportunityKindConverter.convert(null));
	}

}
