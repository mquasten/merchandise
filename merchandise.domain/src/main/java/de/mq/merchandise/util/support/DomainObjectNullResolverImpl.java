package de.mq.merchandise.util.support;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
public class DomainObjectNullResolverImpl {
	
	
	private final String PACKAGE_SCAN = "de.mq.merchandise";

	private final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(new PathMatchingResourcePatternResolver());
	
	private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	
	final Collection<Class<?>> entities()  {
		 
		

	    final Collection<Class<?>> results = new HashSet<>();
	    final Resource[] resources = resources();
	    for (Resource resource : resources) {
	       
	        final Class<?> clazz = entityClass(metadataReaderFactory, resource);
	       
	       
	        if( ! clazz.isAnnotationPresent(Entity.class)&&! clazz.isAnnotationPresent(Embeddable.class) ){
	        	continue;
	        }
	      
	        if( Modifier.isAbstract( clazz.getModifiers() )){
	        	continue;
	        }
	      results.add(clazz);
	    
	    }
		return results;

	}

	private Class<?> entityClass(final MetadataReaderFactory metadataReaderFactory, Resource resource)  {
		try {
			return Class.forName(metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName());
		} catch (final IOException   ex ) {
			throw new BeanDefinitionStoreException("Unable to read meta data for Entity:"  ,  ex);
		} catch(final ClassNotFoundException ex) {
			throw new BeanDefinitionStoreException("Unable to load EntityClass", ex);
		}
	}

	private Resource[] resources()  {
		try {
			
			return resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
			                           ClassUtils.convertClassNameToResourcePath(PACKAGE_SCAN) + "/" + "**/*.class");
		} catch (IOException e) {
			throw new BeanDefinitionStoreException("Unable to read Entities", e);
		}
	}
	  

}
