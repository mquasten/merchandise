package de.mq.merchandise.order.support;

import java.util.Currency;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.order.Money;

public class String2MoneyConverterTest {
	
	final Converter<String, Money> converter = new String2MoneyConverter();
	
	@Test
	public final void convert() {
		Assert.assertEquals(new MoneyImpl(47.11, Currency.getInstance("EUR")), converter.convert("47.11 EUR"));
	}
	
	@Test
	public final void convertNull() {
		Assert.assertNull(converter.convert(null));
	}
	
	@Test
	public final void convertEmpty() {
		Assert.assertNull(converter.convert(" "));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void missingCurrencyCode() {
		converter.convert("47.11");
	}

}
