package de.mq.merchandise.rule.support;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.opportunity.support.DocumentService;
import de.mq.merchandise.util.Parameter;

@Configuration
public class SourceFactoryImpl {
	
	private DocumentService documentService;
	
	@Autowired
	public SourceFactoryImpl(final DocumentService documentService){
		this.documentService=documentService;
	}
	
	
	@Bean(name="groovyObject")
	@Scope("prototype")
	@SuppressWarnings("unchecked")
	public final  <T extends GroovyObject> T create(final Long sourceId, final Parameter<?>... parameters) {
		try {
			final GroovyObject result =  doCreate(sourceId);
			assignParameters(result, parameters);
			return (T) result;
		} catch (final InstantiationException | IllegalAccessException | IOException ex) {
			throw new IllegalStateException("Unaable to create Resource",  ex);
		}
	}
	
	


	
	private GroovyObject doCreate(final Long sourceId) throws InstantiationException, IllegalAccessException, IOException {
		final byte[] source = documentService.document(sourceId);
		final ClassLoader parent = getClass().getClassLoader();
		try (final GroovyClassLoader loader = new GroovyClassLoader(parent)){
			final Class<?> clazz = loader.parseClass(new String(source));
			return  (GroovyObject) clazz.newInstance();
		}
	}


	private void assignParameters(final GroovyObject aScript, final Parameter<?>... parameters) {
		for(final Parameter<?> parameter : parameters){
			aScript.setProperty(parameter.name(), parameter.value());
		}
	}
	
	

}
