package de.mq.merchandise.order.support;

import java.util.Currency;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import de.mq.merchandise.order.Money;

public class String2MoneyConverter implements Converter<String,Money> {

	@Override
	public Money convert(final String source) {
		if( source == null ){
			return null;
		}
		if(  !StringUtils.hasText(source) ) {
			return null;
		}
		final String[] values = source.split("[ \t]+");
		if( values.length != 2){
			throw new IllegalArgumentException("Wrong value " + source + ". Amount and Currency should be given, separated by space, example: 47.11 EUR");
		}
		final Currency currency = Currency.getInstance(values[1]);
		final Double amount = Double.valueOf(values[0]);
		return new MoneyImpl(amount, currency);
	}

}
