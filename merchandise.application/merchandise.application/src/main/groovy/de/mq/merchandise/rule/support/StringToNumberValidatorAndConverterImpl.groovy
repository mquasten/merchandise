package de.mq.merchandise.rule.support
import java.lang.invoke.MethodHandleImpl.BindCaller.T
import java.lang.reflect.Constructor
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;


class StringToNumberValidatorAndConverterImpl<T> implements de.mq.merchandise.rule.Validator<T> , de.mq.merchandise.rule.Converter<T, Number> {

	static final String RESOURCE_KEY = 'StringToNumberValidatorAndConverter.message'

	static final String DEFAULT_MESSAGE = 'Not a valid number for %s'

	def   Class<Number> type = Double.class;
	
	@Override
	final String[] parameters() {
		return ['type'].toArray();
	}

	@Override
	final boolean validate(final T value) {
		try {
			convert(value);
			return true;
		} catch (final NumberFormatException ex){
		return false;
		}
		
	}

	@Override
	final String message(final MessageSource messagesource, final Locale locale) {
		return messagesource.getMessage(RESOURCE_KEY,[type.getSimpleName()].toArray() ,String.format(DEFAULT_MESSAGE, type.getSimpleName()) , locale);
	}

	@Override
	final Number convert(final T value) {
		if (value instanceof Number ) {
			return type.newInstance(value.toString());
		}
		return type.newInstance(value);
		
	}
	
	@Override
	final T[] ok() {
		if( validate('0.5') ) {
			return ['-1.5','0','1.5', -1.5, 0, 1.5].toArray();
		}
		return ['-1','0','1', -1, 0, 1].toArray();
	}
	
	@Override
	final T[] bad() {
		if( ! validate('0.5') ) {
			return ['ogin ateed', '-1.5', '1.5', -1.5, 1.5]
		} else {
			return ['ogin ateed']
		}
		
	
	}

}
