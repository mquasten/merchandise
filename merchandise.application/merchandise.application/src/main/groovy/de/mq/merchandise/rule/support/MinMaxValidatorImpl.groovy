package de.mq.merchandise.rule.support
import org.springframework.context.MessageSource;


class MinMaxValidatorImpl<T>  implements de.mq.merchandise.rule.Validator<T> {
	def Number min = Double.MIN_VALUE;
	def Number  max = Double.MAX_VALUE;
	static final String RESOURCE_KEY = 'MinMaxValidator.message';
	
	static final String DEFAULT_MESSAGE = 'Number required [%s ... %s]';

	@Override
	final  String[] parameters() {
		return ['min' , 'max'];
	}

	

	@Override
	final boolean validate(final T value){
		if(! (value instanceof Number)) {
			return false;
		}
		
		return (value.doubleValue()>= min) && (value.doubleValue() <= max );
	}
	
	@Override
	final String message(final MessageSource messagesource, final Locale locale) {
		return messagesource.getMessage(RESOURCE_KEY, [min,max].toArray(), String.format(DEFAULT_MESSAGE, min, max),  locale,);
	}
	
	
	final void setMax(final String max) {
		this.max=Double.valueOf(max);
	}
	
	final void setMin(final String min) {
		this.min=Double.valueOf(min);
	}


	
	
}
