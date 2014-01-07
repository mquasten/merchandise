package de.mq.merchandise.rule.support


import groovy.lang.MetaClass;

import org.mockito.internal.matchers.InstanceOf;
import org.springframework.context.MessageSource


class MinMaxValidatorImpl<T>  implements  de.mq.merchandise.rule.Validator<T>  {
	def Number min = Double.MIN_VALUE+1;
	def Number  max = Double.MAX_VALUE-1;
	static final String RESOURCE_KEY = 'MinMaxValidator.message'

	static final String DEFAULT_MESSAGE = 'Number required [%s ... %s]'

	@Override
	final  String[] parameters() {

		return ['min' , 'max'].toArray()
	}





	@Override
	final boolean validate(final T  value){
		try {
			final Number numberValue= number(value)
			return (numberValue.doubleValue() >= min.doubleValue()) && (numberValue.doubleValue() <= max.doubleValue() )
		} catch (final NumberFormatException ne){
			return false;
		}
	}

	private Number number(final T value) {
		if (value instanceof Number ) {
			return value;
		}
		return Double.valueOf(value);
	}


	@Override
	final String message(final MessageSource messagesource, final Locale locale) {
		return messagesource.getMessage(RESOURCE_KEY, [min, max].toArray(), String.format(DEFAULT_MESSAGE, min, max),  locale,)
	}


	final void setMax(final String max) {
		this.max=Double.valueOf(max)
	} 

	final void setMin(final String min) {
		this.min=Double.valueOf(min)
	} 





	@Override
	final T[] ok() {
		return [ String.valueOf(min), String.valueOf(( min + max) / 2) ,  String.valueOf(max), min, ( min + max) / 2, max ].toArray();
	}





	@Override
	final T[] bad() {
		return [String.valueOf(min-0.5), String.valueOf(max+0.5), 'For my name was Elisa Day', min-0.5, max+0.5 ];
	}





	
}
