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
		final Validator  validator = new MinMaxValidatorImpl();
		Assert.assertEquals 2, validator.parameters().length
		Assert.assertEquals 'min', validator.parameters()[0]
		Assert.assertEquals 'max', validator.parameters()[1]
	}
	
	@Test
	final void setMinAsString() {
		final Validator  validator = new MinMaxValidatorImpl();
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
		final Validator  validator = new MinMaxValidatorImpl();
		validator.setProperty('min', 10)
		validator.setProperty('max', 20)
		Assert.assertTrue validator.validate('15.15')
		Assert.assertFalse validator.validate('9.9')
		Assert.assertFalse validator.validate('20.1')
		Assert.assertFalse validator.validate('xx')
		
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
	

}
