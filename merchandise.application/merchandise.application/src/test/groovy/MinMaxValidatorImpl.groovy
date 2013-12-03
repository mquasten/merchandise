
class MinMaxValidatorImpl<T>  implements de.mq.merchandise.rule.Validator<T>{
	def Number min = Double.MIN_VALUE;
	def Number  max = Double.MAX_VALUE;

	final  String[] parameters() {
		return ['min' , 'max'];
	}

	final String resourceKey() {
		return 'MinMaxValidator.message';
	}

	final boolean validate(final T value){
		if(! (value instanceof Number)) {
			return false;
		}
		
		return (value.doubleValue()>= min) && (value.doubleValue() <= max );
	}
	
	void setMax(final String max) {
		this.max=Double.valueOf(max);
	}
	
	void setMin(final String min) {
		this.min=Double.valueOf(min);
	}
	
}
