package de.mq.merchandise.rule.support;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import junit.framework.Assert;


import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.rule.Validator;

public class MinMaxValidatorValidationTest {
	
	private static final String ERROR_MESSAGE = "Eine Fehlermeldung";

	@Test
	public final void testValidate() throws InstantiationException, IllegalAccessException, IOException {
		final ClassLoader parent = getClass().getClassLoader();
		try (final GroovyClassLoader loader = new GroovyClassLoader(parent)){
		//Class<?> clazz = loader.parseClass(VALIDATOR);
			
		final File reader = new File("src/test/groovy/MinMaxValidatorImpl.groovy");
		final Class<?> clazz = loader.parseClass(reader);
		GroovyObject aScript = (GroovyObject) clazz.newInstance();
		
		Assert.assertTrue(aScript instanceof Validator );
		@SuppressWarnings("unchecked")
		final Validator<Object> validator = (Validator<Object>) aScript;
		final String[] params = validator.parameters();
	
		Assert.assertEquals(2, params.length);
		Assert.assertEquals("min", params[0]);
		Assert.assertEquals("max", params[1]);
	
		aScript.setProperty("min", "1e1");
		aScript.setProperty("max", "2e1");
		
		Assert.assertEquals(10d, aScript.getProperty("min"));
		Assert.assertEquals(20d, aScript.getProperty("max"));
		
		
		Assert.assertTrue(validator.validate(15L));
		Assert.assertFalse(validator.validate(9L));
		Assert.assertFalse(validator.validate(21L));
		
		Assert.assertFalse(validator.validate("kinkyKylie"));
		
		final MessageSource messagesource = Mockito.mock(MessageSource.class);
		Locale locale = Locale.GERMAN;
		
		Mockito.when(messagesource.getMessage( (String) ReflectionTestUtils.getField(aScript, "RESOURCE_KEY"), new Number[]{10d, 20d}, String.format((String) ReflectionTestUtils.getField(aScript, "DEFAULT_MESSAGE"), 10d, 20d), locale )).thenReturn(ERROR_MESSAGE);
		Assert.assertEquals(ERROR_MESSAGE, validator.message(messagesource, locale));
	
		
	}
	
	
	}

}
