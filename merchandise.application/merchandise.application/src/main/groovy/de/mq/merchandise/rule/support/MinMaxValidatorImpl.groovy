package de.mq.merchandise.rule.support


import org.springframework.context.MessageSource


class MinMaxValidatorImpl  implements  de.mq.merchandise.rule.Validator<String>  {
	def Number min = Double.MIN_VALUE
	def Number  max = Double.MAX_VALUE
	static final String RESOURCE_KEY = 'MinMaxValidator.message'

	static final String DEFAULT_MESSAGE = 'Number required [%s ... %s]'

	@Override
	final  String[] parameters() {

		return ['min' , 'max'].toArray()
	}





	@Override
	final boolean validate(final String  value){
		try {
			final Number numberValue= Double.valueOf(value);
			return (numberValue.doubleValue() >= min.doubleValue()) && (numberValue.doubleValue() <= max.doubleValue() )
		} catch (final NumberFormatException ne){
			return false;
		}
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
}
