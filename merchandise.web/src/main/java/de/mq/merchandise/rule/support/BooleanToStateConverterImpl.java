package de.mq.merchandise.rule.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.support.StateBuilderImpl;

@Component
public class BooleanToStateConverterImpl implements Converter<Boolean, State> {

	@Override
	public final State convert(final Boolean active) {
		
		
		return  new StateBuilderImpl().forState(active).build();
		
	}

}
