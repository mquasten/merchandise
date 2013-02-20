package de.mq.merchandise.model.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.model.support.String2LegalFormConverter;

public class String2LegalFormConverterTest {
	
	
	
	@Test
	public final void converter() {
		final Converter<String,LegalForm> converter = new String2LegalFormConverter();
		Assert.assertEquals(LegalForm.GmbHCoKG, converter.convert(LegalForm.GmbHCoKG.name()));
	}

}
