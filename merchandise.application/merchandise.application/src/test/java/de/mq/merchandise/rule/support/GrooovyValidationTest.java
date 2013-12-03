package de.mq.merchandise.rule.support;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import de.mq.merchandise.rule.Validator;

 


public class GrooovyValidationTest {
	
	String  VALIDATOR = "class MinMaxValidator  implements de.mq.merchandise.rule.Validator<Number>{" +
		"def Number min; " +
		"def Number max; " +
		
		"def  String[] parameters() {" +
		    "['min' , 'max'];"+ 
			
		"};"+

		"def String resourceKey() {"+
		     "'MinMaxValidator.message';" +
		"};"+
		     
		
	"def boolean validate(final Number value){"+
		  "(value.doubleValue() >= min) && (value.doubleValue() <= max);"+
	      
	"};"+
		
		
	"}" ;
	
	
	@Test
	public final void testValidate() throws InstantiationException, IllegalAccessException, IOException {
		final ClassLoader parent = getClass().getClassLoader();
		try (final GroovyClassLoader loader = new GroovyClassLoader(parent)){
		//Class<?> clazz = loader.parseClass(VALIDATOR);
			
		final File reader = new File("src/test/groovy/MinMaxValidatorImpl.groovy");
		Class<?> clazz = loader.parseClass(reader);
		GroovyObject aScript = (GroovyObject) clazz.newInstance();
		@SuppressWarnings("unchecked")
		final Validator<Object> validator = (Validator<Object>) aScript;
		final String[] params = validator.parameters();
	
		Assert.assertEquals(2, params.length);
		Assert.assertEquals("min", params[0]);
		Assert.assertEquals("max", params[1]);
		
		Assert.assertEquals("MinMaxValidator.message", validator.resourceKey());
	
		aScript.setProperty("min", "1e1");
		aScript.setProperty("max", "2e1");
		
		Assert.assertEquals(10d, aScript.getProperty("min"));
		Assert.assertEquals(20d, aScript.getProperty("max"));
		
		
		Assert.assertTrue(validator.validate(15L));
		Assert.assertFalse(validator.validate(9L));
		Assert.assertFalse(validator.validate(21L));
		
		Assert.assertFalse(validator.validate("kinkyKylie"));
	}
	
	
	}

}
