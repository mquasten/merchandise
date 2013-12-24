package de.mq.merchandise.rule.support;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import de.mq.merchandise.rule.Converter;

import de.mq.merchandise.rule.ParameterNamesAware;
import de.mq.merchandise.rule.Validator;

public class StringToNumberValidatorAndConverterTest {

	private GroovyObject script;

	@Before
	public void setup() throws IOException, InstantiationException, IllegalAccessException {
		final ClassLoader parent = getClass().getClassLoader();
		try (final GroovyClassLoader loader = new GroovyClassLoader(parent)) {
			final File reader = new File("src/main/groovy/StringToNumberValidatorAndConverterImpl.groovy");
			final Class<?> clazz = loader.parseClass(reader);
			script = (GroovyObject) clazz.newInstance();

		}
	}
	
	
	@Test
	public final void validate() throws IOException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		final Validator<String> validator = (Validator<String>) script;
		Assert.assertFalse(validator.validate("dontLetMeGetMe"));
		Assert.assertTrue(validator.validate("3.14e0"));
	}
	
	@Test
	public final void convert() {
		@SuppressWarnings("unchecked")
		final Converter<String,Number> converter = (Converter<String,Number>) script;
		Assert.assertEquals(3.14d, converter.convert("3.14e0"));
	}
	
	@Test
	public final void setType() {
		Assert.assertEquals(Double.class, script.getProperty("type"));
		script.setProperty("type", Integer.class.getName() );
		Assert.assertEquals(Integer.class, script.getProperty("type"));
	}
	
	@Test
	public final void parameter() {
		@SuppressWarnings("unchecked")
		final ParameterNamesAware<String> parameterAware = (ParameterNamesAware<String>) script;
		final String[] results = parameterAware.parameters();
		Assert.assertEquals(1, results.length);
		Assert.assertEquals("type", results[0]);
		
	}
	

}
