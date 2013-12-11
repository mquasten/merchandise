package de.mq.merchandise.rule.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.customer.State;

@Component
public class StateToBooleanConverterImpl implements Converter<State,Boolean> {

	@Override
	public Boolean convert(final State state) {
		return state.isActive();
	}

}
