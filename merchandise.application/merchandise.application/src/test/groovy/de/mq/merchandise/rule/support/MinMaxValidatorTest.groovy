package de.mq.merchandise.rule.support
import de.mq.merchandise.rule.Validator
import  org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource
class MinMaxValidatorTest {

	static final String DOUBLE_STRING = '47.11e0'

	static final String MESSAGE = 'dontletMeGetMe'

	
	
	@Test
	final void parameters() {
		final Validator  validator = new MinMaxValidatorImpl<>();
		Assert.assertEquals 2, validator.parameters().length
		Assert.assertEquals 'min', validator.parameters()[0]
		Assert.assertEquals 'max', validator.parameters()[1]
	}
	
	@Test
	final void setMinAsString() {
		final Validator validator = new MinMaxValidatorImpl();
		validator.setProperty('min', DOUBLE_STRING)
		Assert.assertEquals Double.valueOf('47.11e0'), validator.getProperties().get('min') , 0;
	}
	
	@Test
	final void setMaxAsString() {
		final Validator  validator = new MinMaxValidatorImpl();
		validator.setProperty('max', DOUBLE_STRING)
		Assert.assertEquals Double.valueOf('47.11e0'), validator.getProperties().get('max') , 0;
	}
	
	@Test
	final void validate() {
		final Validator<String>  validator = new MinMaxValidatorImpl<String>();
		validator.setProperty('min', 10)
		validator.setProperty('max', 20)
		Assert.assertTrue validator.validate('15.15')
		Assert.assertFalse validator.validate('9.9')
		Assert.assertFalse validator.validate('20.1')
		Assert.assertFalse validator.validate('xx')
		
	}
	
	@Test
	final void validateNumber() {
		final Validator<Number>  validator = new MinMaxValidatorImpl<Number>();
		validator.setProperty('min', 10)
		validator.setProperty('max', 20)
		Assert.assertTrue validator.validate(15.15)
		Assert.assertFalse validator.validate(9.9)
		Assert.assertFalse validator.validate(20.1)
	}
	
	@Test
	final void message() {
		final Validator  validator = new MinMaxValidatorImpl();
		validator.setProperty('min', 10)
		validator.setProperty('max', 20)
		
		final MessageSource messageSource = Mockito.mock(MessageSource.class);
		Mockito.when(messageSource.getMessage(MinMaxValidatorImpl.RESOURCE_KEY, [10D, 20D].toArray() ,String.format(MinMaxValidatorImpl.DEFAULT_MESSAGE, 10D, 20D) , Locale.GERMAN )).thenReturn(MESSAGE)
		
		Assert.assertEquals MESSAGE, validator.message(messageSource, Locale.GERMAN)
	}
	
	@Test
	final void ok() {
		final Validator  validator = new MinMaxValidatorImpl();
		validator.setProperty('min', 1)
		validator.setProperty('max', 2)
		int counter=0;
		for(final String value :validator.ok()) {
			Assert.assertTrue Double.valueOf(value)==1D||Double.valueOf(value)==2D||Double.valueOf(value)==1.5D
			Assert.assertTrue validator.validate(value)
			counter++;
		}
		Assert.assertEquals 6, counter 
	}
	
	@Test
	final void bad() {
		final Validator  validator = new MinMaxValidatorImpl();
		validator.setProperty('min', 1)
		validator.setProperty('max', 2)
		int counter=0;
		for(final String value :validator.bad()) {
			if( isNumber(value) ) {
				Assert.assertTrue(Double.valueOf(value) < 1 || Double.valueOf(value) > 2)
			}else {
			     Assert.assertEquals 'For my name was Elisa Day',  value
			}
			
			Assert.assertFalse  validator.validate(value)
			counter++;
		}
		Assert.assertEquals 5, counter
	
	}

	private boolean isNumber(String value) {
		try {
			Double.valueOf(value)
			return true;
		} catch(final NumberFormatException ne){
			return false;
		}
	}
	

}
