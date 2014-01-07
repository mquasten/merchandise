package de.mq.merchandise.rule.support


import java.util.Collections.CheckedMap;

import  org.junit.Assert;



import de.mq.merchandise.rule.Converter
import de.mq.merchandise.rule.Validator
import de.mq.merchandise.rule.support.StringToNumberValidatorAndConverterImpl;

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
		
		Assert.assertTrue(validator.validate(2));
		Assert.assertFalse(validator.validate(2.2e10));
	}
	
	@Test
	public final void convert() {
		final Converter<String>  converter = new StringToNumberValidatorAndConverterImpl();
		converter.setProperty('type', Integer.class);
		Assert.assertEquals(2, converter.convert('2'));
		
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
	
	@Test
	public final void okFloat() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		validator.setProperty('type', 'java.lang.Float')
		int counter=0;
		for(final String value : validator.ok()){
			Assert.assertTrue Double.valueOf(value)==-1.5D || Double.valueOf(value)==1.5D ||  Double.valueOf(value)==0D
			Assert.assertTrue validator.validate(value)
			counter++;
		}
		Assert.assertEquals 6 , counter;
	}
	
	@Test
	public final void okInt() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		validator.setProperty('type', 'java.lang.Integer')
		int counter=0;
		for(final String value : validator.ok()){
				Assert.assertTrue Integer.valueOf(value)==-1 || Integer.valueOf(value)==1 ||  Integer.valueOf(value)==0
				Assert.assertTrue validator.validate(value)
				counter++;
		}
		Assert.assertEquals 6 , counter;
	}
	
	@Test
	public final void badFloat() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		validator.setProperty('type', 'java.lang.Float')
		int counter=0;
		for(final String value : validator.bad()){
			Assert.assertEquals 'ogin ateed',  value
			counter++;
		}
		Assert.assertEquals 1 , counter;
	}
	
	@Test
	public final void badInt() {
		final Validator<Number>  validator = new StringToNumberValidatorAndConverterImpl();
		validator.setProperty('type', 'java.lang.Integer')
		int counter=0;
		for(final String value : validator.bad()){
			
			if( isNumber(value) ) {
				Assert.assertTrue Double.valueOf(value)==-1.5D || Double.valueOf(value)==1.5D 
			}else {
				Assert.assertEquals 'ogin ateed',  value
			}
			counter++;
			
		}
		Assert.assertEquals 5 , counter;
	}
	
	private boolean isNumber(final String value) {
		try {
			Double.valueOf(value)
			return true;
		} catch(final NumberFormatException ne){
			return false;
		}
	}

}
