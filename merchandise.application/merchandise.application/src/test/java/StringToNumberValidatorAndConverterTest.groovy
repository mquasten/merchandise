
import StringToNumberValidatorAndConverterImpl;

import  org.junit.Assert;



import de.mq.merchandise.rule.Converter
import de.mq.merchandise.rule.Validator
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;


class StringToNumberValidatorAndConverterTest {

	static final String MESSAGE = 'Kylie is nice and hot'
	

	@Test
	public void parameters() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		Assert.assertEquals(1, validator.parameters().length);
		Assert.assertEquals('type',  validator.parameters()[0]);
	
	}
	
	
	@Test
	public final void validate() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		validator.setProperty('type', Integer.class);
		
		Assert.assertTrue(validator.validate('2'));
		Assert.assertFalse(validator.validate('2.2'));
	}
	
	@Test
	public final void convert() {
		final Converter<String>  converter = new StringToNumberValidatorAndConverterImpl();
		converter.setProperty('type', Integer.class);
		Assert.assertEquals(2, converter.convert('2'));
		println '++++++++++++++++++++++++++++++++++++'
		
		
	}
	
	
	
	@Test
	public final void setType2() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		validator.setProperty('type', 'java.lang.Integer')
		Assert.assertTrue(validator.validate('2'));
		Assert.assertFalse(validator.validate('2.2'));
	}
	
	@Test
	public final void getType() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		Assert.assertEquals(Double.class, validator.getType());
	}

	@Test
	public final void message() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		MessageSource messageSource = Mockito.mock(MessageSource.class);
	    Mockito.when(messageSource.getMessage(StringToNumberValidatorAndConverterImpl.RESOURCE_KEY, ['Double'].toArray() ,String.format(StringToNumberValidatorAndConverterImpl.DEFAULT_MESSAGE, 'Double') , Locale.GERMAN )).thenReturn(MESSAGE)
		Assert.assertEquals(MESSAGE, validator.message(messageSource, Locale.GERMAN));
		
	}

}
